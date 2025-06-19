package controller;

import dao.RequestDAO;
import dao.RequestDetailDAO;
import dao.CategoryDAO;
import dao.UnitConversionDao;
import dao.UserDAO;
import dao.NotificationDAO;
import dao.MaterialDAO;
import model.Request;
import model.RequestDetail;
import model.Category;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "EditRequestServlet", urlPatterns = {"/editRequest"})
public class EditRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));

            // Khởi tạo các DAO cần thiết
            RequestDAO requestDAO = new RequestDAO();
            RequestDetailDAO detailDAO = new RequestDetailDAO();
            CategoryDAO categoryDAO = new CategoryDAO();
            UnitConversionDao unitDao = new UnitConversionDao();
            UserDAO userDAO = new UserDAO();
            MaterialDAO materialDAO = new MaterialDAO();

            // Lấy dữ liệu cần thiết
            Request req = requestDAO.getRequestById(requestId);
            List<RequestDetail> details = detailDAO.getRequestDetailsByRequestId(requestId);
            User user = userDAO.getUserById((Integer) request.getSession().getAttribute("userId"));
            List<Category> parentCategories = categoryDAO.getParentCategories();

            // Lọc danh mục con
            List<Category> subCategories = categoryDAO.getAllCategories().stream()
                    .filter(cat -> cat.getParentId() != null)
                    .collect(Collectors.toList());

            // Set attributes cho JSP
            request.setAttribute("request", req);
            request.setAttribute("details", details);
            request.setAttribute("userName", user.getFullname());
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
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            int itemCount = Integer.parseInt(request.getParameter("itemCount"));

            // Cập nhật thông tin yêu cầu
            RequestDAO requestDAO = new RequestDAO();
            requestDAO.updateRequestTypeAndReason(
                    requestId,
                    request.getParameter("requestType"),
                    request.getParameter("reason")
            );
            requestDAO.updateRequestStatus(requestId, "Pending");

            // Cập nhật chi tiết yêu cầu
            RequestDetailDAO detailDAO = new RequestDetailDAO();
            detailDAO.deleteAllDetailsByRequestId(requestId);

            String[] parentCategoryIds = request.getParameterValues("parentCategoryId");
            String[] categoryIds = request.getParameterValues("categoryId");
            String[] materialNames = request.getParameterValues("materialName");
            String[] quantities = request.getParameterValues("quantity");
            String[] unitNames = request.getParameterValues("unit");
            String[] descriptions = request.getParameterValues("description");
            String[] materialConditions = request.getParameterValues("materialCondition");

            for (int i = 0; i < itemCount; i++) {
                RequestDetail detail = new RequestDetail();
                detail.setRequestId(requestId);
                detail.setParentCategoryId(parentCategoryIds != null && !parentCategoryIds[i].isEmpty()
                        ? Integer.parseInt(parentCategoryIds[i]) : null);
                detail.setCategoryId(categoryIds != null && !categoryIds[i].isEmpty()
                        ? Integer.parseInt(categoryIds[i]) : null);
                detail.setMaterialName(materialNames[i]);
                detail.setQuantity(Integer.parseInt(quantities[i]));
                detail.setUnitName(unitNames[i]);
                detail.setDescription(descriptions[i]);
                detail.setMaterialCondition(materialConditions != null && !materialConditions[i].isEmpty()
                        ? materialConditions[i] : "Mới");

                detailDAO.addRequestDetail(detail);
            }

            // Gửi thông báo cho giám đốc
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
