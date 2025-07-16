package dao;

import dal.DBContext;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.lang.model.util.Types;
import model.ExportForm;

public class ExportFormDAO extends DBContext {

    public boolean existsExportFormByRequestId(int requestId) throws SQLException {
        String sql = "SELECT 1 FROM export_forms WHERE request_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public int addExportForm(model.ExportForm exportForm) throws SQLException {
        String sql = "INSERT INTO export_forms (request_id, user_id, export_date, reason, recipient_name, delivery_address, contact_person, contact_phone, status, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, exportForm.getRequestId());
            stmt.setInt(2, exportForm.getUserId());
            stmt.setTimestamp(3, exportForm.getExportDate());
            stmt.setString(4, exportForm.getReason());
            stmt.setString(5, exportForm.getRecipientName());
            stmt.setString(6, exportForm.getDeliveryAddress());
            stmt.setString(7, exportForm.getContactPerson());
            stmt.setString(8, exportForm.getContactPhone());
            stmt.setString(9, exportForm.getStatus());
            stmt.setString(10, exportForm.getDescription());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public int createPartialExportForm(model.ExportForm exportForm, String partialNote) throws SQLException {
        String sql = "INSERT INTO export_forms (request_id, user_id, export_date, reason, recipient_name, delivery_address, contact_person, contact_phone, status, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, exportForm.getRequestId());
            stmt.setInt(2, exportForm.getUserId());
            stmt.setTimestamp(3, exportForm.getExportDate());
            stmt.setString(4, exportForm.getReason());
            stmt.setString(5, exportForm.getRecipientName());
            stmt.setString(6, exportForm.getDeliveryAddress());
            stmt.setString(7, exportForm.getContactPerson());
            stmt.setString(8, exportForm.getContactPhone());
            stmt.setString(9, "Đã xuất kho");
            stmt.setString(10, partialNote);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    
    public int countAllExportForms(String projectName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM export_forms WHERE (? IS NULL OR recipient_name LIKE ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (projectName == null || projectName.trim().isEmpty()) {
                stmt.setNull(1, java.sql.Types.VARCHAR);
                stmt.setNull(2, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(1, projectName);
                stmt.setString(2, "%" + projectName + "%");
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<ExportForm> getAllExportFormsAndApprovedRequests(
            String projectName, String sortField, String sortDir, int page, int pageSize,
            String startDate, String endDate) throws SQLException {
        List<ExportForm> list = new ArrayList<>();
        if (!"export_id".equals(sortField) && !"export_date".equals(sortField) && !"recipient_name".equals(sortField)) {
            sortField = "export_id";
        }
        if (!"asc".equalsIgnoreCase(sortDir) && !"desc".equalsIgnoreCase(sortDir)) {
            sortDir = "asc";
        }
        int offset = (page - 1) * pageSize;

        StringBuilder sql = new StringBuilder(
                "SELECT ef.export_id, ef.request_id, ef.user_id, ef.export_date, ef.reason, ef.recipient_name, "
                + "ef.delivery_address, ef.contact_person, ef.contact_phone, ef.status, ef.description, "
                + "u.full_name as user_name, r.role_name "
                + "FROM export_forms ef "
                + "JOIN users u ON ef.user_id = u.user_id "
                + "JOIN roles r ON u.role_id = r.role_id "
                + "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if (projectName != null && !projectName.trim().isEmpty()) {
            sql.append(" AND ef.recipient_name LIKE ?");
            params.add("%" + projectName.trim() + "%");
        }
        
        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append(" AND ef.export_date >= ?");
            params.add(startDate.trim());
        }
        
        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append(" AND ef.export_date <= ?");
            if (endDate.trim().length() == 10) {
                params.add(endDate.trim() + " 23:59:59");
            } else {
                params.add(endDate.trim());
            }
        }

        sql.append(" ORDER BY ").append(sortField).append(" ").append(sortDir)
                .append(", export_id ").append(sortDir)
                .append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add(offset);

        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ExportForm f = new ExportForm();
                f.setExportId(rs.getInt("export_id"));
                f.setRequestId(rs.getInt("request_id"));
                f.setUserId(rs.getInt("user_id"));
                f.setExportDate(rs.getTimestamp("export_date"));
                f.setReason(rs.getString("reason"));
                f.setRecipientName(rs.getString("recipient_name"));
                f.setDeliveryAddress(rs.getString("delivery_address"));
                f.setContactPerson(rs.getString("contact_person"));
                f.setContactPhone(rs.getString("contact_phone"));
                f.setStatus(rs.getString("status"));
                f.setDescription(rs.getString("description"));
                f.setUserName(rs.getString("user_name"));
                f.setUserRole(rs.getString("role_name"));
                list.add(f);
            }
        }
        return list;
    }

    public ExportForm getExportFormById(int exportId) throws SQLException {
        String sql = "SELECT ef.*, u.full_name as user_name FROM export_forms ef JOIN users u ON ef.user_id = u.user_id WHERE ef.export_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, exportId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                model.ExportForm f = new model.ExportForm();
                f.setExportId(rs.getInt("export_id"));
                f.setRequestId(rs.getInt("request_id"));
                f.setUserId(rs.getInt("user_id"));
                f.setExportDate(rs.getTimestamp("export_date"));
                f.setReason(rs.getString("reason"));
                f.setRecipientName(rs.getString("recipient_name"));
                f.setDeliveryAddress(rs.getString("delivery_address"));
                f.setContactPerson(rs.getString("contact_person"));
                f.setContactPhone(rs.getString("contact_phone"));
                f.setStatus(rs.getString("status"));
                f.setDescription(rs.getString("description"));
                f.setUserName(rs.getString("user_name"));
                return f;
            }
        }
        return null;
    }
}
