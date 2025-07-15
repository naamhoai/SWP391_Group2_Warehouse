package controller;

import dao.NotificationDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Notification;
import model.User;
import com.google.gson.Gson;
import java.util.ArrayList;

@WebServlet("/getNotifications")
public class GetNotificationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession();
            User userSession = (User) session.getAttribute("Admin");
            if (userSession == null) {
                userSession = (User) session.getAttribute("user");
            }
            if (userSession == null) {
                userSession = (User) session.getAttribute("staff");
            }
            System.out.println("[DEBUG] userSession from session: " + userSession);
            Integer userId = (userSession != null) ? userSession.getUser_id() : null;
            System.out.println("[DEBUG] userId trong servlet: " + userId);

            if (userId != null) {
                UserDAO userDAO = new UserDAO();
                User user = userDAO.getUserById(userId);
                System.out.println("[DEBUG] user from DB: " + user);

                if (user != null && user.getRole() != null) {
                    String userRole = user.getRole().getRolename();
                    System.out.println("[DEBUG] userRole: " + userRole);
                    NotificationDAO notificationDAO = new NotificationDAO();
                    List<Notification> notifications = new ArrayList<>();

                    switch (userRole) {
                        case "Giám đốc":
                            notifications = notificationDAO.getDirectorNotifications(userId);
                            System.out.println("[DEBUG] getDirectorNotifications, count: " + notifications.size());
                            break;
                        case "Nhân viên kho":
                            notifications = notificationDAO.getWarehouseStaffNotifications(userId);
                            System.out.println("[DEBUG] getWarehouseStaffNotifications, count: " + notifications.size());
                            break;
                        case "Nhân viên công ty":
                            notifications = notificationDAO.getStaffNotifications(userId);
                            System.out.println("[DEBUG] getStaffNotifications, count: " + notifications.size());
                            break;
                        case "Admin":
                            notifications = notificationDAO.getAllNotifications(userId);
                            System.out.println("[DEBUG] getAllNotifications (Admin), count: " + notifications.size());
                            break;
                        default:
                            notifications = notificationDAO.getAllNotifications(userId);
                            System.out.println("[DEBUG] getAllNotifications (default), count: " + notifications.size());
                            break;
                    }

                    System.out.println("[DEBUG] Notifications trả về cho userId=" + userId + ":");
                    for (Notification n : notifications) {
                        System.out.println("  - id=" + n.getId() + ", requestId=" + n.getRequestId() + ", message=" + n.getMessage() + ", isRead=" + n.isRead() + ", createdAt=" + n.getCreatedAt());
                    }

                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(notifications);
                    out.print(jsonResponse);

                } else {
                    System.out.println("[DEBUG] user null hoặc không có role");
                    out.print("[]");
                }
            } else {
                System.out.println("[DEBUG] userId null");
                out.print("[]");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("[]");
        }
    }
}
