package dal;

import model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO extends DBContext {

    // Lấy tất cả danh mục
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToCategory(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Lấy danh mục cha (parent_id IS NULL)
    public List<Category> getParentCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE parent_id IS NULL";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToCategory(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Lấy danh sách có lọc theo keyword, parentId và sort
    public List<Category> getFilteredCategories(String keyword, Integer parentId, String sortBy) {
        List<Category> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM categories WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND LOWER(name) LIKE ?");
            params.add("%" + keyword.trim().toLowerCase() + "%");
        }

        if (parentId != null) {
            sql.append(" AND parent_id = ?");
            params.add(parentId);
        }
        // Không thêm "AND parent_id IS NULL" khi không lọc theo parentId

        if (sortBy != null) {
            switch (sortBy.toLowerCase()) {
                case "name":
                    sql.append(" ORDER BY name ASC");
                    break;
                case "id":
                    sql.append(" ORDER BY category_id ASC");
                    break;
                case "priority":
                    sql.append(" ORDER BY priority ASC");
                    break;
                default:
                    sql.append(" ORDER BY category_id ASC");
            }
        } else {
            sql.append(" ORDER BY category_id ASC");
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCategory(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Thêm danh mục mới
    public void addCategory(String name, Integer parentId) {
        String sql = "INSERT INTO categories (name, parent_id) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("Thêm danh mục: " + name + " | Parent: " + parentId);

            stmt.setString(1, name);
            if (parentId != null) {
                stmt.setInt(2, parentId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            int rows = stmt.executeUpdate();
            System.out.println("Số dòng được thêm: " + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy một danh mục theo ID
    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Cập nhật danh mục
    public void updateCategory(int id, String name, Integer parentId) {
        if (parentId != null && parentId == id) {
            System.err.println("Không thể chọn chính nó làm danh mục cha");
            return;
        }

        String sql = "UPDATE categories SET name = ?, parent_id = ? WHERE category_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            if (parentId != null) {
                stmt.setInt(2, parentId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, id);

            int rows = stmt.executeUpdate();
            System.out.println("Cập nhật danh mục ID: " + id + " | Rows affected: " + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa danh mục
    public void deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE category_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println("Xóa danh mục ID: " + id + " | Rows affected: " + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Map dữ liệu từ ResultSet thành Category object
    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        int id = rs.getInt("category_id");
        String name = rs.getString("name");
        int pid = rs.getInt("parent_id");
        Integer parentId = rs.wasNull() ? null : pid;

        return new Category(id, name, parentId);
    }
}
