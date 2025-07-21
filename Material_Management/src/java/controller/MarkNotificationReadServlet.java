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
            String purchaseOrderIdStr = request.getParameter("purchaseOrderId");

            if (notificationIdStr == null || notificationIdStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số notificationId");
                return;
            }

            int notificationId = Integer.parseInt(notificationIdStr);

            NotificationDAO notificationDAO = new NotificationDAO();
            notificationDAO.markAsRead(notificationId);

            // Xử lý chuyển hướng dựa trên loại thông báo
            if (purchaseOrderIdStr != null && !purchaseOrderIdStr.isEmpty()) {
                // Thông báo về purchase order
                response.sendRedirect("purchaseOrderDetail?id=" + purchaseOrderIdStr);
            } else if (requestIdStr != null && !requestIdStr.isEmpty()) {
                // Thông báo về request
                response.sendRedirect("viewRequestDetail?requestId=" + requestIdStr);
            } else {
                // Mặc định về trang chủ
                response.sendRedirect("directorDashboard.jsp");
            }
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
