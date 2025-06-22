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

    public List<Map<String, Object>> getPermissionLogsByRole(int roleId) throws SQLException {
        List<Map<String, Object>> logs = new ArrayList<>();
        String sql = "SELECT l.log_id, l.role_id, r.role_name, l.admin_id, u.full_name AS admin_name, "
                   + "l.action, l.permission_name, l.old_value, l.new_value, l.log_date "
                   + "FROM permission_logs l "
                   + "LEFT JOIN users u ON l.admin_id = u.user_id "
                   + "LEFT JOIN roles r ON l.role_id = r.role_id "
                   + "WHERE l.role_id = ? "
                   + "ORDER BY l.log_date DESC";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> log = new HashMap<>();
                    log.put("logId", rs.getInt("log_id"));
                    log.put("roleId", rs.getInt("role_id"));
                    log.put("roleName", rs.getString("role_name"));
                    log.put("adminId", rs.getInt("admin_id"));
                    log.put("adminName", rs.getString("admin_name"));
                    log.put("action", rs.getString("action"));
                    log.put("permissionName", rs.getString("permission_name"));
                    log.put("oldValue", rs.getBoolean("old_value"));
                    log.put("newValue", rs.getBoolean("new_value"));
                    log.put("logDate", rs.getTimestamp("log_date"));
                    logs.add(log);
                }
            }
        }
        return logs;
    }

    public List<Map<String, Object>> getAllPermissionLogs() throws SQLException {
        List<Map<String, Object>> logs = new ArrayList<>();
        String sql = "SELECT l.log_id, l.role_id, r.role_name, l.admin_id, u.full_name AS admin_name, "
                   + "l.action, l.permission_name, l.old_value, l.new_value, l.log_date "
                   + "FROM permission_logs l "
                   + "LEFT JOIN users u ON l.admin_id = u.user_id "
                   + "LEFT JOIN roles r ON l.role_id = r.role_id "
                   + "ORDER BY l.log_date DESC";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> log = new HashMap<>();
                log.put("logId", rs.getInt("log_id"));
                log.put("roleId", rs.getInt("role_id"));
                log.put("roleName", rs.getString("role_name"));
                log.put("adminId", rs.getInt("admin_id"));
                log.put("adminName", rs.getString("admin_name"));
                log.put("action", rs.getString("action"));
                log.put("permissionName", rs.getString("permission_name"));
                log.put("oldValue", rs.getBoolean("old_value"));
                log.put("newValue", rs.getBoolean("new_value"));
                log.put("logDate", rs.getTimestamp("log_date"));
                logs.add(log);
            }
        }
        return logs;
    }
}
