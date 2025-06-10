package dao;

import model.PurchaseOrder;
import java.sql.*;

public class PurchaseOrderDAO {
    private Connection connection;

    public PurchaseOrderDAO(Connection connection) {
        this.connection = connection;
    }

    // Lưu thông tin đơn mua vào cơ sở dữ liệu
    public void save(PurchaseOrder purchaseOrder) {
        String sql = "INSERT INTO purchase_orders (supplier_id, user_id, total_amount, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, purchaseOrder.getSupplierId());
            statement.setInt(2, purchaseOrder.getUserId());
            statement.setDouble(3, purchaseOrder.getTotalAmount());
            statement.setString(4, purchaseOrder.getStatus());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        purchaseOrder.setPurchaseOrderId(generatedKeys.getInt(1)); // Set the generated ID
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật trạng thái duyệt đơn hàng
    public void updateApprovalStatus(PurchaseOrder purchaseOrder) {
        String sql = "UPDATE purchase_orders SET status = ?, approval_status = ?, approved_by = ? WHERE purchase_order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, purchaseOrder.getStatus());
            statement.setString(2, purchaseOrder.getApprovalStatus());
            statement.setInt(3, purchaseOrder.getApprovedBy());
            statement.setInt(4, purchaseOrder.getPurchaseOrderId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy đơn hàng theo ID
    public PurchaseOrder getById(int purchaseOrderId) {
        String sql = "SELECT * FROM purchase_orders WHERE purchase_order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, purchaseOrderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                PurchaseOrder purchaseOrder = new PurchaseOrder(
                        resultSet.getInt("supplier_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDouble("total_amount")
                );
                purchaseOrder.setPurchaseOrderId(resultSet.getInt("purchase_order_id"));
                purchaseOrder.setStatus(resultSet.getString("status"));
                purchaseOrder.setApprovalStatus(resultSet.getString("approval_status"));
                purchaseOrder.setApprovedBy(resultSet.getInt("approved_by"));
                return purchaseOrder;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
