package dao;

import dal.DBContext;
import model.PurchaseOrderDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderDetailDAO extends DBContext {
    public List<PurchaseOrderDetail> getDetailsByOrderId(int orderId) {
        List<PurchaseOrderDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM purchase_order_details WHERE purchase_order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrderDetail detail = new PurchaseOrderDetail();
                    detail.setPurchaseOrderDetailId(rs.getInt("purchase_order_detail_id"));
                    detail.setPurchaseOrderId(rs.getInt("purchase_order_id"));
                    int materialId = rs.getInt("material_id");
                    if (!rs.wasNull()) {
                        detail.setMaterialId(materialId);
                    }
                    detail.setMaterialName(rs.getString("material_name"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setUnit(rs.getString("unit"));
                    detail.setConvertedUnit(rs.getString("converted_unit"));
                    detail.setUnitPrice(rs.getDouble("unit_price"));
                    detail.setTotalPrice(rs.getDouble("total_price"));
                    list.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String addPurchaseOrderDetail(PurchaseOrderDetail detail, Connection conn) {
        try {
            // Lấy material_id từ material_name
            int materialId = 0;
            String sqlMaterial = "SELECT material_id FROM materials WHERE name = ? LIMIT 1";
            try (PreparedStatement psMat = conn.prepareStatement(sqlMaterial)) {
                psMat.setString(1, detail.getMaterialName());
                try (ResultSet rsMat = psMat.executeQuery()) {
                    if (rsMat.next()) {
                        materialId = rsMat.getInt("material_id");
                    } else {
                        return "Không tìm thấy vật tư: '" + detail.getMaterialName() + "' trong hệ thống.";
                    }
                }
            }
            detail.setMaterialId(materialId);

            String sql = "INSERT INTO purchase_order_details (purchase_order_id, material_id, material_name, quantity, unit, converted_unit, unit_price, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detail.getPurchaseOrderId());
            if (detail.getMaterialId() > 0) {
                ps.setInt(2, detail.getMaterialId());
            } else {
                    ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setString(3, detail.getMaterialName());
                ps.setInt(4, detail.getQuantity());
                ps.setString(5, detail.getUnit());
                ps.setString(6, detail.getConvertedUnit());
                ps.setDouble(7, detail.getUnitPrice());
                ps.setDouble(8, detail.getTotalPrice());
            ps.executeUpdate();
            return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi insert chi tiết: " + detail.getMaterialName() + " - " + e.getMessage();
        }
    }
    
    public void deleteDetailsByOrderId(int orderId, Connection conn) {
        String sql = "DELETE FROM purchase_order_details WHERE purchase_order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 