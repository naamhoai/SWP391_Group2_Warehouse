package dao;

import dal.DBContext;
import model.Material;
import model.Category;
import model.Supplier;
import model.UnitConversion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class MaterialDAO extends DBContext {

    public List<Material> getMaterialsForAdmin(String searchQuery, Integer categoryId, String supplierFilter, String statusFilter,
            int page, int itemsPerPage, String sortField, String sortDir) {
        List<Material> materials = new ArrayList<>();
        String sql = "SELECT m.*, c.name as category_name, c.hidden as category_hidden, s.supplier_name, s.status as supplier_status, u.unit_name "
                + "FROM materials m "
                + "LEFT JOIN categories c ON m.category_id = c.category_id "
                + "LEFT JOIN supplier s ON m.supplier_id = s.supplier_id "
                + "LEFT JOIN units u ON m.unit_id = u.unit_id "
                + "WHERE 1=1 ";
        ArrayList<Object> params = new ArrayList<>();
        sql = buildWhereClauses(sql, params, searchQuery, categoryId, supplierFilter, statusFilter);
        sql += " ORDER BY m.material_id ASC";
        sql += " LIMIT ? OFFSET ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            for (Object param : params) {
                ps.setObject(paramIndex++, param);
            }
            ps.setInt(paramIndex++, itemsPerPage);
            ps.setInt(paramIndex, (page - 1) * itemsPerPage);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    materials.add(mapRowToMaterial(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return materials;
    }

    public int getTotalMaterialsForAdmin(String searchQuery, Integer categoryId, String supplierFilter, String statusFilter) {
        String sql = "SELECT COUNT(*) as total FROM materials m "
                + "LEFT JOIN categories c ON m.category_id = c.category_id "
                + "LEFT JOIN supplier s ON m.supplier_id = s.supplier_id "
                + "WHERE 1=1 ";
        ArrayList<Object> params = new ArrayList<>();
        sql = buildWhereClauses(sql, params, searchQuery, categoryId, supplierFilter, statusFilter);
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
public Material getMaterialById(int materialId) throws SQLException {
        String sql = "SELECT m.*, c.name as category_name, s.supplier_name, u.unit_name "
                + "FROM materials m "
                + "LEFT JOIN categories c ON m.category_id = c.category_id "
                + "LEFT JOIN supplier s ON m.supplier_id = s.supplier_id "
                + "LEFT JOIN units u ON m.unit_id = u.unit_id "
                + "WHERE m.material_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToMaterial(rs);
                }
            }
        }
        return null;
    }

    public boolean addMaterial(Material material) throws SQLException {
        String sql = "INSERT INTO materials (name, category_id, image_url, description, price, supplier_id, unit_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, material.getName());
            ps.setInt(2, material.getCategoryId());
            ps.setString(3, material.getImageUrl());
            ps.setString(4, material.getDescription());
            ps.setLong(5, material.getPrice());
            ps.setInt(6, material.getSupplierId());
            ps.setInt(7, material.getUnitId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateMaterial(Material material) throws SQLException {
        String sql = "UPDATE materials SET name = ?, category_id = ?, supplier_id = ?, price = ?, "
                + "unit_id = ?, description = ?, image_url = ?, status = ? "
                + "WHERE material_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, material.getName());
            ps.setInt(2, material.getCategoryId());
            ps.setInt(3, material.getSupplierId());
            ps.setLong(4, material.getPrice());
            ps.setInt(5, material.getUnitId());
            ps.setString(6, material.getDescription());
            ps.setString(7, material.getImageUrl());
            ps.setString(8, material.getStatus());
            ps.setInt(9, material.getMaterialId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteMaterial(int materialId) throws SQLException {
        return updateMaterialStatus(materialId, "inactive");
    }

    public boolean addMaterialWithId(Material material) throws SQLException {
        String sql = "INSERT INTO materials (material_id, name, category_id, image_url, description, supplier_id, unit_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, material.getMaterialId());
            ps.setString(2, material.getName());
            ps.setInt(3, material.getCategoryId());
            ps.setString(4, material.getImageUrl());
            ps.setString(5, material.getDescription());
            ps.setInt(6, material.getSupplierId());
            ps.setInt(7, material.getUnitId());
            ps.setString(8, material.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateMaterialStatus(int materialId, String status) throws SQLException {
        String sql = "UPDATE materials SET status = ? WHERE material_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, materialId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateMultipleMaterialsStatus(List<Integer> materialIds, String status) throws SQLException {
        if (materialIds == null || materialIds.isEmpty()) {
            return false;
        }
        String sql = "UPDATE materials SET status = ? WHERE material_id IN (";
        for (int i = 0; i < materialIds.size(); i++) {
            sql += (i == 0 ? "?" : ", ?");
        }
        sql += ")";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            for (int i = 0; i < materialIds.size(); i++) {
                ps.setInt(i + 2, materialIds.get(i));
            }
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật trạng thái vật tư dựa trên trạng thái supplier
     * @param supplierId ID của supplier
     * @param supplierStatus Trạng thái mới của supplier
     * @return true nếu cập nhật thành công
     */
    public boolean updateMaterialsStatusBySupplier(int supplierId, String supplierStatus) throws SQLException {
        String materialStatus;
        
        // Xác định trạng thái vật tư dựa trên trạng thái supplier
        switch (supplierStatus.toLowerCase()) {
            case "active":
                materialStatus = "active";
                break;
            case "inactive":
                materialStatus = "inactive";
                break;
            case "terminated":
                materialStatus = "inactive"; // Vật tư chuyển sang ngừng kinh doanh
                break;
            default:
                materialStatus = "inactive"; // Mặc định là ngừng kinh doanh
                break;
        }
        
        String sql = "UPDATE materials SET status = ? WHERE supplier_id = ?";
        try (Connection conn = new DBContext().getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, materialStatus);
            ps.setInt(2, supplierId);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Đã cập nhật " + rowsAffected + " vật tư cho supplier ID: " + supplierId + 
                             " từ trạng thái supplier: " + supplierStatus + " sang trạng thái vật tư: " + materialStatus);
            return rowsAffected >= 0; // Trả về true ngay cả khi không có vật tư nào được cập nhật
        }
    }

    private String buildWhereClauses(String sql, List<Object> params, String searchQuery, Integer categoryId, String supplierFilter, String statusFilter) {
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            sql += "AND m.name LIKE ? ";
            params.add("%" + searchQuery.trim() + "%");
        }
        if (categoryId != null) {
            sql += "AND c.category_id = ? ";
            params.add(categoryId);
        }
        if (supplierFilter != null && !supplierFilter.trim().isEmpty() && !supplierFilter.equals("All")) {
            sql += "AND s.supplier_name = ? ";
            params.add(supplierFilter.trim());
        }
        if (statusFilter != null && !statusFilter.trim().isEmpty() && !statusFilter.equals("All")) {
            if (statusFilter.equals("inactive")) {
                sql += "AND (s.status != 'active' OR c.hidden = 1) ";
            } else if (statusFilter.equals("active")) {
                sql += "AND s.status = 'active' AND (c.hidden = 0 OR c.hidden IS NULL) ";
            }
        }
        return sql;
    }

    private String getSafeSortField(String field) {
        switch (field.toLowerCase()) {
            case "material_id":
                return "m.material_id";
            case "name":
return "m.name";
            case "price":
                return "m.price";
            case "status":
                return "m.status";
            default:
                return "m.material_id";
        }
    }

    private Material mapRowToMaterial(ResultSet rs) throws SQLException {
        Material material = new Material();
        material.setMaterialId(rs.getInt("material_id"));
        material.setName(rs.getString("name"));
        material.setCategoryId(rs.getInt("category_id"));
        material.setSupplierId(rs.getInt("supplier_id"));
        material.setUnitId(rs.getInt("unit_id"));
        material.setCategoryName(rs.getString("category_name"));
        material.setSupplierName(rs.getString("supplier_name"));
        material.setImageUrl(rs.getString("image_url"));
        material.setPrice(rs.getLong("price"));
        material.setDescription(rs.getString("description"));
        material.setUnitName(rs.getString("unit_name"));
        material.setStatus(rs.getString("status"));
        material.setSupplierStatus(rs.getString("supplier_status"));
        try {
            material.setCategoryHidden(rs.getBoolean("category_hidden"));
        } catch (SQLException e) {
            material.setCategoryHidden(false);
        }
        return material;
    }

    public List<Material> getMaterial() {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT m.material_id, m.name, m.category_id, c.parent_id, u.unit_name "
                + "FROM materials m "
                + "LEFT JOIN categories c ON m.category_id = c.category_id "
                + "LEFT JOIN units u ON m.unit_id = u.unit_id "
                + "WHERE (c.hidden = 0 OR c.hidden IS NULL) "
                + "ORDER BY m.name";
        try {
            PreparedStatement ca = new DBContext().connection.prepareStatement(sql);
            ResultSet st = ca.executeQuery();
            while (st.next()) {
                Material mate = new Material();
                mate.setName(st.getString("name"));
                mate.setMaterialId(st.getInt("material_id"));
                mate.setCategoryId(st.getInt("category_id"));
                mate.setName(st.getString("unit_name"));
                list.add(mate);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<String> getAllMaterialNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT name FROM materials";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    public int getMaterialIdByName(String materialName) {
        String sql = "SELECT material_id FROM materials WHERE name = ?";
try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, materialName.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("material_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Tìm kiếm vật tư theo tên (autocomplete)
     */
    public List<Material> searchMaterialsByName(String keyword) {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT m.material_id, m.name, m.price, m.unit_id, u.unit_name, m.category_id, c.name as category_name "
                + "FROM materials m "
                + "LEFT JOIN units u ON m.unit_id = u.unit_id "
                + "LEFT JOIN categories c ON m.category_id = c.category_id "
                + "WHERE m.status = 'active' AND m.name LIKE ? LIMIT 10";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("material_id"));
                m.setName(rs.getString("name"));
                m.setPrice(rs.getLong("price"));
                m.setUnitId(rs.getInt("unit_id"));
                m.setUnitName(rs.getString("unit_name"));
                m.setCategoryId(rs.getInt("category_id"));
                m.setCategoryName(rs.getString("category_name"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public int getNextMaterialId() {
        String sql = "SELECT COALESCE(MAX(material_id), 0) + 1 AS next_id FROM materials";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("next_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void close() {
        super.closeConnection();
    }

}