package dao;

import dal.DBContext;
import java.sql.*;
import java.util.*;

public class UserPermissionDAO {

    public Map<String, Boolean> getRolePermissions(int roleId) throws SQLException {
        Map<String, Boolean> rolePermissions = new LinkedHashMap<>();

        List<String> allPermissionNames = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT permission_name FROM permissions ORDER BY permission_name"); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                allPermissionNames.add(rs.getString("permission_name"));
            }
        }
        for (String perm : allPermissionNames) {
            rolePermissions.put(perm, false);
        }

        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT p.permission_name FROM role_permissions rp "
                + "JOIN permissions p ON rp.permission_id = p.permission_id "
                + "WHERE rp.role_id = ?")) {
            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String permName = rs.getString("permission_name");
                    rolePermissions.put(permName, true);
                }
            }
        }
        return rolePermissions;
    }

    public void updateRolePermissions(int roleId, List<String> permissions) throws SQLException {
        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try {
                Set<String> validPermissions = new HashSet<>();
                try (PreparedStatement stmt = conn.prepareStatement("SELECT permission_name FROM permissions"); ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        validPermissions.add(rs.getString("permission_name"));
                    }
                }
                List<String> filteredPermissions = new ArrayList<>();
                if (permissions != null) {
                    for (String perm : permissions) {
                        if (validPermissions.contains(perm)) {
                            filteredPermissions.add(perm);
                        }
                    }
                }

                try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM role_permissions WHERE role_id = ?")) {
                    deleteStmt.setInt(1, roleId);
                    deleteStmt.executeUpdate();
                }

                if (!filteredPermissions.isEmpty()) {
                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO role_permissions (role_id, permission_id) "
                            + "SELECT ?, permission_id FROM permissions WHERE permission_name = ?")) {
                        for (String perm : filteredPermissions) {
                            insertStmt.setInt(1, roleId);
                            insertStmt.setString(2, perm);
                            insertStmt.addBatch();
                        }
                        insertStmt.executeBatch();
                    }
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

    public List<Map<String, Object>> getAllPermissions() throws SQLException {
        List<Map<String, Object>> permissions = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT permission_id, permission_name, description FROM permissions ORDER BY permission_name"); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> permission = new HashMap<>();
                permission.put("permissionId", rs.getInt("permission_id"));
                permission.put("permissionName", rs.getString("permission_name"));
                permission.put("description", rs.getString("description") != null ? rs.getString("description") : "");
                permissions.add(permission);
            }
        }
        return permissions;
    }
}
