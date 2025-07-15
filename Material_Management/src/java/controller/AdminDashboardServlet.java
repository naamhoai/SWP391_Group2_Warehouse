package controller;

import dao.InventoryDAO;
import dao.ExportMaterialDAO;
import dao.RequestDAO;
import model.Inventory;
import model.ExportMaterial;
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
        // Tổng giá trị tồn kho
        InventoryDAO inventoryDAO = new InventoryDAO();
        List<Inventory> inventories = inventoryDAO.getInventoryWithMaterialInfo();
        long totalInventoryValue = 0;
        for (Inventory inv : inventories) {
            totalInventoryValue += inv.getQuantityOnHand() * inv.getPrice();
        }
        request.setAttribute("totalInventoryValue", totalInventoryValue);

        // Top 5 vật tư xuất nhiều nhất
        ExportMaterialDAO exportDAO = new ExportMaterialDAO();
        List<ExportMaterial> topExportedItems = exportDAO.getTopExportedMaterials(5);
        request.setAttribute("topExportedItems", topExportedItems);

        // Top 5 vật tư nhập nhiều nhất

        // Phân bổ yêu cầu: chỉ lấy số lượng xuất kho
        RequestDAO requestDAO = new RequestDAO();
        int outgoingCount = requestDAO.countAllRequests();
        Map<String, Integer> requestStats = new HashMap<>();
        requestStats.put("outgoingCount", outgoingCount);
        request.setAttribute("requestStats", requestStats);

        // Xu hướng tồn kho theo tháng
        Map<String, Integer> importByMonth = inventoryDAO.getTotalImportedByMonth();
        Map<String, Integer> exportByMonth = inventoryDAO.getTotalExportedByMonth();
        // Lấy tất cả các tháng xuất hiện trong nhập hoặc xuất
        Set<String> allMonths = new TreeSet<>();
        allMonths.addAll(importByMonth.keySet());
        allMonths.addAll(exportByMonth.keySet());
        List<String> labels = new ArrayList<>(allMonths);
        Collections.sort(labels); // Đảm bảo thứ tự thời gian
        List<Integer> values = new ArrayList<>();
        int tonDau = 0; // Nếu muốn, có thể lấy tồn đầu kỳ từ DB hoặc đặt 0
        int nhapLuyKe = 0;
        int xuatLuyKe = 0;
        for (String month : labels) {
            nhapLuyKe += importByMonth.getOrDefault(month, 0);
            xuatLuyKe += exportByMonth.getOrDefault(month, 0);
            int tonKho = tonDau + nhapLuyKe - xuatLuyKe;
            values.add(tonKho);
        }
        request.setAttribute("inventoryTrendLabels", labels);
        request.setAttribute("inventoryTrend", values);

        // Tổng vật tư trong kho (tổng số lượng vật tư còn tồn kho)
        int totalItems = 0;
        for (Inventory inv : inventories) {
            totalItems += inv.getQuantityOnHand();
        }
        request.setAttribute("totalItems", totalItems);

        // Các dữ liệu dashboard khác (nếu có)
        // ...

        request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
    }
} 