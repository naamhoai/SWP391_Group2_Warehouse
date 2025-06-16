package dao;

import dal.DBContext;
import model.Request;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.RequestDetail;

public class RequestDAO {

    // Thêm yêu cầu mới
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


    // Lấy danh sách yêu cầu chờ duyệt
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

    // Lấy chi tiết yêu cầu theo request_id
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

    // Cập nhật trạng thái yêu cầu
    public boolean updateRequestStatus(int requestId, String status) throws SQLException {
        String sql = "UPDATE requests SET request_status = ? WHERE request_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, requestId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Cập nhật trạng thái và lý do phê duyệt/từ chối
    public boolean updateStatusAndNote(int requestId, String status, String directorNote) throws SQLException {
        String sql = "UPDATE requests SET request_status = ?, director_note = ? WHERE request_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, directorNote);
            stmt.setInt(3, requestId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Lấy user_id của người tạo yêu cầu
    public int getRequestCreatorId(int requestId) throws SQLException {
        String sql = "SELECT user_id FROM requests WHERE request_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        }
        return -1; // Không tìm thấy
    }

    public boolean updateRequestReason(int requestId, String reason) throws SQLException {
        String sql = "UPDATE requests SET reason = ? WHERE request_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reason);
            stmt.setInt(2, requestId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Cập nhật loại yêu cầu và lý do
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
        // Lấy ID của nhân viên kho từ database
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
        return -1; // Trả về -1 nếu không tìm thấy
    }
    // Trong RequestDAO

    public int createPurchaseRequest(int userId, String reason) throws SQLException {
        String sql = "INSERT INTO requests (request_type, user_id, reason, request_status, created_at) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Mua Vật Tư");
            stmt.setInt(2, userId);
            stmt.setString(3, reason);
            stmt.setString(4, "Pending");
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Trả về request_id vừa tạo
            }
        }
        return -1;
    }

    public List<Request> getAllRequests(String sort) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name as creator_name, ro.role_name as creator_role "
                + "FROM requests r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "JOIN roles ro ON u.role_id = ro.role_id "
                + "ORDER BY r.request_id " + ("desc".equalsIgnoreCase(sort) ? "DESC" : "ASC");

        try (Connection conn = new DBContext().getConnection(); PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public List<Request> getRequestsByUserId(int userId, String sort) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name as creator_name, ro.role_name as creator_role "
                + "FROM requests r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "JOIN roles ro ON u.role_id = ro.role_id "
                + "WHERE r.user_id = ? "
                + "ORDER BY r.request_id " + ("desc".equalsIgnoreCase(sort) ? "DESC" : "ASC");

        try (Connection conn = new DBContext().getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, userId);
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

    public List<Request> filterRequests(String status, String date, String sort) {
        List<Request> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT r.*, u.full_name as creator_name, ro.role_name as creator_role "
                + "FROM requests r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "JOIN roles ro ON u.role_id = ro.role_id "
                + "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (status != null && !status.isEmpty()) {
            sql.append(" AND r.request_status = ?");
            params.add(status);
        }
        if (date != null && !date.isEmpty()) {
            sql.append(" AND DATE(r.created_at) = ?");
            params.add(date);
        }
        sql.append(" ORDER BY r.request_id " + ("desc".equalsIgnoreCase(sort) ? "DESC" : "ASC"));

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
}
