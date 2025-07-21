package controller;

import dao.InventoryDAO;
import dao.ExportMaterialDAO;
import dao.RequestDAO;
import dao.DeliveryDAO;
import model.Inventory;
import model.ExportMaterial;
import model.Delivery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/adminDashboard"})
public class AdminDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        InventoryDAO inventoryDAO = new InventoryDAO();
        List<Inventory> inventories = inventoryDAO.getInventoryWithMaterialInfo();
        Set<Integer> materialIds = new HashSet<>();
        long totalInventoryValue = 0;
        for (Inventory inv : inventories) {
            materialIds.add(inv.getMaterialId());
            int price = Math.max(0, inv.getPrice());
            int qty = Math.max(0, inv.getQuantityOnHand());
            totalInventoryValue += (long) qty * price;
        }
        int totalMaterialTypes = materialIds.size();
        request.setAttribute("totalItems", totalMaterialTypes);
        request.setAttribute("totalInventoryValue", totalInventoryValue);

        ExportMaterialDAO exportDAO = new ExportMaterialDAO();
        List<ExportMaterial> topExportedItems = exportDAO.getTopExportedMaterials(5);
        request.setAttribute("topExportedItems", topExportedItems);

        RequestDAO requestDAO = new RequestDAO();
        int outgoingCount = requestDAO.countAllRequests();
        Map<String, Integer> requestStats = new HashMap<>();
        requestStats.put("outgoingCount", outgoingCount);
        request.setAttribute("requestStats", requestStats);

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
        Map<String, Integer> importByMonth = inventoryDAO.getTotalImportedByMonthRange(startDate, endDate);
        Map<String, Integer> exportByMonth = inventoryDAO.getTotalExportedByMonthRange(startDate, endDate);
        
        // Tạo danh sách tháng trong khoảng thời gian
        Set<String> allMonths = new TreeSet<>();
        allMonths.addAll(importByMonth.keySet());
        allMonths.addAll(exportByMonth.keySet());
        
        // Nếu không có dữ liệu thực, tạo dữ liệu mẫu cho khoảng thời gian
        if (allMonths.isEmpty()) {
            java.time.YearMonth start = java.time.YearMonth.parse(startDate);
            java.time.YearMonth end = java.time.YearMonth.parse(endDate);
            for (java.time.YearMonth m = start; !m.isAfter(end); m = m.plusMonths(1)) {
                allMonths.add(m.toString());
            }
        }
        
        List<String> monthLabels = new ArrayList<>(allMonths);
        Collections.sort(monthLabels);
        
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
        
        List<Integer> importValues = new ArrayList<>();
        List<Integer> exportValues = new ArrayList<>();
        for (String month : monthLabels) {
            importValues.add(importByMonth.getOrDefault(month, 0));
            exportValues.add(exportByMonth.getOrDefault(month, 0));
        }
        
        request.setAttribute("importExportMonthLabels", formattedMonthLabels);
        request.setAttribute("importByMonth", importValues);
        request.setAttribute("exportByMonth", exportValues);
        
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

        DeliveryDAO deliveryDAO = new DeliveryDAO();
        int deliveredOrderCount = 0;
        for (Delivery d : deliveryDAO.getAllDeliveries()) {
            if ("Đã giao".equals(d.getStatus())) {
                deliveredOrderCount++;
            }
        }
        request.setAttribute("pendingDeliveries", deliveredOrderCount);

        List<Map<String, Object>> recentTransactions = requestDAO.getRecentTransactions(5);
        int totalTransactions = requestDAO.countAllRequests();
        request.setAttribute("recentTransactions", recentTransactions);
        request.setAttribute("totalTransactions", totalTransactions);

        request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
    }
} 