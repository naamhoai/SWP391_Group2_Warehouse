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

public class PermissionListDAO {
    
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
                        
                        // Extract module from permission name
                        String permissionName = rs.getString("permission_name");
                        String[] parts = permissionName.split("_");
                        if (parts.length >= 2) {
                            permission.put("module", parts[0]);
                        }
                        
                        permissions.add(permission);
                    }
                }
            }
        }
        return permissions;
    }
    
    public Map<String, List<Map<String, Object>>> getPermissionsByModule() throws SQLException {
        List<Map<String, Object>> allPermissions = getAllPermissions();
        Map<String, List<Map<String, Object>>> permissionsByModule = new HashMap<>();
        
        for (Map<String, Object> permission : allPermissions) {
            String module = (String) permission.get("module");
            if (module != null) {
                permissionsByModule.computeIfAbsent(module, k -> new ArrayList<>()).add(permission);
            }
        }
        
        return permissionsByModule;
    }
    
    public List<Map<String, Object>> getPermissionsByModuleName(String moduleName) throws SQLException {
        List<Map<String, Object>> permissions = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT permission_id, permission_name, description FROM permissions " +
                        "WHERE permission_name LIKE ? ORDER BY permission_name";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, moduleName + "_%");
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
    
    public Map<String, Object> getPermissionById(int permissionId) throws SQLException {
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT permission_id, permission_name, description FROM permissions WHERE permission_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, permissionId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Map<String, Object> permission = new HashMap<>();
                        permission.put("permissionId", rs.getInt("permission_id"));
                        permission.put("permissionName", rs.getString("permission_name"));
                        permission.put("description", rs.getString("description") != null ? rs.getString("description") : "");
                        return permission;
                    }
                }
            }
        }
        return null;
    }
    
    public List<Map<String, Object>> searchPermissions(String keyword) throws SQLException {
        List<Map<String, Object>> permissions = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT permission_id, permission_name, description FROM permissions " +
                        "WHERE permission_name LIKE ? OR description LIKE ? ORDER BY permission_name";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "%" + keyword + "%");
                stmt.setString(2, "%" + keyword + "%");
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