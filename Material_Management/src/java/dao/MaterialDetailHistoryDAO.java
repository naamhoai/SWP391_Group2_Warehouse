package dao;

import dal.DBContext;
import model.MaterialDetailHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDetailHistoryDAO extends DBContext {
    public List<MaterialDetailHistory> getHistoryByMaterialId(int materialId) {
        List<MaterialDetailHistory> list = new ArrayList<>();
        String sql = "SELECT h.*, m.name as material_name, u.full_name as user_name FROM material_detail_history h LEFT JOIN materials m ON h.material_id = m.material_id LEFT JOIN users u ON h.changed_by = u.user_id WHERE h.material_id = ? ORDER BY h.changed_at DESC, h.history_id DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MaterialDetailHistory h = new MaterialDetailHistory(
                        rs.getInt("history_id"),
                        rs.getInt("material_id"),
                        rs.getString("field_name"),
                        rs.getString("old_value"),
                        rs.getString("new_value"),
                        rs.getInt("changed_by"),
                        rs.getString("role_name"),
                        rs.getTimestamp("changed_at")
                    );
                    try {
                        java.lang.reflect.Field f = h.getClass().getDeclaredField("materialName");
                        f.setAccessible(true);
                        f.set(h, rs.getString("material_name"));
                    } catch (Exception ignore) {}
                    try {
                        java.lang.reflect.Field f2 = h.getClass().getDeclaredField("userName");
                        f2.setAccessible(true);
                        f2.set(h, rs.getString("user_name"));
                    } catch (Exception ignore) {}
                    list.add(h);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insertHistory(MaterialDetailHistory history) {
        String sql = "INSERT INTO material_detail_history (material_id, field_name, old_value, new_value, changed_by, role_name, changed_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, history.getMaterialId());
            ps.setString(2, history.getFieldName());
            ps.setString(3, history.getOldValue());
            ps.setString(4, history.getNewValue());
            ps.setInt(5, history.getChangedBy());
            ps.setString(6, history.getRoleName());
            ps.setTimestamp(7, history.getChangedAt());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<MaterialDetailHistory> getAllHistory() {
        List<MaterialDetailHistory> list = new ArrayList<>();
        String sql = "SELECT h.*, m.name as material_name, u.full_name as user_name FROM material_detail_history h LEFT JOIN materials m ON h.material_id = m.material_id LEFT JOIN users u ON h.changed_by = u.user_id ORDER BY h.changed_at DESC, h.history_id DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MaterialDetailHistory h = new MaterialDetailHistory(
                        rs.getInt("history_id"),
                        rs.getInt("material_id"),
                        rs.getString("field_name"),
                        rs.getString("old_value"),
                        rs.getString("new_value"),
                        rs.getInt("changed_by"),
                        rs.getString("role_name"),
                        rs.getTimestamp("changed_at")
                    );
                    try {
                        java.lang.reflect.Field f = h.getClass().getDeclaredField("materialName");
                        f.setAccessible(true);
                        f.set(h, rs.getString("material_name"));
                    } catch (Exception ignore) {}
                    try {
                        java.lang.reflect.Field f2 = h.getClass().getDeclaredField("userName");
                        f2.setAccessible(true);
                        f2.set(h, rs.getString("user_name"));
                    } catch (Exception ignore) {}
                    list.add(h);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MaterialDetailHistory> getFilteredHistory(String fromDate, String toDate, String keyword, String roleName, String unused) {
        List<MaterialDetailHistory> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT h.*, m.name as material_name, u.full_name as user_name FROM material_detail_history h LEFT JOIN materials m ON h.material_id = m.material_id LEFT JOIN users u ON h.changed_by = u.user_id WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append(" AND DATE(h.changed_at) >= ?");
            params.add(fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append(" AND DATE(h.changed_at) <= ?");
            params.add(toDate);
        }
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (m.name LIKE ? OR u.full_name LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }
        if (roleName != null && !roleName.isEmpty()) {
            sql.append(" AND h.role_name = ?");
            params.add(roleName);
        }
        sql.append(" ORDER BY h.changed_at DESC, h.history_id DESC");
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MaterialDetailHistory h = new MaterialDetailHistory(
                        rs.getInt("history_id"),
                        rs.getInt("material_id"),
                        rs.getString("field_name"),
                        rs.getString("old_value"),
                        rs.getString("new_value"),
                        rs.getInt("changed_by"),
                        rs.getString("role_name"),
                        rs.getTimestamp("changed_at")
                    );
                    try {
                        java.lang.reflect.Field f = h.getClass().getDeclaredField("materialName");
                        f.setAccessible(true);
                        f.set(h, rs.getString("material_name"));
                    } catch (Exception ignore) {}
                    try {
                        java.lang.reflect.Field f2 = h.getClass().getDeclaredField("userName");
                        f2.setAccessible(true);
                        f2.set(h, rs.getString("user_name"));
                    } catch (Exception ignore) {}
                    list.add(h);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
} 