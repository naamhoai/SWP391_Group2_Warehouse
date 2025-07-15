package controller;

import dao.NotificationDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "MarkNotificationReadServlet", urlPatterns = {"/markNotificationRead"})
public class MarkNotificationReadServlet extends HttpServlet {

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String notificationIdStr = request.getParameter("notificationId");
            String requestIdStr = request.getParameter("requestId");

            if (notificationIdStr == null || notificationIdStr.isEmpty()
                    || requestIdStr == null || requestIdStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số notificationId hoặc requestId");
                return;
            }

            int notificationId = Integer.parseInt(notificationIdStr);
            int requestId = Integer.parseInt(requestIdStr);

            NotificationDAO notificationDAO = new NotificationDAO();
            notificationDAO.markAsRead(notificationId);

            response.sendRedirect("viewRequestDetail?requestId=" + requestId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi đánh dấu đã đọc: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "";
    }
}
