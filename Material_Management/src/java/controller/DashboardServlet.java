package controller;

import dal.DBContext;
import dao.DashboardDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            String timeFilter = request.getParameter("timeFilter");
            if (timeFilter == null) {
                timeFilter = "month"; // Default filter
            }

            DashboardDAO dashboardDAO = new DashboardDAO();
            
            // Get stats based on time filter
            Map<String, Integer> stats = dashboardDAO.getFilteredStats(timeFilter);
            
            // Get inventory statistics
            Map<String, Integer> inventoryStats = dashboardDAO.getInventoryStats();
            
            // Get low stock items details
            List<Map<String, Object>> lowStockItemsList = dashboardDAO.getLowStockItemsList();
            
            // Set attributes for JSP
            request.setAttribute("timeFilter", timeFilter);
            request.setAttribute("totalItems", stats.get("totalItems"));
            request.setAttribute("monthlyOrders", stats.get("monthlyOrders"));
            request.setAttribute("lowStockItems", stats.get("lowStockItems"));
            request.setAttribute("pendingDeliveries", stats.get("pendingDeliveries"));
            
            request.setAttribute("inventoryStats", inventoryStats);
            request.setAttribute("lowStockItemsList", lowStockItemsList);
            
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
} 