package dao;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Delivery;

public class DeliveryDAO {
    private final DBContext dbContext;
    
    public DeliveryDAO() {
        dbContext = new DBContext();
    }
    
    public List<Delivery> getAllDeliveries() {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT d.*, e.material_id, m.name AS material_name " +
                     "FROM deliveries d " +
                     "JOIN export_forms e ON d.export_id = e.export_id " +
                     "JOIN materials m ON e.material_id = m.material_id";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Delivery delivery = new Delivery();
                delivery.setId(rs.getInt("delivery_id"));
                delivery.setExportId(rs.getInt("export_id"));
                delivery.setReceiverName(rs.getString("receiver_name"));
                delivery.setDeliveryAddress(rs.getString("delivery_address"));
                delivery.setStatus(rs.getString("status"));
                delivery.setDeliveryDate(rs.getTimestamp("delivery_date"));
                delivery.setDescription(rs.getString("description"));
                delivery.setMaterialName(rs.getString("material_name"));
                deliveries.add(delivery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return deliveries;
    }
    
    public boolean addDelivery(Delivery delivery) {
        String sql = "INSERT INTO deliveries (export_id, receiver_name, delivery_address, status, delivery_date, description) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, delivery.getExportId());
            stmt.setString(2, delivery.getReceiverName());
            stmt.setString(3, delivery.getDeliveryAddress());
            stmt.setString(4, delivery.getStatus());
            stmt.setTimestamp(5, delivery.getDeliveryDate());
            stmt.setString(6, delivery.getDescription());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateDelivery(Delivery delivery) {
        String sql = "UPDATE deliveries SET export_id = ?, receiver_name = ?, delivery_address = ?, status = ?, " +
                     "delivery_date = ?, description = ? WHERE delivery_id = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, delivery.getExportId());
            stmt.setString(2, delivery.getReceiverName());
            stmt.setString(3, delivery.getDeliveryAddress());
            stmt.setString(4, delivery.getStatus());
            stmt.setTimestamp(5, delivery.getDeliveryDate());
            stmt.setString(6, delivery.getDescription());
            stmt.setInt(7, delivery.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteDelivery(int id) {
        String sql = "DELETE FROM deliveries WHERE delivery_id = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Delivery getDeliveryById(int id) {
        String sql = "SELECT d.*, e.material_id, m.name AS material_name " +
                     "FROM deliveries d " +
                     "JOIN export_forms e ON d.export_id = e.export_id " +
                     "JOIN materials m ON e.material_id = m.material_id " +
                     "WHERE d.delivery_id = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Delivery delivery = new Delivery();
                delivery.setId(rs.getInt("delivery_id"));
                delivery.setExportId(rs.getInt("export_id"));
                delivery.setReceiverName(rs.getString("receiver_name"));
                delivery.setDeliveryAddress(rs.getString("delivery_address"));
                delivery.setStatus(rs.getString("status"));
                delivery.setDeliveryDate(rs.getTimestamp("delivery_date"));
                delivery.setDescription(rs.getString("description"));
                delivery.setMaterialName(rs.getString("material_name"));
                return delivery;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}