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

    // Thêm đơn mua mới
    public void addPurchaseOrder(PurchaseOrder order) throws SQLException {
        String sql = "INSERT INTO purchase_orders (supplier_id, user_id, order_date, total_amount, status, note) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, order.getSupplierId());
            stmt.setInt(2, order.getUserId());
            stmt.setTimestamp(3, order.getOrderDate());
            stmt.setDouble(4, order.getTotalAmount());
            stmt.setString(5, order.getStatus());
            stmt.setString(6, order.getNote());
            stmt.executeUpdate();

            // Lấy ID vừa tạo
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                order.setPurchaseOrderId(rs.getInt(1));
            }
        }
    }

    // Lấy danh sách đơn mua theo trạng thái
    public List<PurchaseOrder> getPurchaseOrdersByStatus(String status) throws SQLException {
        List<PurchaseOrder> orders = new ArrayList<>();
        String sql = "SELECT po.*, u.full_name FROM purchase_orders po " +
                     "JOIN users u ON po.user_id = u.user_id " +
                     "WHERE po.status = ?";
                     
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrder order = new PurchaseOrder();
                    order.setPurchaseOrderId(rs.getInt("purchase_order_id"));
                    order.setSupplierId(rs.getInt("supplier_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setStatus(rs.getString("status"));
                    order.setCreatorName(rs.getString("full_name"));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    // Lấy đơn mua theo ID
    public PurchaseOrder getPurchaseOrderById(int purchaseOrderId) throws SQLException {
        String sql = "SELECT po.*, u.full_name, s.supplier_name, s.contact_person, s.supplier_phone, r.role_name FROM purchase_orders po " +
                     "JOIN users u ON po.user_id = u.user_id " +
                     "LEFT JOIN supplier s ON po.supplier_id = s.supplier_id " +
                     "LEFT JOIN roles r ON u.role_id = r.role_id " +
                     "WHERE po.purchase_order_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, purchaseOrderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PurchaseOrder order = new PurchaseOrder();
                    order.setPurchaseOrderId(rs.getInt("purchase_order_id"));
                    order.setSupplierId(rs.getInt("supplier_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setStatus(rs.getString("status"));
                    order.setNote(rs.getString("note"));
                    order.setApprovalStatus(rs.getString("approval_status"));
                    order.setRejectionReason(rs.getString("rejection_reason"));
                    order.setCreatorName(rs.getString("full_name"));
                    order.setSupplierName(rs.getString("supplier_name") != null ? rs.getString("supplier_name") : "");
                    order.setCreatorRoleName(rs.getString("role_name") != null ? rs.getString("role_name") : "");
                    order.setContactPerson(rs.getString("contact_person") != null ? rs.getString("contact_person") : "");
                    order.setSupplierPhone(rs.getString("supplier_phone") != null ? rs.getString("supplier_phone") : "");
                    return order;
                }
            }
        }
        return null;
    }

    // Lấy tất cả đơn mua với tìm kiếm
    public List<PurchaseOrder> getAllPurchaseOrders(String fromDate, String toDate, String status, String supplier, String sortOrder) throws SQLException {
        List<PurchaseOrder> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT po.*, u.full_name, s.supplier_name, r.role_name FROM purchase_orders po ");
        sql.append("JOIN users u ON po.user_id = u.user_id ");
        sql.append("LEFT JOIN supplier s ON po.supplier_id = s.supplier_id ");
        sql.append("LEFT JOIN roles r ON u.role_id = r.role_id ");
        sql.append("WHERE 1=1 ");
        
        List<Object> params = new ArrayList<>();
        
        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append("AND DATE(po.order_date) >= ? ");
            params.add(fromDate);
        }
        
        if (toDate != null && !toDate.isEmpty()) {
            sql.append("AND DATE(po.order_date) <= ? ");
            params.add(toDate);
        }
        
        if (status != null && !status.isEmpty() && !status.equals("All")) {
            sql.append("AND po.status = ? ");
            params.add(status);
        }
        
        if (supplier != null && !supplier.isEmpty() && !supplier.equals("All")) {
            sql.append("AND s.supplier_name LIKE ? ");
            params.add("%" + supplier + "%");
        }
        
        // Sắp xếp theo ID
        if (sortOrder != null && sortOrder.equalsIgnoreCase("asc")) {
            sql.append("ORDER BY po.purchase_order_id ASC");
        } else {
            sql.append("ORDER BY po.purchase_order_id DESC");
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrder order = new PurchaseOrder();
                    order.setPurchaseOrderId(rs.getInt("purchase_order_id"));
                    order.setSupplierId(rs.getInt("supplier_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setStatus(rs.getString("status"));
                    order.setNote(rs.getString("note"));
                    order.setApprovalStatus(rs.getString("approval_status"));
                    order.setRejectionReason(rs.getString("rejection_reason"));
                    order.setCreatorName(rs.getString("full_name"));
                    order.setSupplierName(rs.getString("supplier_name") != null ? rs.getString("supplier_name") : "");
                    order.setCreatorRoleName(rs.getString("role_name") != null ? rs.getString("role_name") : "");
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    // Lấy danh sách tên nhà cung cấp duy nhất đã từng xuất hiện trong đơn mua
    public List<String> getSupplierNamesInOrders() throws SQLException {
        List<String> names = new ArrayList<>();
        String sql = "SELECT DISTINCT s.supplier_name FROM purchase_orders po JOIN supplier s ON po.supplier_id = s.supplier_id WHERE s.supplier_name IS NOT NULL ORDER BY s.supplier_name ASC";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                names.add(rs.getString("supplier_name"));
            }
        }
        return names;
    }

    // Cập nhật đơn mua
    public boolean updatePurchaseOrder(PurchaseOrder order) throws SQLException {
        String sql = "UPDATE purchase_orders SET supplier_id = ?, total_amount = ?, note = ?, status = ?, approval_status = ?, rejection_reason = ? WHERE purchase_order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, order.getSupplierId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setString(3, order.getNote());
            stmt.setString(4, order.getStatus());
            stmt.setString(5, order.getApprovalStatus());
            stmt.setString(6, order.getRejectionReason());
            stmt.setInt(7, order.getPurchaseOrderId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    // Lấy số lượng đơn mua đã duyệt theo tháng
    public java.util.Map<String, Integer> getApprovedPurchaseOrderCountByMonth(String startDate, String endDate) {
        java.util.Map<String, Integer> result = new java.util.LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(order_date, '%Y-%m') AS month, COUNT(*) AS total_approved_orders " +
                     "FROM purchase_orders WHERE approval_status = 'Approved' ";
        java.util.List<Object> params = new java.util.ArrayList<>();
        if (startDate != null && !startDate.isEmpty()) {
            sql += "AND order_date >= ? ";
            params.add(startDate + "-01 00:00:00");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql += "AND order_date <= ? ";
            params.add(endDate + "-31 23:59:59");
        }
        sql += "GROUP BY month ORDER BY month";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("month"), rs.getInt("total_approved_orders"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
