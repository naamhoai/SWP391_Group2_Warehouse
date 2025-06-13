package dao;

import dal.DBContext;
import model.Request;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {
    
    // Thêm yêu cầu mới
    public int addRequest(Request request) throws SQLException {
        String sql = "INSERT INTO requests (request_type, user_id, reason, request_status, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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

    // Lấy danh sách yêu cầu theo user_id
    public List<Request> getRequestsByUserId(int userId) throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name as requester_name " +
                    "FROM requests r " +
                    "LEFT JOIN users u ON r.user_id = u.user_id " +
                    "WHERE r.user_id = ? " +
                    "ORDER BY r.created_at DESC";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
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

    // Lấy danh sách yêu cầu chờ duyệt
    public List<Request> getPendingRequests() throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name as requester_name " +
                    "FROM requests r " +
                    "LEFT JOIN users u ON r.user_id = u.user_id " +
                    "WHERE r.request_status = 'Pending' " +
                    "ORDER BY r.created_at DESC";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String sql = "SELECT r.*, u.full_name as requester_name " +
                    "FROM requests r " +
                    "LEFT JOIN users u ON r.user_id = u.user_id " +
                    "WHERE r.request_id = ?";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
                return request;
            }
        }
        return null;
    }

    // Cập nhật trạng thái yêu cầu
    public boolean updateRequestStatus(int requestId, String status) throws SQLException {
        String sql = "UPDATE requests SET request_status = ? WHERE request_id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, requestId);
            return stmt.executeUpdate() > 0;
        }
    }
}