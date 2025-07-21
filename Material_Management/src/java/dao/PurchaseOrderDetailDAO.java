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
                    
                    // material_id có thể null
                    int materialId = rs.getInt("material_id");
                    if (!rs.wasNull()) {
                        detail.setMaterialId(materialId);
                    }
                    
                    detail.setMaterialName(rs.getString("material_name"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setUnit(rs.getString("unit"));
                    detail.setBaseUnit(rs.getString("base_unit"));
                    detail.setConvertedUnit(rs.getString("converted_unit"));
                    detail.setUnitPrice(rs.getDouble("unit_price"));
                    detail.setTotalPrice(rs.getDouble("total_price"));
                    detail.setDescription(rs.getString("description"));
                    detail.setMaterialCondition(rs.getString("material_condition"));
                    
                    // category_id có thể null
                    int categoryId = rs.getInt("category_id");
                    if (!rs.wasNull()) {
                        detail.setCategoryId(categoryId);
                    }
                    
                    // conversion_id có thể null
                    int conversionId = rs.getInt("conversion_id");
                    if (!rs.wasNull()) {
                        detail.setConversionId(conversionId);
                    }
                    
                    list.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String addPurchaseOrderDetail(PurchaseOrderDetail detail, Connection conn) {
        String sql = "INSERT INTO purchase_order_details (purchase_order_id, material_id, material_name, category_id, conversion_id, quantity, unit, base_unit, converted_unit, unit_price, total_price, description, material_condition) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detail.getPurchaseOrderId());
            // material_id có thể null
            if (detail.getMaterialId() > 0) {
                ps.setInt(2, detail.getMaterialId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, detail.getMaterialName());
            // category_id có thể null
            if (detail.getCategoryId() > 0) {
                ps.setInt(4, detail.getCategoryId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            // conversion_id có thể null
            if (detail.getConversionId() > 0) {
                ps.setInt(5, detail.getConversionId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setInt(6, detail.getQuantity());
            ps.setString(7, detail.getUnit());
            ps.setString(8, detail.getBaseUnit());
            ps.setString(9, detail.getConvertedUnit());
            ps.setDouble(10, detail.getUnitPrice());
            ps.setDouble(11, detail.getTotalPrice());
            ps.setString(12, detail.getDescription());
            ps.setString(13, detail.getMaterialCondition());
            System.out.println("==> Đang insert chi tiết: " + detail.getMaterialName());
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("==> Lỗi khi insert chi tiết: " + detail.getMaterialName());
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