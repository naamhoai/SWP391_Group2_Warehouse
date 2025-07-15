package controller;

import dao.InventoryDAO;
import dao.CategoryDAO;
import dao.SupplierDAO;
import model.Inventory;
import model.Category;
import model.Supplier;
import dal.DBContext;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/InventoryServlet")
public class InventoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("true".equals(request.getParameter("reset"))) {
            response.sendRedirect("inventory");
            return;
        }
        int page = 1;
        int pageSize = 10;
        Integer categoryId = null;
        Integer supplierId = null;
        String search = null;
        String condition = null;
        try {
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            if (request.getParameter("pageSize") != null) {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
            }
            if (request.getParameter("categoryId") != null && !request.getParameter("categoryId").isEmpty()) {
                categoryId = Integer.parseInt(request.getParameter("categoryId"));
            }
            if (request.getParameter("supplierId") != null && !request.getParameter("supplierId").isEmpty()) {
                supplierId = Integer.parseInt(request.getParameter("supplierId"));
            }
            if (request.getParameter("search") != null && !request.getParameter("search").isEmpty()) {
                search = request.getParameter("search");
            }
            if (request.getParameter("condition") != null && !request.getParameter("condition").isEmpty()) {
                condition = request.getParameter("condition");
            }
        } catch (Exception e) {
            
        }
        try (Connection conn = new DBContext().getConnection()) {
        InventoryDAO inventoryDAO = new InventoryDAO(conn);
        CategoryDAO categoryDAO = new CategoryDAO();
        SupplierDAO supplierDAO = new SupplierDAO();

            List<Inventory> inventoryList = inventoryDAO.getInventoryList(categoryId, supplierId, search, condition, page, pageSize);
            int totalRecords = inventoryDAO.countInventory(categoryId, supplierId, search, condition);
            int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
            List<Category> categoryList = categoryDAO.getAllCategories();
            List<Supplier> supplierList = supplierDAO.getAllSuppliers();

            request.setAttribute("inventoryList", inventoryList);
            request.setAttribute("categoryList", categoryList);
            request.setAttribute("supplierList", supplierList);
            request.setAttribute("page", page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalRecords", totalRecords);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("categoryId", categoryId);
            request.setAttribute("supplierId", supplierId);
            request.setAttribute("search", search);
            request.setAttribute("condition", condition);

            request.getRequestDispatcher("inventory.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Lỗi lấy dữ liệu tồn kho: " + e.getMessage());
        }
    }
}
