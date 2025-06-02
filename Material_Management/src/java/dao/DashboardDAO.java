package dao;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardDAO {
    
    public Map<String, Integer> getFilteredStats(String timeFilter) throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        
        String dateCondition;
        switch (timeFilter) {
            case "today":
                dateCondition = "DATE(export_date) = CURRENT_DATE";
                break;
            case "week":
                dateCondition = "YEARWEEK(export_date) = YEARWEEK(CURRENT_DATE)";
                break;
            case "year":
                dateCondition = "YEAR(export_date) = YEAR(CURRENT_DATE)";
                break;
            default: // month
                dateCondition = "MONTH(export_date) = MONTH(CURRENT_DATE)";
                break;
        }

        try (Connection conn = new DBContext().getConnection()) {
            // Get orders count based on time filter
            String sql = "SELECT COUNT(*) as count FROM export_forms WHERE " + dateCondition;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    stats.put("monthlyOrders", rs.getInt("count"));
                }
            }

            // Get total items
            sql = "SELECT SUM(quantity) as total FROM inventory";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    stats.put("totalItems", rs.getInt("total"));
                }
            }

            // Get low stock items count
            sql = "SELECT COUNT(*) as count FROM inventory WHERE quantity < 10";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    stats.put("lowStockItems", rs.getInt("count"));
                }
            }

            // Get pending deliveries count
            sql = "SELECT COUNT(*) as count FROM requests WHERE status = 'pending' " +
                 "AND request_type_id IN (SELECT request_type_id FROM request_types WHERE type_name LIKE '%delivery%')";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    stats.put("pendingDeliveries", rs.getInt("count"));
                }
            }
        }
        return stats;
    }

    public Map<String, Integer> getInventoryStats() throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT " +
                        "SUM(CASE WHEN quantity > 10 THEN 1 ELSE 0 END) as in_stock, " +
                        "SUM(CASE WHEN quantity <= 10 AND quantity > 0 THEN 1 ELSE 0 END) as low_stock, " +
                        "SUM(CASE WHEN quantity = 0 THEN 1 ELSE 0 END) as out_of_stock " +
                        "FROM inventory";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    stats.put("inStock", rs.getInt("in_stock"));
                    stats.put("lowStock", rs.getInt("low_stock"));
                    stats.put("outOfStock", rs.getInt("out_of_stock"));
                }
            }
        }
        return stats;
    }

    public List<Map<String, Object>> getLowStockItemsList() throws SQLException {
        List<Map<String, Object>> items = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT m.name, i.quantity, 10 as min_stock, " +
                        "CASE " +
                        "   WHEN i.quantity = 0 THEN 'Out of Stock' " +
                        "   WHEN i.quantity < 5 THEN 'Critical' " +
                        "   ELSE 'Low Stock' " +
                        "END as status " +
                        "FROM inventory i " +
                        "JOIN materials m ON i.material_id = m.material_id " +
                        "WHERE i.quantity < 10 " +
                        "ORDER BY i.quantity ASC " +
                        "LIMIT 5";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", rs.getString("name"));
                    item.put("quantity", rs.getInt("quantity"));
                    item.put("minStock", rs.getInt("min_stock"));
                    item.put("status", rs.getString("status"));
                    items.add(item);
                }
            }
        }
        return items;
    }
} 