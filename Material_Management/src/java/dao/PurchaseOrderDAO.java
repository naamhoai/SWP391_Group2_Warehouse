package dao;

import dal.DBContext;
import model.PurchaseOrder;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class PurchaseOrderDAO {

    private Connection connection;

    public PurchaseOrderDAO() {
        this.connection = new DBContext().getConnection();
    }

    public PurchaseOrderDAO(Connection connection) {
        this.connection = connection;
    }

    // Thêm đơn mua mới vào bảng purchase_orders
    public void addPurchaseOrder(PurchaseOrder order) throws SQLException {
        String sql = "INSERT INTO purchase_orders (supplier_id, user_id, order_date, total_amount, status, request_status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, order.getSupplierId());
            stmt.setInt(2, order.getUserId());
            stmt.setTimestamp(3, order.getOrderDate());
            stmt.setDouble(4, order.getTotalAmount());
            stmt.setString(5, order.getStatus());
            stmt.setString(6, order.getApprovalStatus());
            stmt.executeUpdate();

            // Get the auto-generated purchase_order_id
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                order.setPurchaseOrderId(rs.getInt(1));  // Set the generated purchaseOrderId
            }
        }
    }

    // Tạo yêu cầu duyệt đơn mua vật tư
    public void createRequestForApproval(int userId, int purchaseOrderId, String reason) throws SQLException {
        String sql = "INSERT INTO requests (request_type, purchase_order_id, user_id, reason, request_status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "Mua Vật Tư");
            stmt.setInt(2, purchaseOrderId);
            stmt.setInt(3, userId);
            stmt.setString(4, reason);
            stmt.setString(5, "Pending");  // Trạng thái yêu cầu ban đầu là Pending
            stmt.executeUpdate();
        }
    }

    // Lấy danh sách đơn mua theo trạng thái
    public List<PurchaseOrder> getPurchaseOrdersByStatus(String status) throws SQLException {
        List<PurchaseOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM purchase_orders WHERE status = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrder order = new PurchaseOrder(
                            rs.getInt("supplier_id"),
                            rs.getInt("user_id"),
                            rs.getDouble("total_amount")
                    );
                    order.setPurchaseOrderId(rs.getInt("purchase_order_id"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setStatus(rs.getString("status"));
                    order.setApprovalStatus(rs.getString("request_status"));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    // Cập nhật trạng thái yêu cầu duyệt trong bảng requests
    public void updateRequestStatus(int requestId, String status) throws SQLException {
        String sql = "UPDATE requests SET request_status = ? WHERE request_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, requestId);
            stmt.executeUpdate();
        }
    }

    // Cập nhật đơn mua
    public boolean updatePurchaseOrder(PurchaseOrder order) throws SQLException {
        String sql = "UPDATE purchase_orders SET total_amount = ?, status = ? WHERE purchase_order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Gán giá trị cho các tham số trong câu truy vấn
            stmt.setDouble(1, order.getTotalAmount());  // Tổng giá trị đơn hàng
            stmt.setString(2, order.getStatus());       // Trạng thái đơn hàng
            stmt.setInt(3, order.getPurchaseOrderId()); // ID đơn hàng

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu cập nhật thành công
        }
    }

    // Lấy PurchaseOrder theo purchase_order_id
    public PurchaseOrder getPurchaseOrderById(int purchaseOrderId) throws SQLException {
        String sql = "SELECT * FROM purchase_orders WHERE purchase_order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, purchaseOrderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PurchaseOrder order = new PurchaseOrder(
                        rs.getInt("supplier_id"),
                        rs.getInt("user_id"),
                        rs.getDouble("total_amount")
                    );
                    order.setPurchaseOrderId(rs.getInt("purchase_order_id"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setStatus(rs.getString("status"));
                    order.setApprovalStatus(rs.getString("request_status"));
                    return order;
                }
            }
        }
        return null;
    }
    public boolean existsPurchaseOrderByRequestId(int requestId) throws SQLException {
        String sql = "SELECT 1 FROM purchase_orders WHERE request_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}
