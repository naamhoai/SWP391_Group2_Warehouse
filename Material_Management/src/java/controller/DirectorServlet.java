package controller;

import dao.NotificationDAO;
import dao.RequestDAO;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Notification;
import model.Request;
import model.User;

@WebServlet(name = "DirectorServlet", urlPatterns = {"/director/*"})
public class DirectorServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");

            if (userId != null) {
                UserDAO userDAO = new UserDAO();
                User user = userDAO.getUserById(userId); // ✅ lấy user theo userId

                if (user != null) {
                    request.setAttribute("user", user); // ✅ gán user vào request để dùng ở JSP

                    // Lấy thông báo theo chính user đang đăng nhập
                    NotificationDAO notificationDAO = new NotificationDAO();
                    List<Notification> notifications = notificationDAO.getAllNotifications(userId);
                    request.setAttribute("notifications", notifications);
                }
            } else {
                System.out.println("Warning: userId from session is null.");
            }

            // Lấy danh sách yêu cầu chờ duyệt
            RequestDAO requestDAO = new RequestDAO();
            List<Request> requests = requestDAO.getPendingRequests();
            request.setAttribute("requests", requests);

            request.getRequestDispatcher("/view/directorDashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Lỗi khi tải dashboard: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
    }
}
