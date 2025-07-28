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
        
        // Lấy tham số thời gian từ request
        String startMonth = request.getParameter("startMonth");
        String startYear = request.getParameter("startYear");
        String endMonth = request.getParameter("endMonth");
        String endYear = request.getParameter("endYear");
        
        // Tạo startDate và endDate từ tham số
        String startDate = null;
        String endDate = null;
        
        if (startMonth != null && startYear != null && !startMonth.isEmpty() && !startYear.isEmpty()) {
            startDate = startYear + "-" + (startMonth.length() == 1 ? "0" + startMonth : startMonth);
        }
        if (endMonth != null && endYear != null && !endMonth.isEmpty() && !endYear.isEmpty()) {
            endDate = endYear + "-" + (endMonth.length() == 1 ? "0" + endMonth : endMonth);
        }
        
        // Nếu không có tham số, sử dụng 6 tháng gần nhất
        if (startDate == null || startDate.isEmpty()) {
            java.time.LocalDate now = java.time.LocalDate.now();
            startDate = now.minusMonths(5).toString().substring(0, 7);
        }
        if (endDate == null || endDate.isEmpty()) {
            java.time.LocalDate now = java.time.LocalDate.now();
            endDate = now.toString().substring(0, 7);
        }
        
        // Lấy dữ liệu nhập/xuất theo khoảng thời gian
        List<String> monthLabels = new ArrayList<>();
        java.time.YearMonth startYM = java.time.YearMonth.parse(startDate);
        java.time.YearMonth endYM = java.time.YearMonth.parse(endDate);
        for (java.time.YearMonth m = startYM; !m.isAfter(endYM); m = m.plusMonths(1)) {
            monthLabels.add(m.toString());
        }
        
        // Định dạng tháng sang tiếng Việt
        List<String> formattedMonthLabels = new ArrayList<>();
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
        List<Integer> importValues = new ArrayList<>();
        List<Integer> exportValues = new ArrayList<>();
        for (String month : monthLabels) {
            importValues.add(importByMonth.getOrDefault(month, 0));
            exportValues.add(exportByMonth.getOrDefault(month, 0));
        }
        
        Gson gson = new Gson();
        request.setAttribute("importExportMonthLabels", formattedMonthLabels);
        request.setAttribute("importByMonthJson", gson.toJson(importValues));
        request.setAttribute("exportByMonthJson", gson.toJson(exportValues));
        
        // Lưu tham số thời gian để hiển thị trên form
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        
        // Tách tháng và năm để hiển thị trên dropdown
        if (startDate != null && startDate.length() >= 7) {
            String[] startParts = startDate.split("-");
            if (startParts.length == 2) {
                request.setAttribute("startMonth", Integer.parseInt(startParts[1]));
                request.setAttribute("startYear", Integer.parseInt(startParts[0]));
            }
        }
        if (endDate != null && endDate.length() >= 7) {
            String[] endParts = endDate.split("-");
            if (endParts.length == 2) {
                request.setAttribute("endMonth", Integer.parseInt(endParts[1]));
                request.setAttribute("endYear", Integer.parseInt(endParts[0]));
            }
        }

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
