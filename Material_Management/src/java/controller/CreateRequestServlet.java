package controller;

import dao.*;
import model.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CreateRequestServlet", urlPatterns = {"/createRequest"})
public class CreateRequestServlet extends HttpServlet {

    private void prepareRequestForm(HttpServletRequest request) throws Exception {
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> parentCategories = categoryDAO.getParentCategories();
        List<Category> subCategories = new ArrayList<>();
        for (Category cat : categoryDAO.getAllCategories()) {
            if (cat.getParentId() != null) {
                subCategories.add(cat);
            }
        }

        Integer userId = (Integer) request.getSession().getAttribute("userId");
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserById(userId);

        MaterialDAO materialDAO = new MaterialDAO();
        List<Material> materials = materialDAO.getMaterial();
        UnitConversionDao unitDao = new UnitConversionDao();
        List<UnitConversion> unitList = unitDao.getAllunit();

        request.setAttribute("userName", user.getFullname());
        request.setAttribute("materialList", materials);
        request.setAttribute("unitList", unitList);
        request.setAttribute("parentCategories", parentCategories);
        request.setAttribute("subCategories", subCategories);
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            prepareRequestForm(request);
            request.getRequestDispatcher("requestForm.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải form: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestType = request.getParameter("requestType");
        String reason = request.getParameter("reason");
        String[] materialNames = request.getParameterValues("materialName");
        String[] quantities = request.getParameterValues("quantity");
        String[] unitNames = request.getParameterValues("unit");
        String[] materialConditions = request.getParameterValues("materialCondition");

        try {
            if (requestType == null || requestType.trim().isEmpty()) {
                request.setAttribute("error", "Loại yêu cầu không được để trống.");
                prepareRequestForm(request);
                request.getRequestDispatcher("requestForm.jsp").forward(request, response);
                return;
            }

            if (reason == null || reason.trim().isEmpty()) {
                request.setAttribute("error", "Lý do không được để trống.");
                prepareRequestForm(request);
                request.getRequestDispatcher("requestForm.jsp").forward(request, response);
                return;
            }

            if (reason.length() > 500) {
                request.setAttribute("error", "Lý do không được vượt quá 500 ký tự.");
                prepareRequestForm(request);
                request.getRequestDispatcher("requestForm.jsp").forward(request, response);
                return;
            }

            if (materialNames == null || materialNames.length == 0) {
                request.setAttribute("error", "Phải có ít nhất một vật tư trong yêu cầu.");
                prepareRequestForm(request);
                request.getRequestDispatcher("requestForm.jsp").forward(request, response);
                return;
            }

            for (int i = 0; i < materialNames.length; i++) {
                String error = validateLine(i, materialNames[i], quantities[i], unitNames[i]);
                if (error != null) {
                    request.setAttribute("error", error);
                    prepareRequestForm(request);
                    request.getRequestDispatcher("requestForm.jsp").forward(request, response);
                    return;
                }
            }

            int userId = (int) request.getSession().getAttribute("userId");
            Timestamp now = new Timestamp(System.currentTimeMillis());

            Request req = new Request();
            req.setRequestType(requestType);
            req.setUserId(userId);
            req.setReason(reason);
            req.setRequestStatus("Pending");
            req.setCreatedAt(now);

            RequestDAO requestDAO = new RequestDAO();
            int requestId = requestDAO.addRequest(req);

            List<RequestDetail> details = new ArrayList<>();
            for (int i = 0; i < materialNames.length; i++) {
                RequestDetail detail = new RequestDetail();
                detail.setRequestId(requestId);
                detail.setMaterialName(materialNames[i]);
                detail.setQuantity(Integer.parseInt(quantities[i]));
                detail.setUnitName(unitNames[i]);
                detail.setMaterialCondition(materialConditions[i]);
                details.add(detail);
            }

            RequestDetailDAO detailDAO = new RequestDetailDAO();
            for (RequestDetail d : details) {
                detailDAO.addRequestDetail(d);
            }

            UserDAO userDAO = new UserDAO();
            Integer directorId = userDAO.getDirectorId();
            User sender = userDAO.getUserById(userId);

            if (directorId != null) {
                NotificationDAO notificationDAO = new NotificationDAO();
                String message = "Có yêu cầu vật tư mới từ " + sender.getFullname();
                notificationDAO.addNotification(directorId, message, requestId);
            }

            response.sendRedirect("successRequest.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi gửi yêu cầu: " + e.getMessage());
        }
    }
}
