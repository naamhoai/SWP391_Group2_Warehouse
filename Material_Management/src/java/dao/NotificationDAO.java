package dao;

import dal.DBContext;
import model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO extends DBContext {

    // Thêm thông báo mới cho user (giám đốc)
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getUnreadNotifications(int userId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = FALSE ORDER BY created_at DESC";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Notification n = new Notification();
                n.setId(rs.getInt("id"));
                n.setUserId(rs.getInt("user_id"));
                n.setMessage(rs.getString("message"));
                n.setRead(rs.getBoolean("is_read"));
                n.setCreatedAt(rs.getTimestamp("created_at"));
                n.setRequestId(rs.getInt("request_id"));
                n.setLink(rs.getString("link"));
                list.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Đánh dấu thông báo đã đọc
    public void markAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getAllNotifications(int userId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Notification n = new Notification();
                n.setId(rs.getInt("id"));
                n.setUserId(rs.getInt("user_id"));
                n.setMessage(rs.getString("message"));
                n.setRead(rs.getBoolean("is_read"));
                n.setCreatedAt(rs.getTimestamp("created_at"));
                n.setRequestId(rs.getInt("request_id"));
                n.setLink(rs.getString("link"));
                list.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm phương thức để kiểm tra kết nối và dữ liệu
    public void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Kết nối thành công");
            
            // Kiểm tra bảng notifications
            String sql = "SELECT COUNT(*) FROM notifications";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Số lượng thông báo trong DB: " + rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NotificationDAO dao = new NotificationDAO();
        int directorId = 2;
        List<Notification> notifications = dao.getAllNotifications(directorId);
        System.out.println("Tổng số thông báo: " + notifications.size());
        for (Notification n : notifications) {
            System.out.println("ID: " + n.getId()
                + ", user_id: " + n.getUserId()
                + ", message: " + n.getMessage()
                + ", is_read: " + n.isRead()
                + ", created_at: " + n.getCreatedAt()
                + ", link: " + n.getLink());
        }
    }
}
