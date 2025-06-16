package controller;

import dao.RequestDAO;
import dao.RequestDetailDAO;
import dao.CategoryDAO;
import dao.UnitConversionDao;
import dao.UserDAO;
import model.Request;
import model.RequestDetail;
import model.Category;
import model.UnitConversion;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

            // Lấy thông tin yêu cầu và chi tiết yêu cầu (bao gồm materialCondition)
            Request req = requestDAO.getRequestById(requestId);
            List<RequestDetail> details = detailDAO.getRequestDetailsByRequestId(requestId);

            // Lấy user đang đăng nhập
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            User user = userDAO.getUserById(userId);

            // Lấy danh mục cha, con, đơn vị
            List<Category> parentCategories = categoryDAO.getParentCategories();
            List<Category> subCategories = new ArrayList<>();
            for (Category cat : categoryDAO.getAllCategories()) {
                if (cat.getParentId() != null) {
                    subCategories.add(cat);
                }
            }
            List<UnitConversion> unitList = unitDao.getAllunit();

            // Truyền sang JSP để hiển thị form chỉnh sửa
            request.setAttribute("request", req);
            request.setAttribute("details", details);
            request.setAttribute("userName", user.getFullname());
            request.setAttribute("parentCategories", parentCategories);
            request.setAttribute("subCategories", subCategories);
            request.setAttribute("unitList", unitList);
            request.getRequestDispatcher("editRequest.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải form chỉnh sửa yêu cầu: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            String requestType = request.getParameter("requestType");
            String reason = request.getParameter("reason");
            int itemCount = Integer.parseInt(request.getParameter("itemCount"));

            // Lấy các thông tin vật tư
            String[] parentCategoryIds = request.getParameterValues("parentCategoryId");
            String[] categoryIds = request.getParameterValues("categoryId");
            String[] materialNames = request.getParameterValues("materialName");
            String[] quantities = request.getParameterValues("quantity");
            String[] unitNames = request.getParameterValues("unit");
            String[] descriptions = request.getParameterValues("description");
            String[] materialConditions = request.getParameterValues("materialCondition");

            // Cập nhật lại yêu cầu
            RequestDAO requestDAO = new RequestDAO();
            requestDAO.updateRequestTypeAndReason(requestId, requestType, reason);
            requestDAO.updateRequestStatus(requestId, "Pending");

            // Xóa chi tiết cũ và thêm lại chi tiết mới
            RequestDetailDAO detailDAO = new RequestDetailDAO();
            detailDAO.deleteAllDetailsByRequestId(requestId);
            for (int i = 0; i < itemCount; i++) {
                RequestDetail detail = new RequestDetail();
                detail.setRequestId(requestId);
                detail.setParentCategoryId(parentCategoryIds != null && parentCategoryIds[i] != null && !parentCategoryIds[i].isEmpty() ? Integer.parseInt(parentCategoryIds[i]) : null);
                detail.setCategoryId(categoryIds != null && categoryIds[i] != null && !categoryIds[i].isEmpty() ? Integer.parseInt(categoryIds[i]) : null);
                detail.setMaterialName(materialNames[i]);
                detail.setQuantity(Integer.parseInt(quantities[i]));
                detail.setUnitName(unitNames[i]);
                detail.setDescription(descriptions[i]);
                // Bổ sung set materialCondition
                if (materialConditions != null && materialConditions[i] != null && !materialConditions[i].isEmpty()) {
                    detail.setMaterialCondition(materialConditions[i]);
                } else {
                    detail.setMaterialCondition("Mới");
                }
                detailDAO.addRequestDetail(detail);
            }

            // Gửi thông báo cho giám đốc (nếu cần)
            response.sendRedirect(request.getContextPath() + "/StaffDashboardServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi cập nhật yêu cầu: " + e.getMessage());
        }
    }
}
