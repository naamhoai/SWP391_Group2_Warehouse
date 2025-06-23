/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.NotificationDAO;
import dao.RequestDAO;
import java.io.PrintWriter;
import model.Notification;
import model.Request;

/**
 *
 * @author kimoa
 */
@WebServlet("/staffDashboard")
public class StaffDashboardServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StaffDashboardServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StaffDashboardServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra userId từ session
        Object userIdObj = request.getSession().getAttribute("userId");
        if (userIdObj == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (int) userIdObj;

        // Lấy thông báo từ chối của nhân viên
        NotificationDAO notificationDAO = new NotificationDAO();
        RequestDAO requestDAO = new RequestDAO();

        List<Notification> allNotifications = notificationDAO.getAllNotifications(userId);
        List<Notification> notifications = allNotifications.stream()
                .filter(noti -> {
                    try {
                        // Lấy thông tin yêu cầu
                        Request req = requestDAO.getRequestById(noti.getRequestId());
                        // Chỉ hiển thị thông báo của yêu cầu bị từ chối và chưa đọc
                        return !noti.isRead()
                                && req != null
                                && "Rejected".equals(req.getRequestStatus());
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        // Truyền sang JSP
        request.setAttribute("notifications", notifications);
        request.getRequestDispatcher("/staffDashboard.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
