package dao;

import model.Category;
import dal.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO extends DBContext {

    // Lấy tất cả danh mục
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories";
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

    // Lấy danh mục vật tư (parent_id IS NULL)
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

    // Lấy danh sách có lọc theo keyword, parentId và sort
    public List<Category> getFilteredCategories(String keyword, Integer parentId, String sortBy) {
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

        // Xử lý sort
        if (sortBy != null) {
            switch (sortBy.toLowerCase()) {
                case "name":
                    sql.append(" ORDER BY name ASC");
                    break;
                case "id":
                default:
                    sql.append(" ORDER BY category_id ASC");
                    break;
            }
        } else {
            sql.append(" ORDER BY category_id ASC");
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

    // Thêm danh mục mới
    public boolean addCategory(String name, Integer parentId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            
            // Kiểm tra xem parentId có tồn tại và có phải là danh mục cha không
            if (parentId != null) {
                String checkSql = "SELECT parent_id FROM categories WHERE category_id = ?";
                stmt = conn.prepareStatement(checkSql);
                stmt.setInt(1, parentId);
                rs = stmt.executeQuery();
                
                if (!rs.next() || rs.getObject("parent_id") != null) {
                    System.err.println("Parent ID không hợp lệ: ID không tồn tại hoặc không phải danh mục cha");
                    return false;
                }
                
                rs.close();
                stmt.close();
            }

            String sql = "INSERT INTO categories (name, parent_id) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            if (parentId != null) {
                stmt.setInt(2, parentId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm danh mục: " + e.getMessage());
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

    // Lấy danh mục theo ID
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

    // Cập nhật danh mục
    public boolean updateCategory(int id, String name, Integer parentId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            
            // Kiểm tra xem category hiện tại có phải là parent không
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

            // Nếu là parent, không cho phép thêm parent_id
            if (isCurrentlyParent && parentId != null) {
                System.err.println("Không thể chuyển danh mục cha thành danh mục con khi đã có danh mục con");
                return false;
            }

            // Kiểm tra parentId có hợp lệ không
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

            String sql = "UPDATE categories SET name = ?, parent_id = ? WHERE category_id = ?";
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

    // Xóa danh mục
    public boolean deleteCategory(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            
            // Kiểm tra xem danh mục có danh mục con không
            String checkChildrenSql = "SELECT COUNT(*) as count FROM categories WHERE parent_id = ?";
            stmt = conn.prepareStatement(checkChildrenSql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next() && rs.getInt("count") > 0) {
                System.err.println("Không thể xóa danh mục này vì có danh mục con");
                return false;
            }
            
            rs.close();
            stmt.close();

            // Thực hiện xóa danh mục
            String sql = "DELETE FROM categories WHERE category_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa danh mục: " + e.getMessage());
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

    // Chuyển đổi từ ResultSet thành Category object
    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        int id = rs.getInt("category_id");
        String name = rs.getString("name");
        int parentIdValue = rs.getInt("parent_id");
        Integer parentId = rs.wasNull() ? null : parentIdValue;

        return new Category(id, name, parentId);
    }

    // Lấy tất cả danh mục có thể làm cha (trừ id được chỉ định)
    public List<Category> getAvailableParentCategories(Integer excludeId) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE category_id != ? OR ? IS NULL ORDER BY name ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            if (excludeId != null) {
                stmt.setInt(1, excludeId);
                stmt.setInt(2, excludeId);
            } else {
                stmt.setNull(1, Types.INTEGER);
                stmt.setNull(2, Types.INTEGER);
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

    // Lấy tên danh mục theo ID
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

    public List<Category> getFilteredCategoriesWithPaging(String keyword, Integer parentId, String sortBy, int page, int pageSize) {
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

        // Xử lý sort
        if (sortBy != null) {
            switch (sortBy.toLowerCase()) {
                case "name":
                    sql.append(" ORDER BY name ASC");
                    break;
                case "id":
                default:
                    sql.append(" ORDER BY category_id ASC");
                    break;
            }
        } else {
            sql.append(" ORDER BY category_id ASC");
        }

        // Thêm LIMIT và OFFSET cho phân trang
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
} 