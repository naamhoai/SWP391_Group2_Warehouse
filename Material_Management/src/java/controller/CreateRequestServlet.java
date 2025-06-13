package controller;

import dao.NotificationDAO;
import dao.RequestDAO;
import dao.RequestDetailDAO;
import dao.UnitConversionDao;
import model.Request;
import model.RequestDetail;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import model.UnitConversion;
import model.User;

@WebServlet(name = "CreateRequestServlet", urlPatterns = {"/createRequest"})
public class CreateRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            dao.CategoryDAO categoryDAO = new dao.CategoryDAO();
            // Lấy danh mục cha (parent_id == null)
            List<Category> parentCategories = categoryDAO.getParentCategories();
// Lấy danh mục con (parent_id != null)
            List<Category> subCategories = new ArrayList<>();
            for (Category cat : categoryDAO.getAllCategories()) {
                if (cat.getParentId() != null) {
                    subCategories.add(cat);
                }
            }
            // Lấy userId từ session
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            dao.UserDAO userDAO = new dao.UserDAO();
// Lấy thông tin user từ DB
            User user = userDAO.getUserById(userId);

// Truyền tên sang JSP
            request.setAttribute("userName", user.getFullname()); // hoặc getFullName()
            UnitConversionDao unitDao = new UnitConversionDao();
            List<UnitConversion> unitList = unitDao.getAllunit();
            request.setAttribute("unitList", unitList);
            request.setAttribute("parentCategories", parentCategories);
            request.setAttribute("subCategories", subCategories);
            request.getRequestDispatcher("requestForm.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải danh mục vật tư: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String requestType = request.getParameter("requestType");
            String reason = request.getParameter("reason");
            int userId = (int) request.getSession().getAttribute("userId"); // Đảm bảo đã login và có userId trong session
            Timestamp now = new Timestamp(System.currentTimeMillis());

            Request req = new Request();
            req.setRequestType(requestType);
            req.setUserId(userId);
            req.setReason(reason);
            req.setRequestStatus("Pending");
            req.setCreatedAt(now);

            RequestDAO requestDAO = new RequestDAO();
            int requestId = requestDAO.addRequest(req);

            int itemCount = Integer.parseInt(request.getParameter("itemCount"));
            List<RequestDetail> details = new ArrayList<>();

            String[] parentCategoryIds = request.getParameterValues("parentCategoryId");
            String[] categoryIds = request.getParameterValues("categoryId");
            String[] materialNames = request.getParameterValues("materialName");
            String[] quantities = request.getParameterValues("quantity");
            String[] unitNames = request.getParameterValues("unit");
            String[] descriptions = request.getParameterValues("description");

            for (int i = 0; i < itemCount; i++) {
                RequestDetail detail = new RequestDetail();
                detail.setRequestId(requestId);

                // Lưu parentCategoryId
                if (parentCategoryIds != null && parentCategoryIds[i] != null && !parentCategoryIds[i].isEmpty()) {
                    detail.setParentCategoryId(Integer.parseInt(parentCategoryIds[i]));
                } else {
                    detail.setParentCategoryId(null);
                }

                // Lưu categoryId
                if (categoryIds != null && categoryIds[i] != null && !categoryIds[i].isEmpty()) {
                    detail.setCategoryId(Integer.parseInt(categoryIds[i]));
                } else {
                    detail.setCategoryId(null);
                }

                detail.setMaterialName(materialNames[i]);
                detail.setQuantity(Integer.parseInt(quantities[i]));
                detail.setUnitName(unitNames[i]);
                detail.setDescription(descriptions[i]);
                details.add(detail);
            }

            RequestDetailDAO detailDAO = new RequestDetailDAO();
            for (RequestDetail d : details) {
                detailDAO.addRequestDetail(d);
            }
            // Thêm phần gửi thông báo cho giám đốc
            dao.UserDAO userDAO = new dao.UserDAO();
            Integer directorId = userDAO.getDirectorId();
            User sender = userDAO.getUserById(userId);
            String senderName = sender != null ? sender.getFullname() : "Nhân viên";

            if (directorId != null) {
                NotificationDAO notificationDAO = new NotificationDAO();
                String message = "Có yêu cầu vật tư mới từ " + senderName;
                notificationDAO.addNotification(directorId, message, requestId);
            }

            response.sendRedirect("successRequest.jsp"); // hoặc chuyển hướng về trang xác nhận
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi gửi yêu cầu: " + e.getMessage());
        }
    }

}
