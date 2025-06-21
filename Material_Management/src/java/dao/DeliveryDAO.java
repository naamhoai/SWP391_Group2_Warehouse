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
                delivery.setPurchaseOrderId(rs.getInt("purchase_order_id"));
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
                    delivery.setPurchaseOrderId(rs.getInt("purchase_order_id"));
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

    // Lấy chi tiết giao hàng theo ID
    public Delivery getDeliveryById(int id) {
        String sql = "SELECT d.*, po.user_id AS order_user_id, pod.material_id, m.name AS material_name "
                + "FROM delivery d "
                + "JOIN purchase_orders po ON d.purchase_order_id = po.purchase_order_id "
                + "JOIN purchase_order_details pod ON po.purchase_order_id = pod.purchase_order_id "
                + "JOIN materials m ON pod.material_id = m.material_id "
                + "WHERE d.delivery_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Delivery delivery = new Delivery();
                    delivery.setId(rs.getInt("delivery_id"));
                    delivery.setPurchaseOrderId(rs.getInt("purchase_order_id"));
                    delivery.setUserId(rs.getInt("user_id"));
                    delivery.setRecipientName(rs.getString("recipient_name"));
                    delivery.setDeliveryAddress(rs.getString("delivery_address"));
                    delivery.setStatus(rs.getString("status"));
                    delivery.setDeliveryDate(rs.getTimestamp("delivery_date"));
                    delivery.setDescription(rs.getString("description"));
                    delivery.setMaterialName(rs.getString("material_name"));
                    return delivery;
                }
            }
        } catch (SQLException e) {
            System.err.println("[DeliveryDAO] Lỗi khi lấy chi tiết giao hàng theo ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
