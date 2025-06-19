package dao;

import dal.DBContext;
import model.Request;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.RequestDetail;

public class RequestDAO extends DBContext {

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

    public int getDirectorId() {
        // Lấy ID của giám đốc từ database
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
        return -1; // Trả về -1 nếu không tìm thấy
    }

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

    public List<Request> filterRequests(String status, String date, String sort, String requestType, int page, int pageSize) throws SQLException {
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
        if (requestType != null && !requestType.isEmpty()) {
            sql.append(" AND r.request_type = ?");
            params.add(requestType);
        }

        sql.append(" ORDER BY r.request_id ").append(sort != null && sort.equals("desc") ? "DESC" : "ASC");
        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            System.out.println("SQL Query: " + sql.toString()); // Debug
            System.out.println("Parameters: " + params); // Debug

            try (ResultSet rs = stmt.executeQuery()) {
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
            System.out.println("Error in filterRequests: " + e.getMessage());
            throw e;
        }
        return requests;
    }

    public int getTotalFilteredRequests(String status, String date, String requestType) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM requests r WHERE 1=1"
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
        if (requestType != null && !requestType.isEmpty()) {
            sql.append(" AND r.request_type = ?");
            params.add(requestType);
        }

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            System.out.println("Count SQL Query: " + sql.toString()); // Debug
            System.out.println("Count Parameters: " + params); // Debug

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Request> searchRequests(String searchTerm, int page, int pageSize) throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name as creator_name, ro.role_name as creator_role "
                + "FROM requests r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "JOIN roles ro ON u.role_id = ro.role_id "
                + "WHERE r.request_type LIKE ? OR r.reason LIKE ? OR u.full_name LIKE ? "
                + "ORDER BY r.request_id DESC LIMIT ? OFFSET ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setInt(4, pageSize);
            stmt.setInt(5, (page - 1) * pageSize);

            System.out.println("Search SQL Query: " + sql); // Debug
            System.out.println("Search Term: " + searchTerm); // Debug
            System.out.println("Search Pattern: " + searchPattern); // Debug

