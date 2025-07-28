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
import com.google.gson.Gson;
import dao.InventoryDAO;
import dao.MaterialDAO;
import java.util.ArrayList;
import java.util.Map;

@WebServlet("/warehouseStaffDashboard")
public class WarehouseStaffDashboardServlet extends HttpServlet {
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

        // Lấy thông báo từ chối của nhân viên kho (nếu có logic riêng, có thể sửa lại)
        NotificationDAO notificationDAO = new NotificationDAO();
        RequestDAO requestDAO = new RequestDAO();
        List<model.Notification> allNotifications = notificationDAO.getAllNotifications(userId);
        List<model.Notification> notifications = allNotifications.stream()
                .filter(noti -> {
                    try {
                        model.Request req = requestDAO.getRequestById(noti.getRequestId());
                        return !noti.isRead()
                                && req != null
                                && "Rejected".equals(req.getRequestStatus());
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        // ==== Lấy dữ liệu cho stats ====
        // Lấy tổng vật tư từ MaterialDAO (giống MaterialListServlet)
        MaterialDAO materialDAO = new MaterialDAO();
        int totalMaterials = materialDAO.getTotalMaterialsForAdmin(null, null, null, null);
        request.setAttribute("totalMaterials", totalMaterials);

        // Lấy tổng vật tư tồn kho từ InventoryDAO
        InventoryDAO inventoryDAO = new InventoryDAO();
        List<model.Inventory> inventories = inventoryDAO.getInventoryWithMaterialInfo();
        int totalInventoryItems = 0;
        for (model.Inventory inv : inventories) {
            totalInventoryItems += inv.getQuantityOnHand();
        }
        request.setAttribute("totalInventoryItems", totalInventoryItems);

        // ==== Lấy dữ liệu biểu đồ mua/xuất vật tư theo tháng ====
        java.time.YearMonth now = java.time.YearMonth.now();
        java.time.YearMonth startYM = now.minusMonths(5);
        java.time.YearMonth endYM = now;
        String startDate = startYM.toString(); // yyyy-MM
        String endDate = endYM.toString();
        java.util.List<String> monthLabels = new ArrayList<>();
        for (java.time.YearMonth m = startYM; !m.isAfter(endYM); m = m.plusMonths(1)) {
            monthLabels.add(m.toString());
        }
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

        // Debug: In log để kiểm tra dữ liệu
        System.out.println("=== DEBUG WAREHOUSE STAFF DASHBOARD ===");
        System.out.println("totalMaterials: " + totalMaterials);
        System.out.println("totalInventoryItems: " + totalInventoryItems);
        System.out.println("formattedMonthLabels: " + formattedMonthLabels);
        System.out.println("importValues: " + importValues);
        System.out.println("exportValues: " + exportValues);
        System.out.println("importByMonthJson: " + gson.toJson(importValues));
        System.out.println("exportByMonthJson: " + gson.toJson(exportValues));
        System.out.println("=====================================");

        // Truyền sang JSP
        request.setAttribute("notifications", notifications);
        request.getRequestDispatcher("/warehouseStaffDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
} 