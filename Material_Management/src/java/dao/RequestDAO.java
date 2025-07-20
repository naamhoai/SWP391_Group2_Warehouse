package dao;

import dal.DBContext;
import model.Request;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO extends DBContext {

    public int addRequest(Request request) throws SQLException {
        String sql = "INSERT INTO requests (user_id, reason, recipient_name, delivery_address, contact_person, contact_phone, request_status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, request.getUserId());
            stmt.setString(2, request.getReason());
            stmt.setString(3, request.getRecipientName());
            stmt.setString(4, request.getDeliveryAddress());
            stmt.setString(5, request.getContactPerson());
            stmt.setString(6, request.getContactPhone());
            stmt.setString(7, request.getRequestStatus());
            stmt.setTimestamp(8, request.getCreatedAt());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<Request> getPendingRequests() throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name as requester_name "
                + "FROM requests r "
                + "LEFT JOIN users u ON r.user_id = u.user_id "
                + "WHERE r.request_status = 'Chờ duyệt' "
                + "ORDER BY r.created_at DESC";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Request request = new Request();
                request.setRequestId(rs.getInt("request_id"));
                request.setUserId(rs.getInt("user_id"));
                request.setReason(rs.getString("reason"));
                request.setRequestStatus(rs.getString("request_status"));
                request.setCreatedAt(rs.getTimestamp("created_at"));
                requests.add(request);
            }
        }
        return requests;
    }

    public Request getRequestById(int requestId) throws SQLException {
        String sql = "SELECT r.*, u.full_name as requester_name "
                + "FROM requests r "
                + "LEFT JOIN users u ON r.user_id = u.user_id "
                + "WHERE r.request_id = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Request request = new Request();
                request.setRequestId(rs.getInt("request_id"));
                request.setUserId(rs.getInt("user_id"));
                request.setReason(rs.getString("reason"));
                request.setRecipientName(rs.getString("recipient_name"));
                request.setDeliveryAddress(rs.getString("delivery_address"));
                request.setContactPerson(rs.getString("contact_person"));
                request.setContactPhone(rs.getString("contact_phone"));
                request.setRequestStatus(rs.getString("request_status"));
                request.setCreatedAt(rs.getTimestamp("created_at"));
                request.setDirectorNote(rs.getString("director_note"));
                return request;
            }
        }
        return null;
    }

    public boolean updateRequestStatus(int requestId, String status) throws SQLException {
        String sql = "UPDATE requests SET request_status = ? WHERE request_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, requestId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateStatusAndNote(int requestId, String status, String directorNote) throws SQLException {
        String sql = "UPDATE requests SET request_status = ?, director_note = ? WHERE request_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, directorNote);
            stmt.setInt(3, requestId);
            return stmt.executeUpdate() > 0;
        }
    }

    public int getRequestCreatorId(int requestId) throws SQLException {
        String sql = "SELECT user_id FROM requests WHERE request_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        }
        return -1;
    }

    public boolean updateRequestReasonAndRecipient(int requestId, String reason, String recipientName, String deliveryAddress, String contactPerson, String contactPhone) throws SQLException {
        String sql = "UPDATE requests SET reason = ?, recipient_name = ?, delivery_address = ?, contact_person = ?, contact_phone = ? WHERE request_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reason);
            stmt.setString(2, recipientName);
            stmt.setString(3, deliveryAddress);
            stmt.setString(4, contactPerson);
            stmt.setString(5, contactPhone);
            stmt.setInt(6, requestId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean createRequest(int userId, String reason, String recipientName, String deliveryAddress, String contactPerson, String contactPhone) throws SQLException {
        String sql = "INSERT INTO requests (user_id, reason, recipient_name, delivery_address, contact_person, contact_phone, request_status, created_at) VALUES (?, ?, ?, ?, ?, ?, 'Chờ duyệt', NOW())";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, reason);
            stmt.setString(3, recipientName);
            stmt.setString(4, deliveryAddress);
            stmt.setString(5, contactPerson);
            stmt.setString(6, contactPhone);
            return stmt.executeUpdate() > 0;
        }
    }

   
    public int getStaffId() {
        String sql = "SELECT u.user_id FROM users u "
                + "JOIN roles r ON u.role_id = r.role_id "
                + "WHERE r.role_name = 'Nhân viên công ty' "
                + "LIMIT 1";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getDirectorId() {
        String sql = "SELECT u.user_id FROM users u "
                + "JOIN roles r ON u.role_id = r.role_id "
                + "WHERE r.role_name = 'Giám đốc' "
                + "LIMIT 1";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getWarehouseStaffId() {
        String sql = "SELECT u.user_id FROM users u "
                + "JOIN roles r ON u.role_id = r.role_id "
                + "WHERE r.role_name = 'Nhân viên kho' "
                + "LIMIT 1";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Request> getFilteredRequests(
            Integer userId, String status, String startDate, String endDate, String projectName,
            String sortBy, String order, int page, int pageSize
    ) {
        List<Request> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT r.*, u.full_name as creator_name, ro.role_name as creator_role "
                + "FROM requests r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "JOIN roles ro ON u.role_id = ro.role_id "
                + "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (userId != null) {
            sql.append(" AND r.user_id = ?");
            params.add(userId);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND r.request_status = ?");
            params.add(status);
        }
        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND r.created_at >= ?");
            params.add(startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append(" AND r.created_at <= ?");
            params.add(endDate + " 23:59:59");
        }
        if (projectName != null && !projectName.isEmpty()) {
            sql.append(" AND r.recipient_name LIKE ?");
            params.add("%" + projectName + "%");
        }

        String sortColumn;
        switch (sortBy != null ? sortBy : "") {
            case "name":
                sortColumn = "r.recipient_name";
                break;
            case "date":
                sortColumn = "r.created_at";
                break;
            default:
                sortColumn = "r.request_id";
        }
        String sortOrder = "desc".equalsIgnoreCase(order) ? "DESC" : "ASC";
        sql.append(" ORDER BY ").append(sortColumn).append(" ").append(sortOrder);

        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection conn = new DBContext().getConnection(); PreparedStatement st = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Request r = new Request();
                    r.setRequestId(rs.getInt("request_id"));
                    r.setReason(rs.getString("reason"));
                    r.setRequestStatus(rs.getString("request_status"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    r.setDirectorNote(rs.getString("director_note"));
                    r.setCreatorName(rs.getString("creator_name"));
                    r.setCreatorRole(rs.getString("creator_role"));
                    r.setRecipientName(rs.getString("recipient_name"));
                    requests.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public int countFilteredRequests(Integer userId, String status, String startDate, String endDate, String projectName) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM requests r WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (userId != null) {
            sql.append(" AND r.user_id = ?");
            params.add(userId);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND r.request_status = ?");
            params.add(status);
        }
        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND r.created_at >= ?");
            params.add(startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append(" AND r.created_at <= ?");
            params.add(endDate + " 23:59:59");
        }
        if (projectName != null && !projectName.isEmpty()) {
            sql.append(" AND r.recipient_name LIKE ?");
            params.add("%" + projectName + "%");
        }

        try (Connection conn = new DBContext().getConnection(); PreparedStatement st = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public List<Request> getApprovedRequestsWithPendingExport(String projectName, Timestamp fromDate, Timestamp toDate, String sortBy, String sortDir, int page, int pageSize) throws SQLException {
        List<Request> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT r.*, u.full_name as creator_name "
                + "FROM requests r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "WHERE r.request_status = 'Đã duyệt' "
                + "AND ( "
                + "    NOT EXISTS (SELECT 1 FROM export_forms ef WHERE ef.request_id = r.request_id) "
                + "    OR EXISTS ( "
                + "        SELECT 1 "
                + "        FROM request_details rd "
                + "        LEFT JOIN ( "
                + "            SELECT ef.request_id, em.material_id, SUM(em.quantity) as total_exported "
                + "            FROM export_forms ef "
                + "            JOIN export_materials em ON ef.export_id = em.export_id "
                + "            GROUP BY ef.request_id, em.material_id "
                + "        ) exported ON exported.request_id = rd.request_id AND exported.material_id = rd.material_id "
                + "        WHERE rd.request_id = r.request_id "
                + "        AND (rd.quantity > IFNULL(exported.total_exported, 0)) "
                + "    ) "
                + ") "
        );

        // Điều kiện tìm kiếm
        List<Object> params = new ArrayList<>();
        if (projectName != null && !projectName.trim().isEmpty()) {
            sql.append(" AND r.recipient_name LIKE ? ");
            params.add("%" + projectName.trim() + "%");
        }
        if (fromDate != null) {
            sql.append(" AND r.created_at >= ? ");
            params.add(fromDate);
        }
        if (toDate != null) {
            sql.append(" AND r.created_at <= ? ");
            params.add(toDate);
        }

        // Sắp xếp
        String sortField = "created_at";
        if ("request_id".equals(sortBy) || "recipient_name".equals(sortBy) || "created_at".equals(sortBy)) {
            sortField = sortBy;
        }
        String sortOrder = "DESC";
        if ("ASC".equalsIgnoreCase(sortDir)) {
            sortOrder = "ASC";
        }
        sql.append(" ORDER BY r.").append(sortField).append(" ").append(sortOrder);

        // Phân trang
        sql.append(" LIMIT ? OFFSET ? ");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            for (Object param : params) {
                stmt.setObject(idx++, param);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Request r = new Request();
                r.setRequestId(rs.getInt("request_id"));
                r.setReason(rs.getString("reason"));
                r.setRequestStatus(rs.getString("request_status"));
                r.setCreatedAt(rs.getTimestamp("created_at"));
                r.setRecipientName(rs.getString("recipient_name"));
                r.setDeliveryAddress(rs.getString("delivery_address"));
                r.setCreatorName(rs.getString("creator_name"));
                list.add(r);
            }
        }
        return list;
    }

    public int countApprovedRequestsWithPendingExport(String projectName, Timestamp fromDate, Timestamp toDate) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(DISTINCT r.request_id) "
                + "FROM requests r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "WHERE r.request_status = 'Đã duyệt' "
                + "AND ( "
                + "    NOT EXISTS (SELECT 1 FROM export_forms ef WHERE ef.request_id = r.request_id) "
                + "    OR EXISTS ( "
                + "        SELECT 1 "
                + "        FROM request_details rd "
                + "        LEFT JOIN ( "
                + "            SELECT ef.request_id, em.material_id, SUM(em.quantity) as total_exported "
                + "            FROM export_forms ef "
                + "            JOIN export_materials em ON ef.export_id = em.export_id "
                + "            GROUP BY ef.request_id, em.material_id "
                + "        ) exported ON exported.request_id = rd.request_id AND exported.material_id = rd.material_id "
                + "        WHERE rd.request_id = r.request_id "
                + "        AND (rd.quantity > IFNULL(exported.total_exported, 0)) "
                + "    ) "
                + ") "
        );

        List<Object> params = new ArrayList<>();
        if (projectName != null && !projectName.trim().isEmpty()) {
            sql.append(" AND r.recipient_name LIKE ? ");
            params.add("%" + projectName.trim() + "%");
        }
        if (fromDate != null) {
            sql.append(" AND r.created_at >= ? ");
            params.add(fromDate);
        }
        if (toDate != null) {
            sql.append(" AND r.created_at <= ? ");
            params.add(toDate);
        }

        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            for (Object param : params) {
                stmt.setObject(idx++, param);
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Đếm tổng số yêu cầu (tức là số lượng xuất kho)
    public int countAllRequests() {
        String sql = "SELECT COUNT(*) FROM requests";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countPendingDeliveries() {
        String sql = "SELECT COUNT(*) FROM delivery WHERE status = 'Chờ giao'";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<java.util.Map<String, Object>> getRecentTransactions(int limit) {
        List<java.util.Map<String, Object>> transactions = new ArrayList<>();
        String sql = "SELECT r.request_id, r.recipient_name, r.request_status, r.created_at, u.full_name as user_name " +
                "FROM requests r " +
                "LEFT JOIN users u ON r.user_id = u.user_id " +
                "ORDER BY r.created_at DESC LIMIT ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                map.put("request_id", rs.getInt("request_id"));
                map.put("recipient_name", rs.getString("recipient_name"));
                map.put("request_status", rs.getString("request_status"));
                map.put("created_at", rs.getTimestamp("created_at"));
                map.put("user_name", rs.getString("user_name"));
                transactions.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // Lấy số lượng yêu cầu mua theo tháng
    public List<model.MonthStat> getRequestCountByMonth() throws SQLException {
        List<model.MonthStat> list = new ArrayList<>();
        String sql = "SELECT DATE_FORMAT(created_at, '%Y-%m') AS month, COUNT(*) AS count " +
                    "FROM requests " +
                    "GROUP BY month " +
                    "ORDER BY month DESC " +
                    "LIMIT 12"; // Lấy 12 tháng gần nhất
        
        try (Connection conn = new DBContext().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new model.MonthStat(
                    rs.getString("month"), 
                    rs.getInt("count"), 
                    0
                ));
            }
        }
        return list;
    }

    // Lấy tổng giá trị mua hàng theo tháng (tính từ request_details)
    public List<model.MonthStat> getPurchaseValueByMonth() throws SQLException {
        List<model.MonthStat> list = new ArrayList<>();
        String sql = "SELECT DATE_FORMAT(r.created_at, '%Y-%m') AS month, " +
                    "SUM(rd.quantity * m.price) AS value " +
                    "FROM requests r " +
                    "JOIN request_details rd ON r.request_id = rd.request_id " +
                    "JOIN materials m ON rd.material_id = m.material_id " +
                    "WHERE r.request_status IN ('Đã duyệt', 'Đã xuất kho') " +
                    "GROUP BY month " +
                    "ORDER BY month DESC " +
                    "LIMIT 12"; // Lấy 12 tháng gần nhất
        
        try (Connection conn = new DBContext().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new model.MonthStat(
                    rs.getString("month"), 
                    0, 
                    rs.getLong("value")
                ));
            }
        }
        return list;
    }

}