            try (ResultSet rs = stmt.executeQuery()) {
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
            System.out.println("Error in searchRequests: " + e.getMessage());
            throw e;
        }
        return requests;
    }

    public int getTotalSearchResults(String searchTerm) throws SQLException {
        String sql = "SELECT COUNT(*) FROM requests r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "WHERE r.request_type LIKE ? OR r.reason LIKE ? OR u.full_name LIKE ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Request> getRequestsByUserId(int userId, String sort, int page, int pageSize) throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name as creator_name, ro.role_name as creator_role "
                + "FROM requests r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "JOIN roles ro ON u.role_id = ro.role_id "
                + "WHERE r.user_id = ? "
                + "ORDER BY r.request_id " + (sort.equals("desc") ? "DESC" : "ASC")
                + " LIMIT ? OFFSET ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, pageSize);
            stmt.setInt(3, (page - 1) * pageSize);

            ResultSet rs = stmt.executeQuery();
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
        return requests;
    }

    public int getTotalRequestsByUserId(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM requests WHERE user_id = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Tìm kiếm requests của một user cụ thể
    public List<Request> searchUserRequests(int userId, String searchTerm, int page, int pageSize) throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name as creator_name, ro.role_name as creator_role "
                + "FROM requests r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "JOIN roles ro ON u.role_id = ro.role_id "
                + "WHERE r.user_id = ? AND (r.request_type LIKE ? OR r.reason LIKE ? OR u.full_name LIKE ?) "
                + "ORDER BY r.request_id DESC LIMIT ? OFFSET ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setInt(1, userId);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            stmt.setInt(5, pageSize);
            stmt.setInt(6, (page - 1) * pageSize);

            System.out.println("User Search SQL Query: " + sql); // Debug
            System.out.println("User ID: " + userId); // Debug
            System.out.println("Search Pattern: " + searchPattern); // Debug

            try (ResultSet rs = stmt.executeQuery()) {
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
        }
        return requests;
    }

    // Đếm tổng số kết quả tìm kiếm của một user
    public int getTotalUserSearchResults(int userId, String searchTerm) throws SQLException {
        String sql = "SELECT COUNT(*) FROM requests r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "WHERE r.user_id = ? AND (r.request_type LIKE ? OR r.reason LIKE ? OR u.full_name LIKE ?)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setInt(1, userId);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Lọc requests của một user cụ thể
    public List<Request> filterUserRequests(int userId, String status, String date, String sort, String requestType, int page, int pageSize) throws SQLException {
        List<Request> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT r.*, u.full_name as creator_name, ro.role_name as creator_role "
            + "FROM requests r "
            + "JOIN users u ON r.user_id = u.user_id "
            + "JOIN roles ro ON u.role_id = ro.role_id "
            + "WHERE r.user_id = ?"
        );
        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (status != null && !status.isEmpty()) {
            sql.append(" AND r.request_status = ?");
            params.add(status);
        }
        if (date != null && !date.isEmpty()) {
            sql.append(" AND DATE(r.created_at) = ?");
            params.add(date);
        }
        if (requestType != null && !requestType.isEmpty()) {
            sql.append(" AND r.request_type = ?");
            params.add(requestType);
        }

        sql.append(" ORDER BY r.request_id ").append(sort != null && sort.equals("desc") ? "DESC" : "ASC");
        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            System.out.println("User Filter SQL Query: " + sql.toString()); // Debug
            System.out.println("Parameters: " + params); // Debug

            try (ResultSet rs = stmt.executeQuery()) {
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
        }
        return requests;
    }

    // Đếm tổng số kết quả lọc của một user
    public int getTotalFilteredUserRequests(int userId, String status, String date, String requestType) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM requests r WHERE r.user_id = ?"
        );
        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (status != null && !status.isEmpty()) {
            sql.append(" AND r.request_status = ?");
            params.add(status);
        }
        if (date != null && !date.isEmpty()) {
            sql.append(" AND DATE(r.created_at) = ?");
            params.add(date);
        }
        if (requestType != null && !requestType.isEmpty()) {
            sql.append(" AND r.request_type = ?");
            params.add(requestType);
        }

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            System.out.println("Count User Filter SQL Query: " + sql.toString()); // Debug
            System.out.println("Count Parameters: " + params); // Debug

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Lấy tổng số requests
    public int getTotalRequests() throws SQLException {
        String sql = "SELECT COUNT(*) FROM requests";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Lấy tất cả requests có phân trang
    public List<Request> getAllRequests(String sort, int page, int pageSize) throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name as creator_name, ro.role_name as creator_role "
                + "FROM requests r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "JOIN roles ro ON u.role_id = ro.role_id "
                + "ORDER BY r.request_id " + (sort != null && sort.equals("desc") ? "DESC" : "ASC")
                + " LIMIT ? OFFSET ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (page - 1) * pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
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
        }
        return requests;
    }

    public List<Request> getNewerRequestsFromOriginal(int originalRequestId) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name as creator_name, ro.role_name as creator_role " +
                    "FROM requests r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "JOIN roles ro ON u.role_id = ro.role_id " +
                    "WHERE r.original_request_id = ? AND r.created_at > (SELECT created_at FROM requests WHERE request_id = ?)";
        
        try (Connection conn = new DBContext().getConnection(); 
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, originalRequestId);
            st.setInt(2, originalRequestId);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                Request request = new Request();
                request.setRequestId(rs.getInt("request_id"));
                request.setRequestType(rs.getString("request_type"));
                request.setUserId(rs.getInt("user_id"));
                request.setReason(rs.getString("reason"));
                request.setRequestStatus(rs.getString("request_status"));
                request.setCreatedAt(rs.getTimestamp("created_at"));
                request.setDirectorNote(rs.getString("director_note"));
                request.setCreatorName(rs.getString("creator_name"));
                request.setCreatorRole(rs.getString("creator_role"));
                requests.add(request);
            }
        } catch (SQLException e) {
            System.out.println("Error when getting newer requests: " + e.getMessage());
        }
        
        return requests;
    }
}
