package dao;

import dal.DBContext;
import model.RequestHistory;
import model.RequestHistoryDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestHistoryDAO {

    public int addRequestHistory(RequestHistory history) {
        String sql = "INSERT INTO request_history (request_id, changed_by, old_status, new_status, action, change_reason, director_note) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int historyId = -1;
        try (Connection conn = new DBContext().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, history.getRequestId());
            stmt.setInt(2, history.getChangedBy());
            stmt.setString(3, history.getOldStatus());
            stmt.setString(4, history.getNewStatus());
            stmt.setString(5, history.getAction());
            stmt.setString(6, history.getChangeReason());
            stmt.setString(7, history.getDirectorNote());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                historyId = rs.getInt(1);
                history.setHistoryId(historyId);
                
                if (history.getHistoryDetails() != null && !history.getHistoryDetails().isEmpty()) {
                    addRequestHistoryDetails(historyId, history.getHistoryDetails());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyId;
    }

    public void addRequestHistoryDetails(int historyId, List<RequestHistoryDetail> details) {
        String sql = "INSERT INTO request_history_details (history_id, material_id, material_name, quantity, warehouse_unit_id, material_condition) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (RequestHistoryDetail detail : details) {
                stmt.setInt(1, historyId);
                stmt.setInt(2, detail.getMaterialId());
                stmt.setString(3, detail.getMaterialName());
                stmt.setInt(4, detail.getQuantity());
                stmt.setInt(5, detail.getWarehouseUnitId());
                stmt.setString(6, detail.getMaterialCondition());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<RequestHistory> getRequestHistoryByRequestId(int requestId) {
        List<RequestHistory> list = new ArrayList<>();
        String sql = "SELECT h.*, u.full_name as changed_by_name, r.role_name as changed_by_role, req.recipient_name, req.delivery_address, req.contact_person, req.contact_phone, req.created_at, u2.full_name as creator_name " +
                    "FROM request_history h " +
                    "LEFT JOIN users u ON h.changed_by = u.user_id " +
                    "LEFT JOIN roles r ON u.role_id = r.role_id " +
                    "LEFT JOIN requests req ON h.request_id = req.request_id " +
                    "LEFT JOIN users u2 ON req.user_id = u2.user_id " +
                    "WHERE h.request_id = ? ORDER BY h.change_time DESC";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RequestHistory h = new RequestHistory();
                h.setHistoryId(rs.getInt("history_id"));
                h.setRequestId(rs.getInt("request_id"));
                h.setChangedBy(rs.getInt("changed_by"));
                h.setChangeTime(rs.getTimestamp("change_time"));
                h.setOldStatus(rs.getString("old_status"));
                h.setNewStatus(rs.getString("new_status"));
                h.setAction(rs.getString("action"));
                h.setChangeReason(rs.getString("change_reason"));
                h.setDirectorNote(rs.getString("director_note"));
                h.setChangedByUserName(rs.getString("changed_by_name"));
                h.setChangedByRoleName(rs.getString("changed_by_role"));
                h.setRecipientName(rs.getString("recipient_name"));
                h.setDeliveryAddress(rs.getString("delivery_address"));
                h.setContactPerson(rs.getString("contact_person"));
                h.setContactPhone(rs.getString("contact_phone"));
                h.setCreatedAt(rs.getTimestamp("created_at"));
                h.setCreatorName(rs.getString("creator_name"));
                h.setHistoryDetails(getRequestHistoryDetailsByHistoryId(h.getHistoryId()));
                list.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<RequestHistoryDetail> getRequestHistoryDetailsByHistoryId(int historyId) {
        List<RequestHistoryDetail> details = new ArrayList<>();
        String sql = "SELECT hd.*, u.unit_name " +
                    "FROM request_history_details hd " +
                    "LEFT JOIN units u ON hd.warehouse_unit_id = u.unit_id " +
                    "WHERE hd.history_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, historyId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RequestHistoryDetail detail = new RequestHistoryDetail();
                detail.setHistoryDetailId(rs.getInt("history_detail_id"));
                detail.setHistoryId(rs.getInt("history_id"));
                detail.setMaterialId(rs.getInt("material_id"));
                detail.setMaterialName(rs.getString("material_name"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setWarehouseUnitId(rs.getInt("warehouse_unit_id"));
                detail.setMaterialCondition(rs.getString("material_condition"));
                detail.setUnitName(rs.getString("unit_name"));
                details.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    public List<RequestHistory> getAllRequestHistory() {
        List<RequestHistory> list = new ArrayList<>();
        String sql = "SELECT h.*, u.full_name as changed_by_name, r.role_name as changed_by_role, req.recipient_name, req.delivery_address, req.contact_person, req.contact_phone, req.created_at, u2.full_name as creator_name " +
                    "FROM request_history h " +
                    "LEFT JOIN users u ON h.changed_by = u.user_id " +
                    "LEFT JOIN roles r ON u.role_id = r.role_id " +
                    "LEFT JOIN requests req ON h.request_id = req.request_id " +
                    "LEFT JOIN users u2 ON req.user_id = u2.user_id " +
                    "ORDER BY h.change_time DESC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RequestHistory h = new RequestHistory();
                h.setHistoryId(rs.getInt("history_id"));
                h.setRequestId(rs.getInt("request_id"));
                h.setChangedBy(rs.getInt("changed_by"));
                h.setChangeTime(rs.getTimestamp("change_time"));
                h.setOldStatus(rs.getString("old_status"));
                h.setNewStatus(rs.getString("new_status"));
                h.setAction(rs.getString("action"));
                h.setChangeReason(rs.getString("change_reason"));
                h.setDirectorNote(rs.getString("director_note"));
                h.setChangedByUserName(rs.getString("changed_by_name"));
                h.setChangedByRoleName(rs.getString("changed_by_role"));
                h.setRecipientName(rs.getString("recipient_name"));
                h.setDeliveryAddress(rs.getString("delivery_address"));
                h.setContactPerson(rs.getString("contact_person"));
                h.setContactPhone(rs.getString("contact_phone"));
                h.setCreatedAt(rs.getTimestamp("created_at"));
                h.setCreatorName(rs.getString("creator_name"));
                h.setHistoryDetails(getRequestHistoryDetailsByHistoryId(h.getHistoryId()));
                list.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getLastEmployeeChangeReason(int requestId, int directorRoleId) {
        String sql = "SELECT change_reason FROM request_history h JOIN users u ON h.changed_by = u.user_id WHERE h.request_id = ? AND u.role_id != ? AND h.change_reason IS NOT NULL AND h.change_reason <> '' ORDER BY h.change_time DESC LIMIT 1";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            stmt.setInt(2, directorRoleId); 
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("change_reason");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RequestHistory getRequestHistoryByHistoryId(int historyId) {
        String sql = "SELECT h.*, u.full_name as changed_by_name, r.role_name as changed_by_role, req.recipient_name, req.delivery_address, req.contact_person, req.contact_phone, req.created_at, u2.full_name as creator_name " +
                "FROM request_history h " +
                "LEFT JOIN users u ON h.changed_by = u.user_id " +
                "LEFT JOIN roles r ON u.role_id = r.role_id " +
                "LEFT JOIN requests req ON h.request_id = req.request_id " +
                "LEFT JOIN users u2 ON req.user_id = u2.user_id " +
                "WHERE h.history_id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, historyId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                RequestHistory h = new RequestHistory();
                h.setHistoryId(rs.getInt("history_id"));
                h.setRequestId(rs.getInt("request_id"));
                h.setChangedBy(rs.getInt("changed_by"));
                h.setChangeTime(rs.getTimestamp("change_time"));
                h.setOldStatus(rs.getString("old_status"));
                h.setNewStatus(rs.getString("new_status"));
                h.setAction(rs.getString("action"));
                h.setChangeReason(rs.getString("change_reason"));
                h.setDirectorNote(rs.getString("director_note"));
                h.setChangedByUserName(rs.getString("changed_by_name"));
                h.setChangedByRoleName(rs.getString("changed_by_role"));
                h.setRecipientName(rs.getString("recipient_name"));
                h.setDeliveryAddress(rs.getString("delivery_address"));
                h.setContactPerson(rs.getString("contact_person"));
                h.setContactPhone(rs.getString("contact_phone"));
                h.setCreatedAt(rs.getTimestamp("created_at"));
                h.setCreatorName(rs.getString("creator_name"));
                h.setHistoryDetails(getRequestHistoryDetailsByHistoryId(h.getHistoryId()));
                return h;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<RequestHistory> getRequestHistoryByUserId(int userId) {
        List<RequestHistory> list = new ArrayList<>();
        String sql = "SELECT h.*, u.full_name as changed_by_name, r.role_name as changed_by_role, req.recipient_name, req.delivery_address, req.contact_person, req.contact_phone, req.created_at, u2.full_name as creator_name " +
                    "FROM request_history h " +
                    "LEFT JOIN users u ON h.changed_by = u.user_id " +
                    "LEFT JOIN roles r ON u.role_id = r.role_id " +
                    "LEFT JOIN requests req ON h.request_id = req.request_id " +
                    "LEFT JOIN users u2 ON req.user_id = u2.user_id " +
                    "WHERE req.user_id = ? ORDER BY h.change_time DESC";
        try (Connection conn = new DBContext().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RequestHistory h = new RequestHistory();
                h.setHistoryId(rs.getInt("history_id"));
                h.setRequestId(rs.getInt("request_id"));
                h.setChangedBy(rs.getInt("changed_by"));
                h.setChangeTime(rs.getTimestamp("change_time"));
                h.setOldStatus(rs.getString("old_status"));
                h.setNewStatus(rs.getString("new_status"));
                h.setAction(rs.getString("action"));
                h.setChangeReason(rs.getString("change_reason"));
                h.setDirectorNote(rs.getString("director_note"));
                h.setChangedByUserName(rs.getString("changed_by_name"));
                h.setChangedByRoleName(rs.getString("changed_by_role"));
                h.setRecipientName(rs.getString("recipient_name"));
                h.setDeliveryAddress(rs.getString("delivery_address"));
                h.setContactPerson(rs.getString("contact_person"));
                h.setContactPhone(rs.getString("contact_phone"));
                h.setCreatedAt(rs.getTimestamp("created_at"));
                h.setCreatorName(rs.getString("creator_name"));
                h.setHistoryDetails(getRequestHistoryDetailsByHistoryId(h.getHistoryId()));
                list.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
