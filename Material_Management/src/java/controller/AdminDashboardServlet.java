package controller;

import dao.InventoryDAO;
import dao.ExportMaterialDAO;
import dao.RequestDAO;
import dao.DeliveryDAO;
import dao.PurchaseOrderDAO;
import dao.PurchaseOrderDetailDAO;
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
import com.google.gson.Gson;
import model.PurchaseOrder;
import java.util.stream.Collectors;
import java.util.Comparator;
import dao.MaterialDAO;
import dao.SupplierDAO;
import dao.UserDAO;
import dao.UnitConversionDao;

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
        // Lấy tổng số vật tư từ MaterialDAO (chuẩn như MaterialListServlet)
        MaterialDAO materialDAO = new MaterialDAO();
        int totalMaterialTypes = materialDAO.getTotalMaterialsForAdmin(null, null, null, null);
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

        // Lấy số lượng đơn đã duyệt
        int approvedRequestCount = requestDAO.countFilteredRequests(null, "Đã duyệt", null, null, null);
        request.setAttribute("approvedRequestCount", approvedRequestCount);

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
        
        // Lấy dữ liệu nhập/xuất theo khoảng thời gian (đã lọc đúng điều kiện ở DAO)
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
        
        // Lấy số lượng đơn đã duyệt theo tháng
        Map<String, Integer> approvedRequestByMonth = requestDAO.getApprovedRequestCountByMonth(startDate, endDate);
        List<Integer> approvedRequestValues = new ArrayList<>();
        for (String month : monthLabels) {
            approvedRequestValues.add(approvedRequestByMonth.getOrDefault(month, 0));
        }
        request.setAttribute("approvedRequestByMonth", approvedRequestValues);
        request.setAttribute("approvedRequestByMonthJson", gson.toJson(approvedRequestValues));
        
        // Lấy số lượng đơn mua đã duyệt theo tháng
        PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
        Map<String, Integer> approvedPurchaseOrderByMonth = purchaseOrderDAO.getApprovedPurchaseOrderCountByMonth(startDate, endDate);
        List<Integer> approvedPurchaseOrderValues = new ArrayList<>();
        for (String month : monthLabels) {
            approvedPurchaseOrderValues.add(approvedPurchaseOrderByMonth.getOrDefault(month, 0));
        }
        request.setAttribute("approvedPurchaseOrderByMonth", approvedPurchaseOrderValues);
        request.setAttribute("approvedPurchaseOrderByMonthJson", gson.toJson(approvedPurchaseOrderValues));
        
        // Lấy danh sách 5 đơn hàng mua đã hoàn thành (status = 'Approved') từ PurchaseOrderDAO và truyền sang JSP với tên recentPurchases.
        List<PurchaseOrder> recentPurchases = new ArrayList<>();
        try {
            recentPurchases = purchaseOrderDAO.getPurchaseOrdersByStatus("Completed");
            recentPurchases = recentPurchases.stream().sorted(Comparator.comparing(PurchaseOrder::getOrderDate).reversed()).limit(5).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        PurchaseOrderDetailDAO detailDAO = new PurchaseOrderDetailDAO();
        for (PurchaseOrder order : recentPurchases) {
            order.setDetails(detailDAO.getDetailsByOrderId(order.getPurchaseOrderId()));
        }
        request.setAttribute("recentPurchases", recentPurchases);
        
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

        // Lấy tổng số nhà cung cấp và trạng thái hợp tác
        SupplierDAO supplierDAO = new SupplierDAO();
        int totalSuppliers = supplierDAO.countSuppliers(null, null);
        int activeSuppliers = supplierDAO.countSuppliers(null, "active"); // Đang hợp tác
        int terminatedSuppliers = supplierDAO.countSuppliers(null, "terminated"); // Ngừng hợp tác
        int inactiveSuppliers = supplierDAO.countSuppliers(null, "inactive"); // Chưa hợp tác
        request.setAttribute("totalSuppliers", totalSuppliers);
        request.setAttribute("activeSuppliers", activeSuppliers);
        request.setAttribute("terminatedSuppliers", terminatedSuppliers);
        request.setAttribute("inactiveSuppliers", inactiveSuppliers);

        // Lấy tổng số tài khoản người dùng và trạng thái hoạt động
        UserDAO userDAO = new UserDAO();
        int totalUsers = userDAO.countAllUsers();
        int activeUsers = userDAO.countActiveUsers();
        int inactiveUsers = userDAO.countInactiveUsers();
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("activeUsers", activeUsers);
        request.setAttribute("inactiveUsers", inactiveUsers);

        // Lấy tổng số đơn vị tính
        UnitConversionDao unitConversionDao = new UnitConversionDao();
        int totalUnits = unitConversionDao.countAllUnits();
        request.setAttribute("totalUnits", totalUnits);

        request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
    }
} 