package dao;

import dal.DBContext;
import model.RequestHistoryDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestHistoryDetailDAO {
    
    public List<RequestHistoryDetail> getDetailsByHistoryId(int historyId) {
        List<RequestHistoryDetail> details = new ArrayList<>();
        String sql = "SELECT d.*, u.unit_name FROM request_history_details d LEFT JOIN units u ON d.warehouse_unit_id = u.unit_id WHERE d.history_id = ?";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, historyId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                RequestHistoryDetail detail = new RequestHistoryDetail();
                detail.setHistoryDetailId(rs.getInt("history_detail_id"));
                detail.setHistoryId(rs.getInt("history_id"));
                detail.setMaterialId(rs.getInt("material_id"));
                detail.setMaterialName(rs.getString("material_name"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setWarehouseUnitId(rs.getInt("warehouse_unit_id"));
                detail.setMaterialCondition(rs.getString("material_condition"));
                detail.setUnitName(rs.getString("unit_name"));
                
                details.add(detail);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return details;
    }
    
    public void insertHistoryDetail(RequestHistoryDetail detail) {
        String sql = "INSERT INTO request_history_details (history_id, material_id, material_name, quantity, warehouse_unit_id, material_condition) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, detail.getHistoryId());
            ps.setInt(2, detail.getMaterialId());
            ps.setString(3, detail.getMaterialName());
            ps.setInt(4, detail.getQuantity());
            ps.setInt(5, detail.getWarehouseUnitId());
            ps.setString(6, detail.getMaterialCondition());
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 