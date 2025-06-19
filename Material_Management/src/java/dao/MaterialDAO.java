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

public class MaterialDAO {
    private DBContext dbContext = new DBContext();

    /**
     * Lấy danh sách vật tư cho trang quản trị (không có tồn kho).
     */
    public List<Material> getMaterialsForAdmin(String searchQuery, String categoryFilter, String supplierFilter,
            int page, int itemsPerPage, String sortField, String sortDir) {
        List<Material> materials = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT m.*, c.name as category_name, s.supplier_name, uc.base_unit ")
                .append("FROM materials m ")
                .append("LEFT JOIN categories c ON m.category_id = c.category_id ")
                .append("LEFT JOIN supplier s ON m.supplier_id = s.supplier_id ")
                .append("LEFT JOIN unit_conversion uc ON m.conversion_id = uc.conversion_id ")
                .append("WHERE 1=1 ");
        ArrayList<Object> params = new ArrayList<>();
        buildWhereClauses(sql, params, searchQuery, categoryFilter, supplierFilter);
        sql.append("ORDER BY ").append(getSafeSortField(sortField)).append(sortDir.equalsIgnoreCase("asc") ? " ASC" : " DESC");
        sql.append(" LIMIT ? OFFSET ?");
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
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

    /**
     * Đếm tổng số vật tư cho trang quản trị.
     */
    public int getTotalMaterialsForAdmin(String searchQuery, String categoryFilter, String supplierFilter) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) as total FROM materials m ")
                .append("LEFT JOIN categories c ON m.category_id = c.category_id ")
                .append("LEFT JOIN supplier s ON m.supplier_id = s.supplier_id ")
                .append("WHERE 1=1 ");
        ArrayList<Object> params = new ArrayList<>();
        buildWhereClauses(sql, params, searchQuery, categoryFilter, supplierFilter);
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
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

    /**
     * Lấy thông tin chi tiết của một vật tư bằng ID.
     */
    public Material getMaterialById(int materialId) throws SQLException {
        String sql = "SELECT m.*, c.name as category_name, s.supplier_name, uc.base_unit "
                + "FROM materials m "
                + "LEFT JOIN categories c ON m.category_id = c.category_id "
                + "LEFT JOIN supplier s ON m.supplier_id = s.supplier_id "
                + "LEFT JOIN unit_conversion uc ON m.conversion_id = uc.conversion_id "
                + "WHERE m.material_id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToMaterial(rs);
                }
            }
        }
        return null;
    }

    /**
     * Thêm vật tư mới.
     */
    public boolean addMaterial(Material material) throws SQLException {
        String sql = "INSERT INTO materials (name, category_id, image_url, description, price, supplier_id, conversion_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, material.getName());
            ps.setInt(2, material.getCategoryId());
            ps.setString(3, material.getImageUrl());
            ps.setString(4, material.getDescription());
            ps.setBigDecimal(5, material.getPrice());
            ps.setInt(6, material.getSupplierId());
            ps.setInt(7, material.getConversionId());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin vật tư.
     */
    public boolean updateMaterial(Material material) throws SQLException {
        String sql = "UPDATE materials SET name = ?, category_id = ?, supplier_id = ?, price = ?, "
                + "conversion_id = ?, material_condition = ?, description = ?, image_url = ? "
                + "WHERE material_id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, material.getName());
            ps.setInt(2, material.getCategoryId());
            ps.setInt(3, material.getSupplierId());
            ps.setBigDecimal(4, material.getPrice());
            ps.setInt(5, material.getConversionId());
            ps.setString(6, material.getMaterialCondition());
            ps.setString(7, material.getDescription());
            ps.setString(8, material.getImageUrl());
            ps.setInt(9, material.getMaterialId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteMaterial(int materialId) throws SQLException {
        String sql = "DELETE FROM materials WHERE material_id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Thêm vật tư mới với ID do admin nhập.
     */
    public boolean addMaterialWithId(Material material) throws SQLException {
        String sql = "INSERT INTO materials (material_id, name, category_id, image_url, description, price, supplier_id, conversion_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, material.getMaterialId());
            ps.setString(2, material.getName());
            ps.setInt(3, material.getCategoryId());
            ps.setString(4, material.getImageUrl());
            ps.setString(5, material.getDescription());
            ps.setBigDecimal(6, material.getPrice());
            ps.setInt(7, material.getSupplierId());
            ps.setInt(8, material.getConversionId());
            return ps.executeUpdate() > 0;
        }
    }

    // --- CÁC HÀM HELPER ---
    private void buildWhereClauses(StringBuilder sql, List<Object> params, String searchQuery, String categoryFilter, String supplierFilter) {
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            sql.append("AND m.name LIKE ? ");
            params.add("%" + searchQuery.trim() + "%");
        }
        if (categoryFilter != null && !categoryFilter.trim().isEmpty() && !categoryFilter.equals("All")) {
            sql.append("AND c.name = ? ");
            params.add(categoryFilter.trim());
        }
        if (supplierFilter != null && !supplierFilter.trim().isEmpty() && !supplierFilter.equals("All")) {
            sql.append("AND s.supplier_name = ? ");
            params.add(supplierFilter.trim());
        }
    }

    private String getSafeSortField(String field) {
        switch (field.toLowerCase()) {
            case "material_id":
                return "m.material_id";
            case "name":
                return "m.name";
            case "price":
                return "m.price";
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
        material.setConversionId(rs.getInt("conversion_id"));
        material.setCategoryName(rs.getString("category_name"));
        material.setSupplierName(rs.getString("supplier_name"));
        material.setImageUrl(rs.getString("image_url"));
        material.setMaterialCondition(rs.getString("material_condition"));
        material.setPrice(rs.getBigDecimal("price"));
        material.setDescription(rs.getString("description"));
        material.setUnit(rs.getString("base_unit"));
        return material;
    }

    public List<Material> getMaterial() {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT m.material_id, m.name, m.category_id, c.parent_id " +
                    "FROM materials m " +
                    "LEFT JOIN categories c ON m.category_id = c.category_id " +
                    "ORDER BY m.name";
        try {
            PreparedStatement ca = new DBContext().connection.prepareStatement(sql);
            ResultSet st = ca.executeQuery();
            while (st.next()) {
                Material mate = new Material();
                mate.setName(st.getString("name"));
                mate.setMaterialId(st.getInt("material_id"));
                mate.setCategoryId(st.getInt("category_id"));
                list.add(mate);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}
