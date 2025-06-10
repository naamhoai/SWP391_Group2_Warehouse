package dao;

import model.PurchaseOrderDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderDetailDAO {

    private Connection connection;

    public PurchaseOrderDetailDAO(Connection connection) {
        this.connection = connection;
    }

    // Thêm chi tiết đơn mua
    public boolean addPurchaseOrderDetail(PurchaseOrderDetail detail) {
        String sql = "INSERT INTO purchase_order_details (purchase_order_id, material_id, quantity, unit_price, total_price, material_condition) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, detail.getPurchaseOrderId());
            stmt.setInt(2, detail.getMaterialId());
            stmt.setInt(3, detail.getQuantity());
            stmt.setDouble(4, detail.getUnitPrice());
            stmt.setDouble(5, detail.getTotalPrice());
            stmt.setString(6, detail.getMaterialCondition());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách chi tiết của đơn mua
    public List<PurchaseOrderDetail> getPurchaseOrderDetails(int purchaseOrderId) {
        List<PurchaseOrderDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM purchase_order_details WHERE purchase_order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, purchaseOrderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int materialId = rs.getInt("material_id");
                int quantity = rs.getInt("quantity");
                double unitPrice = rs.getDouble("unit_price");
                double totalPrice = rs.getDouble("total_price");
                String materialCondition = rs.getString("material_condition");

                PurchaseOrderDetail detail = new PurchaseOrderDetail();
                detail.setPurchaseOrderId(purchaseOrderId);
                detail.setMaterialId(materialId);
                detail.setQuantity(quantity);
                detail.setUnitPrice(unitPrice);
                detail.setTotalPrice(totalPrice);
                detail.setMaterialCondition(materialCondition);

                details.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }
}
