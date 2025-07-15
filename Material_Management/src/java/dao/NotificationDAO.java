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
        RequestDAO requestDAO = new RequestDAO();

        for (Notification noti : allNotifications) {
            try {
                String msg = noti.getMessage() != null ? noti.getMessage().toLowerCase() : "";

                if (noti.getMessage() != null
                        && noti.getMessage().toLowerCase().startsWith("yêu cầu #")
                        && noti.getMessage().toLowerCase().contains("thiếu:")) {
                    shortageNotifications.add(noti);
                    continue;
                }

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

    public void addCancelExportNotification(int userId, int requestId, String cancelReason) {
        String message = "Đơn xuất kho đã được huỷ. Lý do: " + cancelReason;
        String link = "/viewRequestDetail?requestId=" + requestId;
        String sql = "INSERT INTO notifications (user_id, message, request_id, link) VALUES (?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, message);
            stmt.setInt(3, requestId);
            stmt.setString(4, link);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCancelExportNotificationToMultipleUsers(int requestId, String cancelReason, int warehouseStaffId) {
        try {
            RequestDAO requestDAO = new RequestDAO();
            Request req = requestDAO.getRequestById(requestId);
            if (req == null) {
                return;
            }

            UserDAO userDAO = new UserDAO();
            Integer directorId = userDAO.getDirectorId();
            String message = "Đơn xuất kho đã được huỷ bởi nhân viên kho. Lý do: " + cancelReason;

            if (req.getUserId() != warehouseStaffId) {
                addNotification(req.getUserId(), message, requestId);
            }
            if (directorId != null && directorId != warehouseStaffId) {
                addNotification(directorId, message, requestId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
