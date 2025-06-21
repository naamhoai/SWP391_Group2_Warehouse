package dao;

import dal.DBContext;
import model.Request;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO extends DBContext {

    public int addRequest(Request request) throws SQLException {
        String sql = "INSERT INTO requests (request_type, user_id, reason, request_status, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, request.getRequestType());
            stmt.setInt(2, request.getUserId());
            stmt.setString(3, request.getReason());
            stmt.setString(4, request.getRequestStatus());
            stmt.setTimestamp(5, request.getCreatedAt());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Trả về request_id vừa tạo
            }
        }
        return -1;
    }

    public List<Request> getPendingRequests() throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name as requester_name "
                + "FROM requests r "
                + "LEFT JOIN users u ON r.user_id = u.user_id "
                + "WHERE r.request_status = 'Pending' "
                + "ORDER BY r.created_at DESC";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Request request = new Request();
                request.setRequestId(rs.getInt("request_id"));
                request.setRequestType(rs.getString("request_type"));
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
                request.setRequestType(rs.getString("request_type"));
                request.setUserId(rs.getInt("user_id"));
                request.setReason(rs.getString("reason"));
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

    public boolean updateRequestTypeAndReason(int requestId, String requestType, String reason) throws SQLException {
        String sql = "UPDATE requests SET request_type = ?, reason = ? WHERE request_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, requestType);
            stmt.setString(2, reason);
            stmt.setInt(3, requestId);
            return stmt.executeUpdate() > 0;
        }
    }

    public int getWarehouseStaffId() {
        String sql = "SELECT u.user_id FROM users u "
                + "JOIN roles r ON u.role_id = r.role_id "
                + "WHERE r.role_name = 'Warehouse Staff' "
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
                + "WHERE r.role_name = 'Director' "
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

    public List<Request> getFilteredRequests(Integer userId, String status, String requestType, String startDate, String endDate, String sort, int page, int pageSize) {
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
        if (requestType != null && !requestType.isEmpty()) {
            sql.append(" AND r.request_type = ?");
            params.add(requestType);
        }
        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND r.created_at >= ?");
            params.add(startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append(" AND r.created_at <= ?");
            params.add(endDate + " 23:59:59");
        }

        sql.append(" ORDER BY r.request_id ").append("desc".equalsIgnoreCase(sort) ? "DESC" : "ASC");
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
                    r.setRequestType(rs.getString("request_type"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setReason(rs.getString("reason"));
                    r.setRequestStatus(rs.getString("request_status"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    r.setDirectorNote(rs.getString("director_note"));
                    r.setCreatorName(rs.getString("creator_name"));
                    r.setCreatorRole(rs.getString("creator_role"));
                    requests.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public int countFilteredRequests(Integer userId, String status, String requestType, String startDate, String endDate) {
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
        if (requestType != null && !requestType.isEmpty()) {
            sql.append(" AND r.request_type = ?");
            params.add(requestType);
        }
        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND r.created_at >= ?");
            params.add(startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append(" AND r.created_at <= ?");
            params.add(endDate + " 23:59:59");
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
}
