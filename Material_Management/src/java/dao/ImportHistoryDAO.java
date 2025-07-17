package dao;

import dal.DBContext;
import model.ImportHistory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class ImportHistoryDAO extends DBContext {

    public boolean insertImportHistory(ImportHistory history) {
        String sql = "INSERT INTO import_history (roles, reason, delivered_by, received_by, delivery_phone, project_name, material_name, quantity, unit, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, history.getRoles());
            ps.setString(2, history.getReason());
            ps.setString(3, history.getDeliveredBy());
            ps.setString(4, history.getReceivedBy());
            ps.setString(5, history.getDeliveryPhone());
            ps.setString(6, history.getProjectName());
            ps.setString(7, history.getMaterialName());
            ps.setInt(8, history.getQuantity());
            ps.setString(9, history.getUnit());
            ps.setString(10, history.getStatus());
            ps.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public List<ImportHistory> getAllImportHistory() {
        List<ImportHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM import_history ORDER BY created_at DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ImportHistory h = new ImportHistory();
                h.setId(rs.getInt("id"));
                h.setRoles(rs.getString("roles"));
                h.setReason(rs.getString("reason"));
                h.setDeliveredBy(rs.getString("delivered_by"));
                h.setReceivedBy(rs.getString("received_by"));
                h.setDeliveryPhone(rs.getString("delivery_phone"));
                h.setProjectName(rs.getString("project_name"));
                h.setMaterialName(rs.getString("material_name"));
                h.setQuantity(rs.getInt("quantity"));
                h.setUnit(rs.getString("unit"));
                h.setStatus(rs.getString("status"));
                h.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ImportHistory> getFilteredImportHistory(String search, String status, String date, String unit, int page) {
        List<ImportHistory> list = new ArrayList<>();
        int offset = (page - 1) * 5;
        String sql = "SELECT * FROM import_history WHERE 1=1";
        if (search != null && !search.trim().isEmpty()) {
            sql += " AND (material_name LIKE ? OR received_by LIKE ?)";
        }
        if (status != null && !status.trim().isEmpty()) {
            sql += " AND status = ?";
        }
        if (date != null && !date.trim().isEmpty()) {
            sql += " AND DATE(created_at) = ?";
        }
        if (unit != null && !unit.trim().isEmpty()) {
            sql += " AND unit = ?";
        }

        sql += " ORDER BY id DESC LIMIT 5 OFFSET ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (search != null && !search.trim().isEmpty()) {
                String like = "%" + search.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);

            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(idx++, status.trim());
            }

            if (date != null && !date.trim().isEmpty()) {
                ps.setString(idx++, date);
            }

            if (unit != null && !unit.trim().isEmpty()) {
                ps.setString(idx++, unit.trim());
            }

            ps.setInt(idx, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ImportHistory h = new ImportHistory();
                h.setId(rs.getInt("id"));
                h.setRoles(rs.getString("roles"));
                h.setReason(rs.getString("reason"));
                h.setDeliveredBy(rs.getString("delivered_by"));
                h.setReceivedBy(rs.getString("received_by"));
                h.setDeliveryPhone(rs.getString("delivery_phone"));
                h.setProjectName(rs.getString("project_name"));
                h.setMaterialName(rs.getString("material_name"));
                h.setQuantity(rs.getInt("quantity"));
                h.setUnit(rs.getString("unit"));
                h.setStatus(rs.getString("status"));
                h.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countFiltered(String search, String status, String date, String unit) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM import_history WHERE 1=1";

        if (search != null && !search.trim().isEmpty()) {
            sql += " AND (material_name LIKE ? OR received_by LIKE ?)";
        }
        if (status != null && !status.trim().isEmpty()) {
            sql += " AND status = ?";
        }
        if (date != null && !date.trim().isEmpty()) {
            sql += " AND DATE(created_at) = ?";
        }
        if (unit != null && !unit.trim().isEmpty()) {
            sql += " AND unit = ?";
        }

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;

            if (search != null && !search.trim().isEmpty()) {
                String like = "%" + search.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }

            if (status != null && !status.trim().isEmpty()) {
                ps.setString(idx++, status.trim());
            }

            if (date != null && !date.trim().isEmpty()) {
                ps.setString(idx++, date);
            }

            if (unit != null && !unit.trim().isEmpty()) {
                ps.setString(idx++, unit.trim());
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int pageSize = 5;
        return (total + pageSize - 1) / pageSize;
    }

    public int getcountPages() {
    String sql = "SELECT COUNT(*) FROM import_history";
    try {
        Connection conn = new DBContext().getConnection(); // Tạo mới connection ở đây!
        PreparedStatement st = conn.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            int total = rs.getInt(1);
            int countpage = 0;
            countpage = total / 5;
            if (total % 5 != 0) {
                countpage++;
            }
            return countpage;
        }
    } catch (SQLException e) {
        System.out.println(e);
    }
    return 0;
}
    public static void main(String[] args) {
        ImportHistoryDAO m = new ImportHistoryDAO();
        String a = "Công tắc";
        String b = "cái";
        String c = "Mới";
        String d = "2025-07-15";

        int l = m.getcountPages();
        System.out.println(l);
    }
}
