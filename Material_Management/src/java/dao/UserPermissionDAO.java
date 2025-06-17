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
import java.util.Set;
import java.util.HashSet;

public class UserPermissionDAO {

    public Map<String, Boolean> getRolePermissions(int roleId) throws SQLException {
        Map<String, Boolean> rolePermissions = new HashMap<>();
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT p.permission_name FROM role_permissions rp "
                    + "JOIN permissions p ON rp.permission_id = p.permission_id "
                    + "WHERE rp.role_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, roleId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        rolePermissions.put(rs.getString("permission_name"), true);
                    }
                }
            }
        }
        return rolePermissions;
    }

    public void updateRolePermissions(int roleId, List<String> permissions) throws SQLException {
        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Validate permissions
                Set<String> validPermissions = new HashSet<>();
                try (PreparedStatement validStmt = conn.prepareStatement("SELECT permission_name FROM permissions")) {
                    try (ResultSet rs = validStmt.executeQuery()) {
                        while (rs.next()) {
                            validPermissions.add(rs.getString("permission_name"));
                        }
                    }
                }

                // Filter invalid permissions
                List<String> filteredPermissions = new ArrayList<>();
                for (String perm : permissions) {
                    if (validPermissions.contains(perm)) {
                        filteredPermissions.add(perm);
                    }
                }

                // Delete existing role permissions
                try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM role_permissions WHERE role_id = ?")) {
                    deleteStmt.setInt(1, roleId);
                    deleteStmt.executeUpdate();
                }

                // Insert new permissions
                if (!filteredPermissions.isEmpty()) {
                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO role_permissions (role_id, permission_id) "
                            + "SELECT ?, permission_id FROM permissions WHERE permission_name = ?")) {
                        insertStmt.setInt(1, roleId);
                        for (String perm : filteredPermissions) {
                            insertStmt.setString(2, perm);
                            insertStmt.executeUpdate();
                        }
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new SQLException("Failed to update permissions: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<Map<String, Object>> getAllPermissions() throws SQLException {
        List<Map<String, Object>> permissions = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT permission_id, permission_name, description FROM permissions ORDER BY permission_name";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> permission = new HashMap<>();
                        permission.put("permissionId", rs.getInt("permission_id"));
                        permission.put("permissionName", rs.getString("permission_name"));
                        permission.put("description", rs.getString("description") != null ? rs.getString("description") : "");
                        permissions.add(permission);
                    }
                }
            }
        }
        return permissions;
    }
}
