package dao;

import model.Material;
import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO extends DBContext {
    private static final int DEFAULT_ITEMS_PER_PAGE = 5;

    public int getTotalPages(String searchQuery, String categoryFilter, String statusFilter, int itemsPerPage) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) as total FROM materials m ")
           .append("LEFT JOIN categories c ON m.category_id = c.category_id ")
           .append("LEFT JOIN inventory i ON m.material_id = i.material_id ")
           .append("WHERE 1=1 ");

        ArrayList<Object> params = new ArrayList<>();
        
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            sql.append("AND m.name LIKE ? ");
            params.add("%" + searchQuery.trim() + "%");
        }
        
        if (categoryFilter != null && !categoryFilter.trim().isEmpty() && !categoryFilter.equals("All")) {
            sql.append("AND c.name = ? ");
            params.add(categoryFilter.trim());
        }
        
        if (statusFilter != null && !statusFilter.trim().isEmpty() && !statusFilter.equals("All")) {
            if (statusFilter.equals("In Stock")) {
                sql.append("AND COALESCE(i.quantity, 0) > 0 ");
            } else if (statusFilter.equals("Out of Stock")) {
                sql.append("AND COALESCE(i.quantity, 0) = 0 ");
            }
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            System.out.println("Executing count query: " + sql.toString());
            System.out.println("Count parameters: " + params);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int totalItems = rs.getInt("total");
                return (int) Math.ceil((double) totalItems / itemsPerPage);
            }
        }
        return 0;
    }

    public List<Material> getAllMaterials(String searchQuery, String categoryFilter, String statusFilter, 
                                        int page, int itemsPerPage, String sortField, String sortDir) throws SQLException {
        List<Material> materials = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT m.*, c.name as category_name, COALESCE(i.quantity, 0) as inventory_quantity ")
           .append("FROM materials m ")
           .append("LEFT JOIN categories c ON m.category_id = c.category_id ")
           .append("LEFT JOIN inventory i ON m.material_id = i.material_id ")
           .append("WHERE 1=1 ");

        ArrayList<Object> params = new ArrayList<>();
        
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            sql.append("AND m.name LIKE ? ");
            params.add("%" + searchQuery.trim() + "%");
        }
        
        if (categoryFilter != null && !categoryFilter.trim().isEmpty() && !categoryFilter.equals("All")) {
            sql.append("AND c.name = ? ");
            params.add(categoryFilter.trim());
        }
        
        if (statusFilter != null && !statusFilter.trim().isEmpty() && !statusFilter.equals("All")) {
            if (statusFilter.equals("In Stock")) {
                sql.append("AND COALESCE(i.quantity, 0) > 0 ");
            } else if (statusFilter.equals("Out of Stock")) {
                sql.append("AND COALESCE(i.quantity, 0) = 0 ");
            }
        }

        // Add sorting
        if (sortField != null && !sortField.trim().isEmpty()) {
            String safeField = getSafeSortField(sortField);
            String safeDir = "asc".equalsIgnoreCase(sortDir) ? "ASC" : "DESC";
            sql.append("ORDER BY ").append(safeField).append(" ").append(safeDir);
        } else {
            sql.append("ORDER BY m.material_id DESC");
        }

        sql.append(" LIMIT ? OFFSET ?");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            for (Object param : params) {
                ps.setObject(paramIndex++, param);
            }
            
            ps.setInt(paramIndex++, itemsPerPage);
            ps.setInt(paramIndex, (page - 1) * itemsPerPage);

            System.out.println("Executing materials query: " + sql.toString());
            System.out.println("Materials parameters: " + params);
            System.out.println("Page: " + page + ", Items per page: " + itemsPerPage);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Material material = new Material();
                material.setMaterialId(rs.getInt("material_id"));
                material.setName(rs.getString("name"));
                material.setCategoryId(rs.getInt("category_id"));
                material.setCategoryName(rs.getString("category_name"));
                material.setImageUrl(rs.getString("image_url"));
                material.setMaterialCondition(rs.getString("material_condition"));
                material.setPrice(rs.getBigDecimal("price"));
                material.setDescription(rs.getString("description"));
                material.setQuantity(rs.getInt("inventory_quantity"));
                materials.add(material);
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllMaterials: " + e.getMessage());
            throw e;
        }
        return materials;
    }

    // Helper method to prevent SQL injection in ORDER BY clause
    private String getSafeSortField(String field) {
        switch (field.toLowerCase()) {
            case "material_id":
                return "m.material_id";
            case "price":
                return "m.price";
            case "quantity":
                return "inventory_quantity";
            default:
                return "m.material_id";
        }
    }

    public List<String> getAllCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT name FROM categories WHERE name IS NOT NULL ORDER BY name";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllCategories: " + e.getMessage());
            throw e;
        }
        return categories;
    }
} 