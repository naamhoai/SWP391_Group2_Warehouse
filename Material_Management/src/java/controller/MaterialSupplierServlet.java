package controller;

import dao.MaterialSupplierDAO;
import dao.SupplierDAO;
import model.MaterialSupplier;
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
        String status = request.getParameter("status");
        
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
        
        // Get all materials by supplier (if supplier is selected)
        List<MaterialSupplier> list = new ArrayList<>();
        if (supplierId > 0) {
            list = materialSupplierDAO.getMaterialsBySupplierId(supplierId);
        } else {
            // If no supplier selected, get all materials
            list = materialSupplierDAO.getAllMaterialSuppliers();
        }
        
        // Apply filters
        List<MaterialSupplier> filtered = new ArrayList<>();
        if (list != null) {
            for (MaterialSupplier ms : list) {
                boolean matchesKeyword = true;
                boolean matchesStatus = true;
                
                // Filter by keyword
            if (keyword != null && !keyword.trim().isEmpty()) {
                    if (ms.getMaterialName() == null || 
                        !ms.getMaterialName().toLowerCase().contains(keyword.toLowerCase())) {
                        matchesKeyword = false;
                    }
                }
                
                // Filter by status
                if (status != null && !status.trim().isEmpty()) {
                    if ("available".equals(status)) {
                        // For now, assume all materials from active suppliers are available
                        // You can add actual material status logic here
                        matchesStatus = true;
            } else {
                        matchesStatus = true;
                    }
                }
                
                if (matchesKeyword && matchesStatus) {
                    filtered.add(ms);
            }
        }
        }
        
        // Calculate pagination
        int total = filtered != null ? filtered.size() : 0;
        int totalPages = (int)Math.ceil((double)total / pageSize);
        if (totalPages == 0) totalPages = 1;
        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;
        
        // Get page data
        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        List<MaterialSupplier> pageList = new ArrayList<>();
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
        request.setAttribute("status", status);
        
        request.getRequestDispatcher("materialSupplierList.jsp").forward(request, response);
    }
} 