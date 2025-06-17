package dao;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PermissionLogDAO {
    
    public void logPermissionChange(int roleId, int adminId, String action, String permissionName, 
            Boolean oldValue, Boolean newValue) throws SQLException {
        String sql = "INSERT INTO permission_logs (role_id, admin_id, action, permission_name, old_value, new_value) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roleId);
            stmt.setInt(2, adminId);
            stmt.setString(3, action);
            stmt.setString(4, permissionName);
            stmt.setBoolean(5, oldValue != null ? oldValue : false);
            stmt.setBoolean(6, newValue != null ? newValue : false);
            
            stmt.executeUpdate();
        }
    }
    
    public List<Map<String, Object>> getPermissionLogs(int roleId) throws SQLException {
        List<Map<String, Object>> logs = new ArrayList<>();
        String sql = "SELECT l.*, u.username as admin_name, r.rolename "
                + "FROM permission_logs l "
                + "JOIN users u ON l.admin_id = u.user_id "
                + "JOIN roles r ON l.role_id = r.role_id "
                + "WHERE l.role_id = ? "
                + "ORDER BY l.log_date DESC";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> log = Map.of(
                        "logId", rs.getInt("log_id"),
                        "roleId", rs.getInt("role_id"),
                        "roleName", rs.getString("rolename"),
                        "adminId", rs.getInt("admin_id"),
                        "adminName", rs.getString("admin_name"),
                        "action", rs.getString("action"),
                        "permissionName", rs.getString("permission_name"),
                        "oldValue", rs.getBoolean("old_value"),
                        "newValue", rs.getBoolean("new_value"),
                        "logDate", rs.getTimestamp("log_date")
                    );
                    logs.add(log);
                }
            }
        }
        return logs;
    }
} 