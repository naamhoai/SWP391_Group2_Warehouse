package controller;

import dao.MaterialSupplierDAO;
import dao.InventoryDAO;
import dao.SupplierDAO;
import model.MaterialSupplier;
import model.MaterialSupplierInventory;
import model.Supplier;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "MaterialSupplierServlet", urlPatterns = {"/material-suppliers"})
public class MaterialSupplierServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get filter parameters
        String supplierIdStr = request.getParameter("supplier_id");
        String keyword = request.getParameter("keyword");
        
        // Parse supplier ID
        int supplierId = 0;
        try {
            if (supplierIdStr != null && !supplierIdStr.trim().isEmpty()) {
            supplierId = Integer.parseInt(supplierIdStr);
            }
        } catch (Exception e) {
            // Invalid supplier ID, keep as 0
        }
        
        // Parse pagination parameters
        int pageSize = 10;
        try { 
            String pageSizeStr = request.getParameter("pageSize");
            if (pageSizeStr != null && !pageSizeStr.trim().isEmpty()) {
                pageSize = Integer.parseInt(pageSizeStr);
            }
        } catch(Exception e) {}
        
        int currentPage = 1;
        try { 
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.trim().isEmpty()) {
                currentPage = Integer.parseInt(pageStr);
            }
        } catch(Exception e) {}

        MaterialSupplierDAO materialSupplierDAO = new MaterialSupplierDAO();
        SupplierDAO supplierDAO = new SupplierDAO();
        
        List<MaterialSupplierInventory> list = new ArrayList<>();
        if (supplierId > 0) {
            try (java.sql.Connection conn = new dal.DBContext().getConnection()) {
                InventoryDAO inventoryDAO = new InventoryDAO(conn);
                list = inventoryDAO.getMaterialsBySupplierWithQuantity(supplierId);
            } catch (java.sql.SQLException e) {
                throw new ServletException("Lỗi truy vấn vật tư theo supplier từ inventory", e);
            }
        }
        // Filter by keyword
        List<MaterialSupplierInventory> filtered = new ArrayList<>();
        if (list != null) {
            for (MaterialSupplierInventory ms : list) {
                boolean matchesKeyword = true;
                if (keyword != null && !keyword.trim().isEmpty()) {
                    if (ms.getMaterialName() == null || !ms.getMaterialName().toLowerCase().contains(keyword.toLowerCase())) {
                        matchesKeyword = false;
                    }
                }
                if (matchesKeyword) {
                    filtered.add(ms);
                }
            }
        }
        int total = filtered != null ? filtered.size() : 0;
        int totalPages = (int)Math.ceil((double)total / pageSize);
        if (totalPages == 0) totalPages = 1;
        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;
        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        List<MaterialSupplierInventory> pageList = new ArrayList<>();
        if (filtered != null && !filtered.isEmpty() && start < end) {
            pageList = filtered.subList(start, end);
        }
        
        // Get active suppliers for dropdown
        List<Supplier> activeSuppliers = supplierDAO.getActiveSuppliers();
        
        // Set attributes
        request.setAttribute("materialSupplierList", pageList);
        request.setAttribute("activeSuppliers", activeSuppliers);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("total", total);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("keyword", keyword);
        request.setAttribute("supplier_id", supplierIdStr);
        
        request.getRequestDispatcher("materialSupplierList.jsp").forward(request, response);
    }
} 