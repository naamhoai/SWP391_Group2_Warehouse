package controller;

import dao.*;
import model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "EditRequestServlet", urlPatterns = {"/editRequest"})
public class EditRequestServlet extends HttpServlet {

    private void prepareEditForm(HttpServletRequest request, int requestId) throws Exception {
        RequestDAO requestDAO = new RequestDAO();
        RequestDetailDAO detailDAO = new RequestDetailDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        UnitConversionDao unitDao = new UnitConversionDao();
        UserDAO userDAO = new UserDAO();
        MaterialDAO materialDAO = new MaterialDAO();

        Request req = requestDAO.getRequestById(requestId);
        List<RequestDetail> details = detailDAO.getRequestDetailsByRequestId(requestId);
        Integer roleId = (Integer) request.getSession().getAttribute("roleId");
        boolean canEdit = roleId != null && roleId == 4 && "Rejected".equals(req.getRequestStatus());
        if (!canEdit) {
            request.setAttribute("error", "Bạn không có quyền chỉnh sửa yêu cầu này");
        }

        List<Category> parentCategories = categoryDAO.getParentCategories();
        List<Category> subCategories = categoryDAO.getAllCategories().stream()
                .filter(cat -> cat.getParentId() != null)
                .collect(Collectors.toList());

        request.setAttribute("request", req);
        request.setAttribute("details", details);
        request.setAttribute("userName", userDAO.getUserById((Integer) request.getSession().getAttribute("userId")).getFullname());
        request.setAttribute("parentCategories", parentCategories);
        request.setAttribute("subCategories", subCategories);
        request.setAttribute("unitList", unitDao.getAllunit());
        request.setAttribute("materialList", materialDAO.getMaterial());
    }

    private String validateLine(int index, String name, String quantity, String unit) {
        if (name == null || name.trim().isEmpty()) return "Tên vật tư ở dòng " + (index + 1) + " không được để trống.";
        if (!name.matches("^[a-zA-Z0-9À-ỹ\\s\\-_\\(\\)]+$")) return "Tên vật tư ở dòng " + (index + 1) + " chứa ký tự không hợp lệ.";
        if (name.length() > 100) return "Tên vật tư ở dòng " + (index + 1) + " không được vượt quá 100 ký tự.";

        if (quantity == null || quantity.trim().isEmpty()) return "Số lượng ở dòng " + (index + 1) + " không được để trống.";
        if (!quantity.matches("^[0-9]+$")) return "Số lượng ở dòng " + (index + 1) + " phải là số nguyên dương.";
        int q = Integer.parseInt(quantity);
        if (q <= 0) return "Số lượng ở dòng " + (index + 1) + " phải là số nguyên dương.";
        if (q > 999999) return "Số lượng ở dòng " + (index + 1) + " không được vượt quá 999,999.";

        if (unit == null || unit.trim().isEmpty()) return "Đơn vị ở dòng " + (index + 1) + " không được để trống.";
        if (!unit.matches("^[a-zA-ZÀ-ỹ\\s]+$")) return "Đơn vị ở dòng " + (index + 1) + " chỉ được chứa chữ cái và khoảng trắng.";
        if (unit.length() > 20) return "Đơn vị ở dòng " + (index + 1) + " không được vượt quá 20 ký tự.";

        return null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            prepareEditForm(request, requestId);
            request.getRequestDispatcher("editRequest.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Lỗi khi tải form chỉnh sửa yêu cầu: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestType = request.getParameter("requestType");
        String reason = request.getParameter("reason");
        String[] materialNames = request.getParameterValues("materialName");
        String[] quantities = request.getParameterValues("quantity");
        String[] unitNames = request.getParameterValues("unit");
        String[] materialConditions = request.getParameterValues("materialCondition");
        int requestId = Integer.parseInt(request.getParameter("requestId"));

        try {
            if (requestType == null || requestType.trim().isEmpty()) {
                request.setAttribute("error", "Loại yêu cầu không được để trống.");
                prepareEditForm(request, requestId);
                request.getRequestDispatcher("editRequest.jsp").forward(request, response);
                return;
            }

            if (reason == null || reason.trim().isEmpty()) {
                request.setAttribute("error", "Lý do không được để trống.");
                prepareEditForm(request, requestId);
                request.getRequestDispatcher("editRequest.jsp").forward(request, response);
                return;
            }

            if (reason.length() > 500) {
                request.setAttribute("error", "Lý do không được vượt quá 500 ký tự.");
                prepareEditForm(request, requestId);
                request.getRequestDispatcher("editRequest.jsp").forward(request, response);
                return;
            }

            if (materialNames == null || materialNames.length == 0) {
                request.setAttribute("error", "Phải có ít nhất một vật tư trong yêu cầu.");
                prepareEditForm(request, requestId);
                request.getRequestDispatcher("editRequest.jsp").forward(request, response);
                return;
            }

            for (int i = 0; i < materialNames.length; i++) {
                String error = validateLine(i, materialNames[i], quantities[i], unitNames[i]);
                if (error != null) {
                    request.setAttribute("error", error);
                    prepareEditForm(request, requestId);
                    request.getRequestDispatcher("editRequest.jsp").forward(request, response);
                    return;
                }
            }

            RequestDAO requestDAO = new RequestDAO();
            Request req = requestDAO.getRequestById(requestId);
            if (!"Rejected".equals(req.getRequestStatus())) {
                request.getSession().setAttribute("error", "Chỉ yêu cầu ở trạng thái Rejected mới được chỉnh sửa!");
                response.sendRedirect("requestList.jsp");
                return;
            }

            requestDAO.updateRequestTypeAndReason(requestId, requestType, reason);
            requestDAO.updateRequestStatus(requestId, "Pending");

            RequestDetailDAO detailDAO = new RequestDetailDAO();
            detailDAO.deleteAllDetailsByRequestId(requestId);

            List<RequestDetail> details = new ArrayList<>();
            for (int i = 0; i < materialNames.length; i++) {
                RequestDetail detail = new RequestDetail();
                detail.setRequestId(requestId);
                detail.setMaterialName(materialNames[i]);
                detail.setQuantity(Integer.parseInt(quantities[i]));
                detail.setUnitName(unitNames[i]);
                detail.setMaterialCondition(materialConditions != null && i < materialConditions.length && !materialConditions[i].isEmpty()
                        ? materialConditions[i] : "Mới");
                details.add(detail);
            }

            for (RequestDetail d : details) {
                detailDAO.addRequestDetail(d);
            }

            int directorId = requestDAO.getDirectorId();
            if (directorId != -1) {
                new NotificationDAO().addNotification(
                        directorId,
                        "Yêu cầu vật tư #" + requestId + " đã được chỉnh sửa và gửi lại. Vui lòng xem xét.",
                        requestId
                );
            }

            response.sendRedirect(request.getContextPath() + "/staffDashboard");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Lỗi khi cập nhật yêu cầu: " + e.getMessage());
        }
    }
}
