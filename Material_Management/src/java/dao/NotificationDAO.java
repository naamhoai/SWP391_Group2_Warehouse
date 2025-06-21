package dao;

import dal.DBContext;
import model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO extends DBContext {

    public void addNotification(int userId, String message, Integer requestId) {
        String sql = "INSERT INTO notifications (user_id, message, request_id, link) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, message);
            if (requestId != null) {
                stmt.setInt(3, requestId);
                stmt.setString(4, "viewRequestDetail?requestId=" + requestId);
            } else {
                stmt.setNull(3, Types.INTEGER);
                stmt.setNull(4, Types.VARCHAR);
            }
            stmt.executeUpdate();
            System.out.println("Debug - Added notification for userId: " + userId + ", message: " + message);
        } catch (SQLException e) {
            System.out.println("Debug - Error adding notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Notification> getAllNotifications(int userId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        System.out.println("Debug - Getting all notifications for userId: " + userId);
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
                Notification n = new Notification();
                n.setId(rs.getInt("id"));
                n.setUserId(rs.getInt("user_id"));
                n.setMessage(rs.getString("message"));
                n.setRead(rs.getBoolean("is_read"));
                n.setCreatedAt(rs.getTimestamp("created_at"));
                n.setRequestId(rs.getInt("request_id"));
                n.setLink(rs.getString("link"));
                list.add(n);
                System.out.println("Debug - Found notification: ID=" + n.getId() + ", Message=" + n.getMessage());
            }
            System.out.println("Debug - Total notifications found: " + count);
        } catch (SQLException e) {
            System.out.println("Debug - Error getting all notifications: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public void markAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
            System.out.println("Debug - Marked notification as read: " + notificationId);
        } catch (SQLException e) {
            System.out.println("Debug - Error marking notification as read: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
