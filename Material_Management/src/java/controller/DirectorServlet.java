package controller;

import dao.NotificationDAO;
import dao.RequestDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Notification;
import model.Request;

@WebServlet(name = "DirectorServlet", urlPatterns = {"/director/*"})
public class DirectorServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy danh sách yêu cầu chờ duyệt
            RequestDAO requestDAO = new RequestDAO();
            List<Request> requests = requestDAO.getPendingRequests();
            request.setAttribute("requests", requests);

            // Lấy thông báo cho giám đốc
            dao.UserDAO userDAO = new dao.UserDAO();
            Integer directorId = userDAO.getDirectorId();
            
            // Debug info
            System.out.println("Director ID: " + directorId);
            
            if (directorId != null) {
                NotificationDAO notificationDAO = new NotificationDAO();
                
               
                // Lấy tất cả thông báo
                List<Notification> notifications = notificationDAO.getAllNotifications(directorId);
                
                // Debug info
                System.out.println("Number of notifications: " + (notifications != null ? notifications.size() : 0));
                if (notifications != null) {
                    int unreadCount = 0;
                    for (Notification n : notifications) {
                        if (!n.isRead()) unreadCount++;
                    }
                    System.out.println("Unread notifications: " + unreadCount);
                }
                
                request.setAttribute("notifications", notifications);
            } else {
                System.out.println("Warning: No director ID found!");
            }

            request.getRequestDispatcher("/view/directorDashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải dashboard: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
    }
}
