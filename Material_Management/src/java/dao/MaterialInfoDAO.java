package dao;

import dal.DBContext;
import model.Category;
import model.Supplier;
import model.UnitConversion;
import model.Material;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialInfoDAO {
    private DBContext dbContext = new DBContext();

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT name FROM categories WHERE name IS NOT NULL ORDER BY name";
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

    public List<String> getAllSuppliers() {
        List<String> suppliers = new ArrayList<>();
        String sql = "SELECT DISTINCT supplier_name FROM supplier WHERE status = 'active' ORDER BY supplier_name";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                suppliers.add(rs.getString("supplier_name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return suppliers;
    }

    public List<Category> getAllCategoriesForDropdown() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT category_id, name FROM categories ORDER BY name";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category cat = new Category();
                cat.setCategoryId(rs.getInt("category_id"));
                cat.setName(rs.getString("name"));
                categories.add(cat);
            }
        }
        return categories;
    }

    public List<Supplier> getAllSuppliersForDropdown() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT supplier_id, supplier_name FROM supplier WHERE status = 'active' ORDER BY supplier_name";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Supplier sup = new Supplier();
                sup.setSupplierId(rs.getInt("supplier_id"));
                sup.setSupplierName(rs.getString("supplier_name"));
                suppliers.add(sup);
            }
        }
        return suppliers;
    }

    public List<UnitConversion> getAllUnitConversions() throws SQLException {
        List<UnitConversion> units = new ArrayList<>();
        String sql = "SELECT conversion_id, base_unit FROM unit_conversion ORDER BY base_unit";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UnitConversion unit = new UnitConversion();
                Material material = new Material();
                material.setConversionId(rs.getInt("conversion_id"));
                unit.setMaterial(material);
                unit.setBaseunit(rs.getString("base_unit"));
                units.add(unit);
            }
        }
        return units;
    }
} 