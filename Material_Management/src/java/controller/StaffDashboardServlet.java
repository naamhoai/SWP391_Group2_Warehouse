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
import com.google.gson.Gson;
import dao.InventoryDAO;
import dao.PurchaseOrderDAO;
import dao.DeliveryDAO;
import model.PurchaseOrder;
import dao.PurchaseOrderDetailDAO;
import model.Delivery;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

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

        // ==== Lấy dữ liệu biểu đồ mua/xuất vật tư theo tháng ====
        InventoryDAO inventoryDAO = new InventoryDAO();
        // Lấy khoảng thời gian mặc định: 6 tháng gần nhất
        java.time.YearMonth now = java.time.YearMonth.now();
        java.time.YearMonth startYM = now.minusMonths(5);
        java.time.YearMonth endYM = now;
        String startDate = startYM.toString(); // yyyy-MM
        String endDate = endYM.toString();
        // Tạo danh sách các tháng
        java.util.List<String> monthLabels = new ArrayList<>();
        for (java.time.YearMonth m = startYM; !m.isAfter(endYM); m = m.plusMonths(1)) {
            monthLabels.add(m.toString());
        }
        // Định dạng tháng sang tiếng Việt
        java.util.List<String> formattedMonthLabels = new ArrayList<>();
        String[] vietnameseMonths = {"T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"};
        for (String month : monthLabels) {
            String[] parts = month.split("-");
            if (parts.length == 2) {
                int monthNum = Integer.parseInt(parts[1]);
                String vietnameseMonth = vietnameseMonths[monthNum - 1];
                formattedMonthLabels.add(vietnameseMonth + "/" + parts[0]);
            } else {
                formattedMonthLabels.add(month);
            }
        }
        Map<String, Integer> importByMonth = inventoryDAO.getTotalImportedByMonthRange(startDate, endDate);
        Map<String, Integer> exportByMonth = inventoryDAO.getTotalExportedByMonthRange(startDate, endDate);
        java.util.List<Integer> importValues = new ArrayList<>();
        java.util.List<Integer> exportValues = new ArrayList<>();
        for (String month : monthLabels) {
            importValues.add(importByMonth.getOrDefault(month, 0));
            exportValues.add(exportByMonth.getOrDefault(month, 0));
        }
        Gson gson = new Gson();
        request.setAttribute("importExportMonthLabels", formattedMonthLabels);
        request.setAttribute("importByMonthJson", gson.toJson(importValues));
        request.setAttribute("exportByMonthJson", gson.toJson(exportValues));

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
