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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));

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
            // Validate đầu vào
            if (requestType == null || requestType.trim().isEmpty()) {
                throw new Exception("Loại yêu cầu không được để trống.");
            }

            if (reason == null || reason.trim().isEmpty() || reason.length() > 500) {
                throw new Exception("Lý do không được để trống và không vượt quá 500 ký tự.");
            }

            if (materialNames == null || materialNames.length == 0) {
                throw new Exception("Phải có ít nhất một vật tư trong yêu cầu.");
            }

            for (int i = 0; i < materialNames.length; i++) {
                String name = materialNames[i];
                String quantity = quantities[i];
                String unit = unitNames[i];

                if (name == null || name.trim().isEmpty() || !name.matches("^[a-zA-Z0-9À-ỹ\\s\\-_\\(\\)]+$") || name.length() > 100)
                    throw new Exception("Tên vật tư ở dòng " + (i + 1) + " không hợp lệ.");

                if (quantity == null || !quantity.matches("^[0-9]+$")) 
                    throw new Exception("Số lượng ở dòng " + (i + 1) + " phải là số nguyên dương.");
                
                int q = Integer.parseInt(quantity);
                if (q <= 0 || q > 999999)
                    throw new Exception("Số lượng ở dòng " + (i + 1) + " phải trong khoảng từ 1 đến 999,999.");

                if (unit == null || unit.trim().isEmpty() || !unit.matches("^[a-zA-ZÀ-ỹ\\s]+$") || unit.length() > 20)
                    throw new Exception("Đơn vị ở dòng " + (i + 1) + " không hợp lệ.");
            }

            // Kiểm tra trạng thái yêu cầu
            RequestDAO requestDAO = new RequestDAO();
            Request req = requestDAO.getRequestById(requestId);
            if (!"Rejected".equals(req.getRequestStatus())) {
                request.getSession().setAttribute("error", "Chỉ yêu cầu bị từ chối mới được chỉnh sửa!");
                response.sendRedirect("requestList.jsp");
                return;
            }

            // Cập nhật dữ liệu
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
                detail.setMaterialCondition(
                    (materialConditions != null && i < materialConditions.length && !materialConditions[i].isEmpty())
                        ? materialConditions[i] : "Mới"
                );
                details.add(detail);
            }

            for (RequestDetail d : details) {
                detailDAO.addRequestDetail(d);
            }

            // Gửi lại thông báo cho giám đốc
            int directorId = requestDAO.getDirectorId();
            if (directorId != -1) {
                new NotificationDAO().addNotification(directorId,
                        "Yêu cầu vật tư #" + requestId + " đã được chỉnh sửa và gửi lại. Vui lòng xem xét.",
                        requestId);
            }

            response.sendRedirect(request.getContextPath() + "/staffDashboard");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            doGet(request, response); // gọi lại doGet để hiển thị lại form cùng lỗi
        }
    }
}
