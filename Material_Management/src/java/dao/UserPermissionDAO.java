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

public class UserPermissionDAO {
    
    public List<Map<String, Object>> searchUser(String keyword) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (Connection conn = new DBContext().getConnection()) {
            // Search user by full_name or user_id
            PreparedStatement userStmt = conn.prepareStatement(
                "SELECT u.user_id, u.full_name, u.role_id, r.role_name " +
                "FROM users u JOIN roles r ON u.role_id = r.role_id " +
                "WHERE u.full_name LIKE ? OR u.user_id = ?");
            userStmt.setString(1, "%" + keyword + "%");
            try {
                userStmt.setInt(2, Integer.parseInt(keyword));
            } catch (NumberFormatException e) {
                userStmt.setInt(2, -1); // Invalid ID, won't match any user_id
            }
            
            ResultSet userRs = userStmt.executeQuery();
            while (userRs.next()) { // Lặp qua tất cả kết quả
                Map<String, Object> result = new HashMap<>();
                result.put("userId", userRs.getInt("user_id"));
                result.put("fullName", userRs.getString("full_name"));
                result.put("roleId", userRs.getInt("role_id"));
                result.put("roleName", userRs.getString("role_name"));
                
                // Get role permissions
                Map<String, Boolean> rolePermissions = getRolePermissions(userRs.getInt("role_id"));
                result.put("rolePermissions", rolePermissions);
                
                // Get user permissions
                List<String> userPermissions = getUserPermissions(userRs.getInt("user_id"));
                result.put("userPermissions", userPermissions);
                
                results.add(result);
            }
        }
        return results; // Trả về danh sách người dùng
    }
    
    
    private Map<String, Boolean> getRolePermissions(int roleId) throws SQLException {
        Map<String, Boolean> rolePermissions = new HashMap<>();
        try (Connection conn = new DBContext().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT p.permission_name FROM role_permissions rp " +
                "JOIN permissions p ON rp.permission_id = p.permission_id " +
                "WHERE rp.role_id = ?");
            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rolePermissions.put(rs.getString("permission_name"), true);
            }
        }
        return rolePermissions;
    }
    
    private List<String> getUserPermissions(int userId) throws SQLException {
        List<String> userPermissions = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT p.permission_name FROM user_permissions up " +
                "JOIN permissions p ON up.permission_id = p.permission_id " +
                "WHERE up.user_id = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userPermissions.add(rs.getString("permission_name"));
            }
        }
        return userPermissions;
    }
    
    public void updateUserPermissions(int userId, List<String> permissions) throws SQLException {
        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Delete existing user permissions
                PreparedStatement deleteStmt = conn.prepareStatement(
                    "DELETE FROM user_permissions WHERE user_id = ?");
                deleteStmt.setInt(1, userId);
                deleteStmt.executeUpdate();

                // Insert new permissions
                PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO user_permissions (user_id, permission_id) " +
                    "SELECT ?, permission_id FROM permissions WHERE permission_name = ?");
                insertStmt.setInt(1, userId);
                
                for (String perm : permissions) {
                    insertStmt.setString(2, perm);
                    insertStmt.executeUpdate();
                }
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<Map<String, Object>> getAllUsersWithPermissions() throws SQLException {
        List<Map<String, Object>> users = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT u.user_id, u.full_name, u.role_id, r.role_name " +
                        "FROM users u " +
                        "JOIN roles r ON u.role_id = r.role_id " +
                        "ORDER BY u.user_id";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("userId", rs.getInt("user_id"));
                    user.put("fullName", rs.getString("full_name"));
                    user.put("roleId", rs.getInt("role_id"));
                    user.put("roleName", rs.getString("role_name"));
                    
                    // Get role permissions
                    Map<String, Boolean> rolePermissions = getRolePermissions(rs.getInt("role_id"));
                    user.put("rolePermissions", rolePermissions);
                    
                    // Get user permissions
                    List<String> userPermissions = getUserPermissions(rs.getInt("user_id"));
                    user.put("userPermissions", userPermissions);
                    
                    users.add(user);
                }
            }
        }
        return users;
    }

    public List<Map<String, Object>> getAllPermissions() throws SQLException {
        List<Map<String, Object>> permissions = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT permission_id, permission_name, description FROM permissions ORDER BY permission_name";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, Object> permission = new HashMap<>();
                    permission.put("permissionId", rs.getInt("permission_id"));
                    permission.put("permissionName", rs.getString("permission_name"));
                    permission.put("description", rs.getString("description"));
                    permissions.add(permission);
                }
            }
        }
        return permissions;
    }
} 