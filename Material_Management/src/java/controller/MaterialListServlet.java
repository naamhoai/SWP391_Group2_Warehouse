package controller;

import dao.MaterialDAO;
import dao.MaterialInfoDAO;
import dao.InventoryDAO;
import java.util.List;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import model.*;
import java.util.ArrayList;
import dao.CategoryDAO;
import java.util.Map;
import java.util.HashMap;

@WebServlet(name = "MaterialListServlet", urlPatterns = {"/MaterialListServlet"})
public class MaterialListServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet MaterialListServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MaterialListServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("true".equals(request.getParameter("reset"))) {
            response.sendRedirect("MaterialListServlet");
            return;
        }

        String searchQuery = request.getParameter("search");
        Integer categoryId = null;
        if (request.getParameter("category") != null && !request.getParameter("category").isEmpty()) {
            try {
                categoryId = Integer.parseInt(request.getParameter("category"));
            } catch (NumberFormatException e) {
                categoryId = null;
            }
        }
        String categoryFilter = "All";
        if (categoryId != null) {
            categoryFilter = String.valueOf(categoryId);
        }
        String supplierFilter = request.getParameter("supplier") == null ? "All" : request.getParameter("supplier");
        String statusFilter = request.getParameter("status") == null ? "All" : request.getParameter("status");
        String sortField = request.getParameter("sort") == null ? "material_id" : request.getParameter("sort");
        String sortDir = request.getParameter("dir") == null ? "asc" : request.getParameter("dir");

        int currentPage = 1;
        int itemsPerPage = 10;
        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                currentPage = Integer.parseInt(pageStr);
            }
            String itemsPerPageStr = request.getParameter("itemsPerPage");
            if (itemsPerPageStr != null && !itemsPerPageStr.isEmpty()) {
                itemsPerPage = Integer.parseInt(itemsPerPageStr);
            }
        } catch (NumberFormatException e) {

        }

        MaterialDAO dao = new MaterialDAO();
        int totalItems = dao.getTotalMaterialsForAdmin(searchQuery, categoryId, supplierFilter, statusFilter);
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        }
        if (currentPage < 1) {
            currentPage = 1;
        }

        List<Material> materials = dao.getMaterialsForAdmin(searchQuery, categoryId, supplierFilter, statusFilter, currentPage, itemsPerPage, sortField, sortDir);

        boolean showLowStock = "true".equals(request.getParameter("lowStock"));
        int lowStockItems = 0;
        if (showLowStock) {
            // Sử dụng InventoryDAO để lấy danh sách vật tư có tồn kho thấp
            InventoryDAO inventoryDAO = new InventoryDAO();
            List<Inventory> lowStockInventories = inventoryDAO.getLowStockItems();
            
            // Chuyển đổi từ Inventory sang Material để hiển thị
            List<Material> lowStockList = new ArrayList<>();
            for (Inventory inv : lowStockInventories) {
                // Tạo Material object từ dữ liệu Inventory
                Material material = new Material();
                material.setMaterialId(inv.getMaterialId());
                material.setName(inv.getMaterialName());
                material.setCategoryName(inv.getCategoryName());
                material.setSupplierName(inv.getSupplierName());
                material.setUnitName(inv.getUnitName());
                material.setPrice(inv.getPrice());
                material.setStatus(inv.getStatus());
                material.setUnitId(inv.getUnitId());
                
                lowStockList.add(material);
            }
            materials = lowStockList;
            lowStockItems = lowStockList.size();
        }
        request.setAttribute("lowStockItems", lowStockItems);
        request.setAttribute("showLowStock", showLowStock);

        CategoryDAO categoryDAO = new CategoryDAO();
        MaterialInfoDAO infoDAO = new MaterialInfoDAO();
        List<Category> categories = categoryDAO.getAllCategories();
        List<String> suppliers = infoDAO.getAllSuppliers();
        List<Integer> itemsPerPageOptions = Arrays.asList(5, 10, 20, 50);

        request.setAttribute("materials", materials);
        request.setAttribute("categories", categories);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("itemsPerPageOptions", itemsPerPageOptions);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("itemsPerPage", itemsPerPage);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("categoryFilter", categoryFilter);
        request.setAttribute("supplierFilter", supplierFilter);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("sortField", sortField);
        request.setAttribute("sortDir", sortDir);
        request.setAttribute("categoryId", categoryId);

        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);

        Map<String, Integer> typeCountByUnit = new HashMap<>();
        for (Material m : materials) {
            String unit = m.getUnitName();
            if (unit != null) {
                typeCountByUnit.put(unit, typeCountByUnit.getOrDefault(unit, 0) + 1);
            }
        }
        request.setAttribute("typeCountByUnit", typeCountByUnit);

        request.getRequestDispatcher("materialDetailList.jsp").forward(request, response);
        categoryDAO.closeConnection();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("toggleStatus".equals(action)) {
            String[] idArr = request.getParameterValues("materialIds");
            if (idArr != null && idArr.length > 0) {
                try {
                    MaterialDAO dao = new MaterialDAO();
                    List<Integer> ids = new ArrayList<>();
                    String newStatus = null;
                    for (String idStr : idArr) {
                        int materialId = Integer.parseInt(idStr);
                        String currentStatus = dao.getMaterialById(materialId).getStatus();
                        // Lấy trạng thái của vật tư đầu tiên để xác định trạng thái chuyển đổi
                        if (newStatus == null) {
                            newStatus = "active".equalsIgnoreCase(currentStatus) ? "inactive" : "active";
                        }
                        ids.add(materialId);
                    }
                    dao.updateMultipleMaterialsStatus(ids, newStatus);
                    response.sendRedirect("MaterialListServlet?actionStatus=deleteSuccess");
                } catch (Exception e) {
                    response.sendRedirect("MaterialListServlet?actionStatus=deleteError");
                }
            } else {
                response.sendRedirect("MaterialListServlet");
            }
            return;
        }
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
