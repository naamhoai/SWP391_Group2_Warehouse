package dao;

import dal.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Delivery;

public class DeliveryDAO {

    public DeliveryDAO() {

    }

    // Lấy tất cả giao hàng, KHÔNG JOIN các bảng khác
    public List<Delivery> getAllDeliveries() {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM delivery";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Delivery delivery = new Delivery();
                delivery.setId(rs.getInt("delivery_id"));
                delivery.setExportId(rs.getInt("export_id")); // Sửa lại export_id
                delivery.setUserId(rs.getInt("user_id"));
                delivery.setRecipientName(rs.getString("recipient_name"));
                delivery.setDeliveryAddress(rs.getString("delivery_address"));
                delivery.setStatus(rs.getString("status"));
                delivery.setDeliveryDate(rs.getTimestamp("delivery_date"));
                delivery.setDescription(rs.getString("description"));
                deliveries.add(delivery);
            }
        } catch (SQLException e) {
            System.err.println("[DeliveryDAO] Lỗi khi lấy tất cả giao hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return deliveries;
    }

    // Lấy giao hàng theo trạng thái, KHÔNG JOIN các bảng khác
    public List<Delivery> getDeliveriesByStatus(String status) {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM delivery WHERE status = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Delivery delivery = new Delivery();
                    delivery.setId(rs.getInt("delivery_id"));
                    delivery.setExportId(rs.getInt("export_id")); // Sửa lại export_id
                    delivery.setUserId(rs.getInt("user_id"));
                    delivery.setRecipientName(rs.getString("recipient_name"));
                    delivery.setDeliveryAddress(rs.getString("delivery_address"));
                    delivery.setStatus(rs.getString("status"));
                    delivery.setDeliveryDate(rs.getTimestamp("delivery_date"));
                    delivery.setDescription(rs.getString("description"));
                    deliveries.add(delivery);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DeliveryDAO] Lỗi khi lấy giao hàng theo trạng thái: " + e.getMessage());
            e.printStackTrace();
        }
        return deliveries;
    }

    // Thêm phương thức insertDelivery cho xuất kho
    public int insertDelivery(Delivery delivery) throws SQLException {
        String sql = "INSERT INTO delivery (export_id, user_id, recipient_name, delivery_address, contact_person, contact_phone, status, delivery_date, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, delivery.getExportId());
            stmt.setInt(2, delivery.getUserId());
            stmt.setString(3, delivery.getRecipientName());
            stmt.setString(4, delivery.getDeliveryAddress());
            stmt.setString(5, delivery.getContactPerson());
            stmt.setString(6, delivery.getContactPhone());
            stmt.setString(7, delivery.getStatus());
            stmt.setTimestamp(8, delivery.getDeliveryDate());
            stmt.setString(9, delivery.getDescription());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating delivery failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating delivery failed, no ID obtained.");
                }
            }
        }
    }

    // Lấy chi tiết giao hàng theo ID (chỉ cho xuất kho)
    public Delivery getDeliveryById(int id) {
        String sql = "SELECT * FROM delivery WHERE delivery_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Delivery delivery = new Delivery();
                    delivery.setId(rs.getInt("delivery_id"));
                    delivery.setExportId(rs.getInt("export_id"));
                    delivery.setUserId(rs.getInt("user_id"));
                    delivery.setRecipientName(rs.getString("recipient_name"));
                    delivery.setDeliveryAddress(rs.getString("delivery_address"));
                    delivery.setContactPerson(rs.getString("contact_person"));
                    delivery.setContactPhone(rs.getString("contact_phone"));
                    delivery.setStatus(rs.getString("status"));
                    delivery.setDeliveryDate(rs.getTimestamp("delivery_date"));
                    delivery.setDescription(rs.getString("description"));
                    return delivery;
                }
            }
        } catch (SQLException e) {
            System.err.println("[DeliveryDAO] Lỗi khi lấy chi tiết giao hàng theo ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Delivery getDeliveryByExportId(int exportId) {
        String sql = "SELECT * FROM delivery WHERE export_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, exportId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Delivery delivery = new Delivery();
                    delivery.setId(rs.getInt("delivery_id"));
                    delivery.setExportId(rs.getInt("export_id"));
                    delivery.setUserId(rs.getInt("user_id"));
                    delivery.setRecipientName(rs.getString("recipient_name"));
                    delivery.setDeliveryAddress(rs.getString("delivery_address"));
                    delivery.setContactPerson(rs.getString("contact_person"));
                    delivery.setContactPhone(rs.getString("contact_phone"));
                    delivery.setStatus(rs.getString("status"));
                    delivery.setDeliveryDate(rs.getTimestamp("delivery_date"));
                    delivery.setDescription(rs.getString("description"));
                    return delivery;
                }
            }
        } catch (SQLException e) {
            System.err.println("[DeliveryDAO] Lỗi khi lấy delivery theo export_id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
