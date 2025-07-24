package dao;

import dal.DBContext;
import model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import model.Request;

public class NotificationDAO {

    public void addNotification(int userId, String message, Integer requestId) {
        String sql = "INSERT INTO notifications (user_id, message, request_id, link) VALUES (?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, message);
            if (requestId != null) {
                stmt.setInt(3, requestId);
                stmt.setString(4, "/viewRequestDetail?requestId=" + requestId);
            } else {
                stmt.setNull(3, Types.INTEGER);
                stmt.setNull(4, Types.VARCHAR);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPurchaseOrderNotification(int userId, String message, Integer purchaseOrderId) {
        String sql = "INSERT INTO notifications (user_id, message, request_id, link, notification_type) VALUES (?, ?, ?, ?, 'purchase_order')";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, message);
            if (purchaseOrderId != null) {
                stmt.setInt(3, purchaseOrderId);
                stmt.setString(4, "/purchaseOrderDetail?id=" + purchaseOrderId);
            } else {
                stmt.setNull(3, Types.INTEGER);
                stmt.setNull(4, Types.VARCHAR);
            }
            int result = stmt.executeUpdate();
            System.out.println("=== DEBUG: NotificationDAO - Inserted " + result + " purchase order notification for user " + userId + ", message: " + message);
        } catch (SQLException e) {
            System.out.println("=== DEBUG: NotificationDAO - Error inserting purchase order notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Notification> getAllNotifications(int userId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Notification n = new Notification();
                n.setId(rs.getInt("id"));
                n.setUserId(rs.getInt("user_id"));
                n.setMessage(rs.getString("message"));
                n.setRead(rs.getBoolean("is_read"));
                n.setCreatedAt(rs.getTimestamp("created_at"));
                int reqId = rs.getInt("request_id");
                n.setRequestId(rs.wasNull() ? null : reqId);
                n.setLink(rs.getString("link"));
                n.setNotificationType(rs.getString("notification_type"));
                list.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Notification> getDirectorNotifications(int userId) {
        List<Notification> allNotifications = getAllNotifications(userId);
        Map<Integer, Notification> latestNotiByRequest = new LinkedHashMap<>();
        Map<Integer, Notification> resendNotiByRequest = new LinkedHashMap<>();
        List<Notification> shortageNotifications = new ArrayList<>();
        List<Notification> purchaseOrderNotifications = new ArrayList<>();
        RequestDAO requestDAO = new RequestDAO();

        for (Notification noti : allNotifications) {
            try {
                String msg = noti.getMessage() != null ? noti.getMessage().toLowerCase() : "";

                // Kiểm tra thông báo về purchase order (sử dụng notification_type)
                if (noti.getMessage() != null && noti.getMessage().toLowerCase().contains("đơn mua")) {
                    purchaseOrderNotifications.add(noti);
                    continue;
                }

                // Kiểm tra thông báo về thiếu vật tư
                if (noti.getMessage() != null
                        && noti.getMessage().toLowerCase().startsWith("yêu cầu #")
                        && noti.getMessage().toLowerCase().contains("thiếu:")) {
                    shortageNotifications.add(noti);
                    continue;
                }

                // Kiểm tra thông báo về request
                Request req = requestDAO.getRequestById(noti.getRequestId());
                if (req != null && "Chờ duyệt".equals(req.getRequestStatus())) {
                    if (msg.contains("gửi lại")) {
                        resendNotiByRequest.put(noti.getRequestId(), noti);
                    } else if (!latestNotiByRequest.containsKey(noti.getRequestId())) {
                        latestNotiByRequest.put(noti.getRequestId(), noti);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Integer reqId : resendNotiByRequest.keySet()) {
            if (!latestNotiByRequest.containsKey(reqId)) {
                latestNotiByRequest.put(reqId, resendNotiByRequest.get(reqId));
            }
        }

        List<Notification> result = new ArrayList<>();
        result.addAll(shortageNotifications);
        result.addAll(purchaseOrderNotifications);
        result.addAll(latestNotiByRequest.values());

        return result;
    }

    public List<Notification> getWarehouseStaffNotifications(int userId) {
        List<Notification> allNotifications = getAllNotifications(userId);
        ExportFormDAO exportDAO = new ExportFormDAO();
        List<Notification> filtered = allNotifications.stream()
                .filter(noti -> {
                    String msg = noti.getMessage() != null ? noti.getMessage().toLowerCase() : "";
                    return msg.contains("đã được phê duyệt");
                })
                .filter(noti -> {
                    try {
                        int requestId = noti.getRequestId();
                        boolean hasExport = exportDAO.existsExportFormByRequestId(requestId);
                        return !hasExport;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
        return filtered;
    }

    public List<Notification> getStaffNotifications(int userId) {
        List<Notification> allNotifications = getAllNotifications(userId);
        Map<Integer, Notification> latestNotiByRequest = new LinkedHashMap<>();
        Map<Integer, Notification> resendNotiByRequest = new LinkedHashMap<>();
        Map<Integer, Notification> shortageNotiByRequest = new LinkedHashMap<>();
        RequestDAO requestDAO = new RequestDAO();

        for (Notification noti : allNotifications) {
            try {
                Request req = requestDAO.getRequestById(noti.getRequestId());
                String msg = noti.getMessage() != null ? noti.getMessage().toLowerCase() : "";
                if (noti.getMessage() != null
                        && noti.getMessage().toLowerCase().startsWith("yêu cầu #")
                        && noti.getMessage().toLowerCase().contains("thiếu:")) {
                    shortageNotiByRequest.put(noti.getRequestId(), noti);
                } else if (req != null && req.getUserId() == userId && "Từ chối".equals(req.getRequestStatus())) {
                    if (msg.contains("gửi lại")) {
                        resendNotiByRequest.put(noti.getRequestId(), noti);
                    } else if (!latestNotiByRequest.containsKey(noti.getRequestId())) {
                        latestNotiByRequest.put(noti.getRequestId(), noti);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Integer reqId : resendNotiByRequest.keySet()) {
            latestNotiByRequest.put(reqId, resendNotiByRequest.get(reqId));
        }
        latestNotiByRequest.putAll(shortageNotiByRequest);

        return new ArrayList<>(latestNotiByRequest.values());
    }

    public void markAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNotificationToRole(int roleId, String message, Integer requestId) {
        String sql = "SELECT user_id FROM users WHERE role_id = ?";
        try (Connection conn = new dal.DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    addNotification(userId, message, requestId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
}
