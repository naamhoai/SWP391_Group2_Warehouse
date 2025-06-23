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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
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
            if (requestType == null || requestType.trim().isEmpty() ||
                reason == null || reason.trim().isEmpty() ||
                reason.length() > 500 ||
                materialNames == null || materialNames.length == 0) {
                request.setAttribute("error", "Vui lòng điền đầy đủ thông tin và ít nhất một vật tư.");
                doGet(request, response);
                return;
            }

            for (int i = 0; i < materialNames.length; i++) {
                String name = materialNames[i];
                String quantity = quantities[i];
                String unit = unitNames[i];

                if (name == null || name.trim().isEmpty() || name.length() > 100 || !name.matches("^[a-zA-Z0-9À-ỹ\\s\\-_\\(\\)]+$")) {
                    request.setAttribute("error", "Tên vật tư ở dòng " + (i + 1) + " không hợp lệ.");
                    doGet(request, response);
                    return;
                }

                if (quantity == null || !quantity.matches("^[0-9]+$") || Integer.parseInt(quantity) <= 0 || Integer.parseInt(quantity) > 999999) {
                    request.setAttribute("error", "Số lượng ở dòng " + (i + 1) + " không hợp lệ.");
                    doGet(request, response);
                    return;
                }

                if (unit == null || unit.trim().isEmpty() || unit.length() > 20 || !unit.matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
                    request.setAttribute("error", "Đơn vị ở dòng " + (i + 1) + " không hợp lệ.");
                    doGet(request, response);
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

            RequestDetailDAO detailDAO = new RequestDetailDAO();
            for (int i = 0; i < materialNames.length; i++) {
                RequestDetail detail = new RequestDetail();
                detail.setRequestId(requestId);
                detail.setMaterialName(materialNames[i]);
                detail.setQuantity(Integer.parseInt(quantities[i]));
                detail.setUnitName(unitNames[i]);
                detail.setMaterialCondition(materialConditions[i]);
                detailDAO.addRequestDetail(detail);
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
