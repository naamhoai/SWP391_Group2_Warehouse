package dao;

import dal.DBContext;
import model.Inventory;
import model.Material;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class InventoryDAO extends DBContext {
    private Connection conn;
    public InventoryDAO(Connection conn) {
        this.conn = conn;
    }
    public InventoryDAO(){
    }
    public List<Inventory> getInventoryList(Integer categoryId, Integer supplierId, String search, String condition, int page, int pageSize) throws SQLException {
        List<Inventory> list = new ArrayList<>();
        String sql = "SELECT i.inventory_id, i.material_id, i.material_condition, i.quantity_on_hand, i.price, i.last_updated, " +
                "m.name AS material_name, c.name AS category_name, s.supplier_name, u.unit_name " +
                "FROM inventory i " +
                "JOIN materials m ON i.material_id = m.material_id " +
                "JOIN categories c ON m.category_id = c.category_id " +
                "JOIN supplier s ON m.supplier_id = s.supplier_id " +
                "JOIN units u ON m.unit_id = u.unit_id WHERE 1=1 ";
        List<Object> params = new ArrayList<>();
        if (categoryId != null && categoryId > 0) {
            sql += " AND c.category_id = ?";
            params.add(categoryId);
        }
        if (supplierId != null && supplierId > 0) {
            sql += " AND s.supplier_id = ?";
            params.add(supplierId);
        }
        if (search != null && !search.trim().isEmpty()) {
            sql += " AND LOWER(m.name) LIKE ?";
            params.add("%" + search.trim().toLowerCase() + "%");
        }
        if (condition != null && !condition.isEmpty()) {
            sql += " AND i.material_condition = ?";
            params.add(condition);
        }
        sql += " ORDER BY i.material_id ASC LIMIT ? OFFSET ?";
        params.add(pageSize);
        params.add((page - 1) * pageSize);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inventory inv = new Inventory();
                    inv.setInventoryId(rs.getInt("inventory_id"));
                    inv.setMaterialId(rs.getInt("material_id"));
                    inv.setMaterialCondition(rs.getString("material_condition"));
                    inv.setQuantityOnHand(rs.getInt("quantity_on_hand"));
                    inv.setLastUpdated(rs.getTimestamp("last_updated"));
                    inv.setMaterialName(rs.getString("material_name"));
                    inv.setCategoryName(rs.getString("category_name"));
                    inv.setSupplierName(rs.getString("supplier_name"));
                    inv.setUnitName(rs.getString("unit_name"));
                    inv.setPrice(rs.getInt("price")); // Lấy giá từ inventory
                    list.add(inv);
                }
            }
        }
        return list;
    }

    public int countInventory(Integer categoryId, Integer supplierId, String search, String condition) throws SQLException {
        String sql = "SELECT COUNT(*) FROM inventory i " +
                "JOIN materials m ON i.material_id = m.material_id " +
                "JOIN categories c ON m.category_id = c.category_id " +
                "JOIN supplier s ON m.supplier_id = s.supplier_id WHERE 1=1 ";
        List<Object> params = new ArrayList<>();
        if (categoryId != null && categoryId > 0) {
            sql += " AND c.category_id = ?";
            params.add(categoryId);
        }
        if (supplierId != null && supplierId > 0) {
            sql += " AND s.supplier_id = ?";
            params.add(supplierId);
        }
        if (search != null && !search.trim().isEmpty()) {
            sql += " AND LOWER(m.name) LIKE ?";
            params.add("%" + search.trim().toLowerCase() + "%");
        }
        if (condition != null && !condition.isEmpty()) {
            sql += " AND i.material_condition = ?";
            params.add(condition);
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    public List<Inventory> getInventoryWithMaterialInfo() {
        List<Inventory> inventories = new ArrayList<>();
        String sql = "SELECT i.inventory_id, i.material_id, i.material_condition, i.quantity_on_hand, i.price, i.last_updated, "
                + "m.name AS material_name, m.unit_id, c.name AS category_name, s.supplier_name, u.unit_name, m.status "
                + "FROM inventory i "
                + "LEFT JOIN materials m ON i.material_id = m.material_id "
                + "LEFT JOIN categories c ON m.category_id = c.category_id "
                + "LEFT JOIN supplier s ON m.supplier_id = s.supplier_id "
                + "LEFT JOIN units u ON m.unit_id = u.unit_id "
                + "ORDER BY m.name, i.material_condition";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Inventory inv = new Inventory();
                inv.setInventoryId(rs.getInt("inventory_id"));
                inv.setMaterialId(rs.getInt("material_id"));
                inv.setMaterialCondition(rs.getString("material_condition"));
                inv.setQuantityOnHand(rs.getInt("quantity_on_hand"));
                inv.setLastUpdated(rs.getTimestamp("last_updated"));
                inv.setMaterialName(rs.getString("material_name"));
                inv.setCategoryName(rs.getString("category_name"));
                inv.setSupplierName(rs.getString("supplier_name"));
                inv.setUnitId(rs.getInt("unit_id"));
                inv.setUnitName(rs.getString("unit_name"));
                inv.setPrice(rs.getInt("price")); // Lấy giá từ inventory
                inv.setStatus(rs.getString("status"));
                inventories.add(inv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventories;
    }

    public List<Inventory> getInventoryByMaterialId(int materialId) {
        List<Inventory> inventories = new ArrayList<>();
        String sql = "SELECT i.*, m.name AS material_name, m.unit_id, u.unit_name "
                + "FROM inventory i "
                + "LEFT JOIN materials m ON i.material_id = m.material_id "
                + "LEFT JOIN units u ON m.unit_id = u.unit_id "
                + "WHERE i.material_id = ? ORDER BY i.material_condition";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Inventory inventory = new Inventory();
                inventory.setInventoryId(rs.getInt("inventory_id"));
                inventory.setMaterialId(rs.getInt("material_id"));
                inventory.setMaterialCondition(rs.getString("material_condition"));
                inventory.setQuantityOnHand(rs.getInt("quantity_on_hand"));
                inventory.setLastUpdated(rs.getTimestamp("last_updated"));
                inventory.setMaterialName(rs.getString("material_name"));
                inventory.setUnitId(rs.getInt("unit_id"));
                inventory.setUnitName(rs.getString("unit_name"));
                inventories.add(inventory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventories;
    }

    public int getTotalInventoryByMaterialId(int materialId) {
        String sql = "SELECT COALESCE(SUM(quantity_on_hand), 0) as total FROM inventory WHERE material_id = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalInventoryByMaterialIdAndCondition(int materialId, String materialCondition) {
        String sql = "SELECT COALESCE(SUM(quantity_on_hand), 0) as total FROM inventory WHERE material_id = ? AND material_condition = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            ps.setString(2, materialCondition);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void subtractInventory(int materialId, String materialCondition, int quantity) {
        String sql = "UPDATE inventory SET quantity_on_hand = quantity_on_hand - ? WHERE material_id = ? AND material_condition = ? AND quantity_on_hand >= ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, materialId);
            ps.setString(3, materialCondition);
            ps.setInt(4, quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy tổng nhập theo tháng
    public Map<String, Integer> getTotalImportedByMonth() {
        return getTotalImportedByMonthRange(null, null);
    }

    // Lấy tổng nhập theo khoảng thời gian
    public Map<String, Integer> getTotalImportedByMonthRange(String startDate, String endDate) {
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(po.order_date, '%Y-%m') AS month, SUM(pod.quantity) AS total_imported " +
                "FROM purchase_orders po " +
                "JOIN purchase_order_details pod ON po.purchase_order_id = pod.purchase_order_id " +
                "WHERE 1=1 ";
        
        List<Object> params = new ArrayList<>();
        if (startDate != null && !startDate.isEmpty()) {
            sql += "AND po.order_date >= ? ";
            params.add(startDate + "-01");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql += "AND po.order_date <= ? ";
            params.add(endDate + "-31");
        }
        
        sql += "GROUP BY month ORDER BY month";
        
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("month"), rs.getInt("total_imported"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Lấy tổng xuất theo tháng
    public Map<String, Integer> getTotalExportedByMonth() {
        return getTotalExportedByMonthRange(null, null);
    }

    // Lấy tổng xuất theo khoảng thời gian
    public Map<String, Integer> getTotalExportedByMonthRange(String startDate, String endDate) {
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(ef.export_date, '%Y-%m') AS month, SUM(em.quantity) AS total_exported " +
                "FROM export_forms ef " +
                "JOIN export_materials em ON ef.export_id = em.export_id " +
                "WHERE 1=1 ";
        
        List<Object> params = new ArrayList<>();
        if (startDate != null && !startDate.isEmpty()) {
            sql += "AND ef.export_date >= ? ";
            params.add(startDate + "-01");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql += "AND ef.export_date <= ? ";
            params.add(endDate + "-31");
        }
        
        sql += "GROUP BY month ORDER BY month";
        
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("month"), rs.getInt("total_exported"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Inventory> getLowStockItems() {
        List<Inventory> lowStockItems = new ArrayList<>();
        String sql = "SELECT i.inventory_id, i.material_id, i.material_condition, i.quantity_on_hand, i.price, i.last_updated, "
                + "m.name AS material_name, c.name AS category_name, s.supplier_name, u.unit_name, m.status "
                + "FROM inventory i "
                + "LEFT JOIN materials m ON i.material_id = m.material_id "
                + "LEFT JOIN categories c ON m.category_id = c.category_id "
                + "LEFT JOIN supplier s ON m.supplier_id = s.supplier_id "
                + "LEFT JOIN units u ON m.unit_id = u.unit_id "
                + "WHERE i.quantity_on_hand <= 10 "
                + "ORDER BY i.quantity_on_hand ASC, m.name, i.material_condition";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Inventory inv = new Inventory();
                inv.setInventoryId(rs.getInt("inventory_id"));
                inv.setMaterialId(rs.getInt("material_id"));
                inv.setMaterialCondition(rs.getString("material_condition"));
                inv.setQuantityOnHand(rs.getInt("quantity_on_hand"));
                inv.setLastUpdated(rs.getTimestamp("last_updated"));
                inv.setMaterialName(rs.getString("material_name"));
                inv.setCategoryName(rs.getString("category_name"));
                inv.setSupplierName(rs.getString("supplier_name"));
                inv.setUnitId(rs.getInt("unit_id"));
                inv.setUnitName(rs.getString("unit_name"));
                inv.setPrice(rs.getInt("price")); // Lấy giá từ inventory
                inv.setStatus(rs.getString("status"));
                lowStockItems.add(inv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lowStockItems;
    }

    public int addOrUpdateInventoryWithResult(int materialId, String materialName, int quantity, String materialCondition, double unitPrice) {
        String selectSql = "SELECT inventory_id, quantity_on_hand FROM inventory WHERE material_id = ? AND material_condition = ?";
        String insertSql = "INSERT INTO inventory (material_id, material_condition, quantity_on_hand, last_updated, price) VALUES (?, ?, ?, NOW(), ?)";
        String updateSql = "UPDATE inventory SET quantity_on_hand = quantity_on_hand + ?, last_updated = NOW(), price = ? WHERE inventory_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement selectPs = conn.prepareStatement(selectSql)) {
            selectPs.setInt(1, materialId);
            selectPs.setString(2, materialCondition);
            ResultSet rs = selectPs.executeQuery();
            if (rs.next()) {
                int inventoryId = rs.getInt("inventory_id");
                try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                    updatePs.setInt(1, quantity);
                    updatePs.setDouble(2, unitPrice);
                    updatePs.setInt(3, inventoryId);
                    int result = updatePs.executeUpdate();
                    // LOG cập nhật
                    System.out.println("[INVENTORY UPDATE] Cộng dồn vật tư ID: " + materialId + ", Số lượng: +" + quantity + ", Giá: " + unitPrice + ", Tình trạng: " + materialCondition);
                    return result;
                }
            } else {
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    insertPs.setInt(1, materialId);
                    insertPs.setString(2, materialCondition);
                    insertPs.setInt(3, quantity);
                    insertPs.setDouble(4, unitPrice);
                    int result = insertPs.executeUpdate();
                    // LOG thêm mới
                    System.out.println("[INVENTORY INSERT] Thêm mới vật tư ID: " + materialId + ", Số lượng: " + quantity + ", Giá: " + unitPrice + ", Tình trạng: " + materialCondition);
                    return result;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
