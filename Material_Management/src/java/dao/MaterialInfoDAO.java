package dao;

import dal.DBContext;
import model.Category;
import model.Supplier;
import model.Unit;
import model.Material;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialInfoDAO extends DBContext{

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT name FROM categories WHERE name IS NOT NULL AND hidden = 0 ORDER BY name";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    public List<Category> getAllCategoriesForDropdown() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT category_id, name, parent_id FROM categories WHERE hidden = 0 ORDER BY name";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category cat = new Category();
                cat.setCategoryId(rs.getInt("category_id"));
                cat.setName(rs.getString("name"));
                Object parentIdObj = rs.getObject("parent_id");
                cat.setParentId(parentIdObj != null ? rs.getInt("parent_id") : null);
                categories.add(cat);
            }
        }
        return categories;
    }

    public List<Unit> getAllWarehouseUnits() throws SQLException {
        List<Unit> units = new ArrayList<>();
        String sql = "SELECT unit_id, unit_name, is_system_unit, status FROM units WHERE is_system_unit = 1 AND status = 'Hoạt động' ORDER BY unit_name";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Unit unit = new Unit();
                unit.setUnit_id(rs.getInt("unit_id"));
                unit.setUnit_name(rs.getString("unit_name"));
                unit.setIs_system_unit(rs.getBoolean("is_system_unit"));
                unit.setStatus(rs.getString("status"));
                units.add(unit);
            }
        }
        return units;
    }

    public List<Category> getParentCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE parent_id IS NULL AND hidden = 0 ORDER BY name ASC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Category cat = new Category();
                cat.setCategoryId(rs.getInt("category_id"));
                cat.setName(rs.getString("name"));
                cat.setParentId(null);
                list.add(cat);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách danh mục vật tư: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    public void close(){
        super.closeConnection();
    }
} 