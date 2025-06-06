package dao;

import model.Request;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {
    private Connection conn;
    
    public RequestDAO() {
        // Initialize database connection
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=MaterialManagement;encrypt=true;trustServerCertificate=true";
            String username = "sa";
            String password = "12345";
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Request> getPendingRequests() {
        List<Request> requests = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Requests WHERE status = 'PENDING' ORDER BY requestTime DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Request request = new Request();
                request.setId(rs.getInt("id"));
                request.setType(rs.getString("type"));
                request.setContent(rs.getString("content"));
                request.setRequester(rs.getString("requester"));
                request.setDepartment(rs.getString("department"));
                request.setRequestTime(rs.getTimestamp("requestTime"));
                request.setStatus(rs.getString("status"));
                request.setComment(rs.getString("comment"));
                request.setApproverId(rs.getInt("approverId"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public List<Request> getRecentRequests(int limit) {
        List<Request> requests = new ArrayList<>();
        try {
            String sql = "SELECT TOP (?) * FROM Requests ORDER BY requestTime DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Request request = new Request();
                request.setId(rs.getInt("id"));
                request.setType(rs.getString("type"));
                request.setContent(rs.getString("content"));
                request.setRequester(rs.getString("requester"));
                request.setDepartment(rs.getString("department"));
                request.setRequestTime(rs.getTimestamp("requestTime"));
                request.setStatus(rs.getString("status"));
                request.setComment(rs.getString("comment"));
                request.setApproverId(rs.getInt("approverId"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public List<Request> getRequestHistory() {
        List<Request> requests = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Requests WHERE status != 'PENDING' ORDER BY requestTime DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Request request = new Request();
                request.setId(rs.getInt("id"));
                request.setType(rs.getString("type"));
                request.setContent(rs.getString("content"));
                request.setRequester(rs.getString("requester"));
                request.setDepartment(rs.getString("department"));
                request.setRequestTime(rs.getTimestamp("requestTime"));
                request.setStatus(rs.getString("status"));
                request.setComment(rs.getString("comment"));
                request.setApproverId(rs.getInt("approverId"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public int getPendingRequestCount() {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Requests WHERE status = 'PENDING'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public void approveRequest(int requestId, int approverId, String comment) {
        try {
            String sql = "UPDATE Requests SET status = 'APPROVED', approverId = ?, comment = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, approverId);
            stmt.setString(2, comment);
            stmt.setInt(3, requestId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rejectRequest(int requestId, int approverId, String comment) {
        try {
            String sql = "UPDATE Requests SET status = 'REJECTED', approverId = ?, comment = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, approverId);
            stmt.setString(2, comment);
            stmt.setInt(3, requestId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 