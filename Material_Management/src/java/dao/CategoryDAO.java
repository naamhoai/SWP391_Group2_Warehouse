package dao;

import model.Category;
import dal.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO extends DBContext {

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY hidden ASC, category_id ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToCategory(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }

        return list;
    }

    public List<Category> getVisibleCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE hidden = 0 ORDER BY category_id ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToCategory(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }

        return list;
    }

    public List<Category> getParentCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE parent_id IS NULL ORDER BY name ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToCategory(rs));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách danh mục vật tư: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }

        return list;
    }

    public List<Category> getVisibleParentCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE parent_id IS NULL AND hidden = 0 ORDER BY name ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToCategory(rs));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách danh mục vật tư: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }

        return list;
    }

    public List<Category> getFilteredCategories(String keyword, Integer parentId, String sortBy, String hiddenFilter) {
        List<Category> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM categories WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND LOWER(name) LIKE LOWER(?)");
            params.add("%" + keyword.trim() + "%");
        }

        if (parentId != null) {
            sql.append(" AND parent_id = ?");
            params.add(parentId);
        }

        if (hiddenFilter != null && !hiddenFilter.equals("all")) {
            if (hiddenFilter.equals("visible")) {
                sql.append(" AND hidden = 0");
            } else if (hiddenFilter.equals("hidden")) {
                sql.append(" AND hidden = 1");
            }
        }

        sql.append(" ORDER BY hidden ASC");
        if (sortBy != null) {
            switch (sortBy.toLowerCase()) {
                case "name":
                    sql.append(", name ASC");
                    break;
                case "id":
                default:
                    sql.append(", category_id ASC");
                    break;
            }
        } else {
            sql.append(", category_id ASC");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToCategory(rs));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn danh mục: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }

        return list;
    }

    public String addCategory(String name, Integer parentId, java.sql.Timestamp createdAt) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (parentId != null) {
                String checkSql = "SELECT parent_id FROM categories WHERE category_id = ?";
                stmt = conn.prepareStatement(checkSql);
                stmt.setInt(1, parentId);
                rs = stmt.executeQuery();
                if (!rs.next()) {
                    return "ID danh mục cha không tồn tại.";
                }
                if (rs.getObject("parent_id") != null) {
                    return "ID danh mục cha không hợp lệ (không phải danh mục cha gốc).";
                }
                rs.close();
                stmt.close();
            }
            String sql = "INSERT INTO categories (name, parent_id, created_at, updated_at) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            if (parentId != null) {
                stmt.setInt(2, parentId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            if (createdAt != null) {
                stmt.setTimestamp(3, createdAt);
                stmt.setTimestamp(4, createdAt);
            } else {
                java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
                stmt.setTimestamp(3, now);
                stmt.setTimestamp(4, now);
            }
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) return null;
            else return "Không thể thêm danh mục (không có dòng nào bị ảnh hưởng).";
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm danh mục: " + e.getMessage());
            e.printStackTrace();
            return "Lỗi SQL: " + e.getMessage();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }
    }

    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCategory(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }

        return null;
    }

    public boolean updateCategory(int id, String name, Integer parentId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            
            String checkChildrenSql = "SELECT COUNT(*) as count FROM categories WHERE parent_id = ?";
            stmt = conn.prepareStatement(checkChildrenSql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            boolean isCurrentlyParent = false;
            if (rs.next() && rs.getInt("count") > 0) {
                isCurrentlyParent = true;
            }
            
            rs.close();
            stmt.close();

            if (isCurrentlyParent && parentId != null) {
                System.err.println("Không thể chuyển danh mục cha thành danh mục con khi đã có danh mục con");
                return false;
            }

            if (parentId != null) {
                if (parentId == id) {
                    System.err.println("Không thể chọn chính nó làm danh mục cha");
                    return false;
                }

                String checkParentSql = "SELECT parent_id FROM categories WHERE category_id = ?";
                stmt = conn.prepareStatement(checkParentSql);
                stmt.setInt(1, parentId);
                rs = stmt.executeQuery();
                
                if (!rs.next() || rs.getObject("parent_id") != null) {
                    System.err.println("Parent ID không hợp lệ: ID không tồn tại hoặc không phải danh mục cha");
                    return false;
                }
                
                rs.close();
                stmt.close();
            }

            String sql = "UPDATE categories SET name = ?, parent_id = ?, updated_at = NOW() WHERE category_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            if (parentId != null) {
                stmt.setInt(2, parentId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật danh mục: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }
    }

    public List<Category> getAvailableParentCategories(Integer excludeId) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE parent_id IS NULL" + (excludeId != null ? " AND category_id != ?" : "") + " ORDER BY name ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (excludeId != null) {
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, excludeId);
            } else {
                stmt = conn.prepareStatement(sql);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToCategory(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách danh mục cha: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }
        return list;
    }

    public String getCategoryNameById(Integer categoryId) {
        if (categoryId == null) return null;
        
        String sql = "SELECT name FROM categories WHERE category_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, categoryId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("name");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tên danh mục: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }

        return null;
    }

    public List<Category> getFilteredCategoriesWithPaging(String keyword, Integer parentId, String sortBy, String sortOrder, String hiddenFilter, int page, int pageSize) {
        List<Category> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM categories WHERE parent_id IS NOT NULL");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND LOWER(name) LIKE LOWER(?)");
            params.add("%" + keyword.trim() + "%");
        }

        if (parentId != null) {
            sql.append(" AND parent_id = ?");
            params.add(parentId);
        }

        if (hiddenFilter != null && !hiddenFilter.equals("all")) {
            if (hiddenFilter.equals("visible")) {
                sql.append(" AND hidden = 0");
            } else if (hiddenFilter.equals("hidden")) {
                sql.append(" AND hidden = 1");
            }
        }

        sql.append(" ORDER BY hidden ASC");
        if (sortBy != null) {
            switch (sortBy.toLowerCase()) {
                case "name":
                    sql.append(", name ASC");
                    break;
                case "id":
                default:
                    sql.append(", category_id ");
                    if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
                        sql.append("DESC");
                    } else {
                        sql.append("ASC");
                    }
                    break;
            }
        } else {
            sql.append(", category_id ASC");
        }

        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToCategory(rs));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn danh mục: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }

        return list;
    }

    public int getTotalCategories(String keyword, Integer parentId, String hiddenFilter) {
        int totalRecords = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) as total FROM categories WHERE parent_id IS NOT NULL");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND LOWER(name) LIKE LOWER(?)");
            params.add("%" + keyword.trim() + "%");
        }

        if (parentId != null) {
            sql.append(" AND parent_id = ?");
            params.add(parentId);
        }

        if (hiddenFilter != null && !hiddenFilter.equals("all")) {
            if (hiddenFilter.equals("visible")) {
                sql.append(" AND hidden = 0");
            } else if (hiddenFilter.equals("hidden")) {
                sql.append(" AND hidden = 1");
            }
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            rs = stmt.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi đếm tổng số danh mục: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }

        return totalRecords;
    }

    public int getTotalPages(String keyword, Integer parentId, int pageSize) {
        int totalRecords = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) as total FROM categories WHERE parent_id IS NOT NULL");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND LOWER(name) LIKE LOWER(?)");
            params.add("%" + keyword.trim() + "%");
        }

        if (parentId != null) {
            sql.append(" AND parent_id = ?");
            params.add(parentId);
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            rs = stmt.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi đếm tổng số danh mục: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }

        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    public boolean hideCategory(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            // 1. Ẩn category
            String sql = "UPDATE categories SET hidden = 1 WHERE category_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            // 2. Cập nhật tất cả vật tư thuộc category này sang ngừng kinh doanh
            String updateMaterials = "UPDATE materials SET status = 'inactive' WHERE category_id = ?";
            stmt = conn.prepareStatement(updateMaterials);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi ẩn danh mục: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }
    }

    public boolean unhideCategory(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            String sql = "UPDATE categories SET hidden = 0 WHERE category_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi hiện lại danh mục: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }
    }

    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        int id = rs.getInt("category_id");
        String name = rs.getString("name");
        int parentIdValue = rs.getInt("parent_id");
        Integer parentId = rs.wasNull() ? null : parentIdValue;
        boolean hidden = false;
        try {
            hidden = rs.getBoolean("hidden");
        } catch (SQLException | IllegalArgumentException e) {
            // Nếu cột không tồn tại (cũ), giữ false
        }
        java.sql.Timestamp createdAt = null;
        java.sql.Timestamp updatedAt = null;
        try {
            createdAt = rs.getTimestamp("created_at");
        } catch (SQLException e) {}
        try {
            updatedAt = rs.getTimestamp("updated_at");
        } catch (SQLException e) {}
        return new Category(id, name, parentId, hidden, createdAt, updatedAt);
    }
} 