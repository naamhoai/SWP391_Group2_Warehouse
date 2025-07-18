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

        // Lấy dữ liệu nhập/xuất theo tháng cho biểu đồ tồn kho
        Map<String, Integer> importByMonth = inventoryDAO.getTotalImportedByMonth();
        Map<String, Integer> exportByMonth = inventoryDAO.getTotalExportedByMonth();
        Set<String> allMonths = new TreeSet<>();
        allMonths.addAll(importByMonth.keySet());
        allMonths.addAll(exportByMonth.keySet());
        // Nếu chưa có dữ liệu, thêm tháng hiện tại và tháng trước đó để có ít nhất 2 điểm
        if (allMonths.isEmpty()) {
            java.time.LocalDate now = java.time.LocalDate.now();
            String thisMonth = now.toString().substring(0,7);
            String lastMonth = now.minusMonths(1).toString().substring(0,7);
            allMonths.add(lastMonth);
            allMonths.add(thisMonth);
        }
        // Tạo dải tháng liên tục
        List<String> sortedMonths = new ArrayList<>(allMonths);
        Collections.sort(sortedMonths);
        String firstMonth = sortedMonths.get(0);
        String lastMonth = sortedMonths.get(sortedMonths.size()-1);
        List<String> fullMonthRange = new ArrayList<>();
        java.time.YearMonth start = java.time.YearMonth.parse(firstMonth);
        java.time.YearMonth end = java.time.YearMonth.parse(lastMonth);
        for (java.time.YearMonth m = start; !m.isAfter(end); m = m.plusMonths(1)) {
            fullMonthRange.add(m.toString());
        }
        List<Integer> values = new ArrayList<>();
        int tonDau = 0;
        int nhapLuyKe = 0;
        int xuatLuyKe = 0;
        for (String month : fullMonthRange) {
            nhapLuyKe += importByMonth.getOrDefault(month, 0);
            xuatLuyKe += exportByMonth.getOrDefault(month, 0);
            int tonKho = tonDau + nhapLuyKe - xuatLuyKe;
            values.add(tonKho);
        }
        request.setAttribute("inventoryTrendLabels", fullMonthRange);
        request.setAttribute("inventoryTrend", values);

        Map<String, Integer> importByMonth2 = inventoryDAO.getTotalImportedByMonth();
        Map<String, Integer> exportByMonth2 = inventoryDAO.getTotalExportedByMonth();
        Set<String> allMonths2 = new TreeSet<>();
        allMonths2.addAll(importByMonth2.keySet());
        allMonths2.addAll(exportByMonth2.keySet());
        List<String> monthLabels = new ArrayList<>(allMonths2);
        Collections.sort(monthLabels);
        List<Integer> importValues = new ArrayList<>();
        List<Integer> exportValues = new ArrayList<>();
        for (String month : monthLabels) {
            importValues.add(importByMonth2.getOrDefault(month, 0));
            exportValues.add(exportByMonth2.getOrDefault(month, 0));
        }
        request.setAttribute("importExportMonthLabels", monthLabels);
        request.setAttribute("importByMonth", importValues);
        request.setAttribute("exportByMonth", exportValues);

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