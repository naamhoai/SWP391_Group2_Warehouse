package dao;

import dal.DBContext;
import java.sql.*;
import java.util.*;

public class PermissionListDAO {

    public List<Map<String, Object>> getAllPermissions() throws SQLException {
        List<Map<String, Object>> permissions = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT permission_id, permission_name FROM permissions ORDER BY permission_id ASC";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> permission = new HashMap<>();
                    permission.put("permissionId", rs.getInt("permission_id"));
                    permission.put("permissionName", rs.getString("permission_name"));
                    String permissionName = rs.getString("permission_name");
                    String[] parts = permissionName.split("_");
                    if (parts.length >= 2) {
                        permission.put("module", parts[0]);
                    }
                    permissions.add(permission);
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
}
