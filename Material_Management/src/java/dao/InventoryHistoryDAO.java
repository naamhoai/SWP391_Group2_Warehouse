package dao;

import model.InventoryHistoryRow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dal.DBContext;

public class InventoryHistoryDAO {

    private final String BASE_HISTORY_QUERY = 
        "(SELECT " +
        // Nguồn 1: Nhập kho (Mua hàng)
        "    'Nhập kho' AS transaction_type, " +
        "    po.order_date AS transaction_date, " +
        "    pod.material_name, " +
        "    'Mới' AS material_condition, " +
        "    pod.quantity, " +
        "    pod.material_id, " +
        "    COALESCE(approver.full_name, creator.full_name) AS actor_name, " +
        "    po.purchase_order_id AS reference_id " +
        "FROM purchase_orders po " +
        "JOIN purchase_order_details pod ON po.purchase_order_id = pod.purchase_order_id " +
        "JOIN users creator ON po.user_id = creator.user_id " +
        "LEFT JOIN users approver ON po.approved_by = approver.user_id " +
        "WHERE po.approval_status = 'Approved' AND pod.material_name IS NOT NULL " +
        
        "UNION ALL " +
        
        // Nguồn 2: Xuất kho
        "SELECT " +
        "    'Xuất kho' AS transaction_type, " +
        "    ef.export_date AS transaction_date, " +
        "    em.material_name, " +
        "    em.material_condition, " +
        "    em.quantity AS quantity, " +
        "    em.material_id, " +
        "    u_exporter.full_name AS actor_name, " +
        "    (ef.export_id + 1000000) AS reference_id " + // === THAY ĐỔI: Lấy export_id + 1 triệu ===
        "FROM export_forms ef " +
        "JOIN export_materials em ON ef.export_id = em.export_id " +
        "JOIN users u_exporter ON ef.user_id = u_exporter.user_id " +
        "WHERE (ef.status = 'Hoàn thành' OR ef.status = 'Đã xuất kho') AND em.material_name IS NOT NULL " +

        "UNION ALL " +
        
        // Nguồn 3: Nhập kho (Thủ công)
        "SELECT " +
        "    'Nhập kho' AS transaction_type, " +
        "    ih.created_at AS transaction_date, " +
        "    ih.material_name, " +
        "    'Mới' AS material_condition, " +
        "    ih.quantity, " +
        "    m.material_id, " +
        "    ih.received_by AS actor_name, " +
        "    (ih.id + 2000000) AS reference_id " +
        "FROM import_history ih " +
        "LEFT JOIN materials m ON ih.material_name = m.name " +
        "WHERE ih.material_name IS NOT NULL " +
        ") AS MaterialHistory";
        
    private int executeCountQuery(String sql, List<Object> params) {
        int count = 0;
        Connection conn = null;
        try {
            conn = new DBContext().getConnection();
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                for (int i = 0; i < params.size(); i++) {
                    st.setObject(i + 1, params.get(i));
                }
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        count = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
        return count;
    }

    private List<InventoryHistoryRow> executeGetQuery(String sql, List<Object> params) {
        List<InventoryHistoryRow> list = new ArrayList<>();
        Connection conn = null;
        int originalIsolationLevel = 0;
        try {
            conn = new DBContext().getConnection();
            originalIsolationLevel = conn.getTransactionIsolation();
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                for (int i = 0; i < params.size(); i++) {
                    st.setObject(i + 1, params.get(i));
                }
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        InventoryHistoryRow row = new InventoryHistoryRow();
                        row.setTransactionType(rs.getString("transaction_type"));
                        row.setReferenceId(rs.getInt("reference_id"));
                        row.setTransactionDate(rs.getTimestamp("transaction_date"));
                        row.setMaterialName(rs.getString("material_name"));
                        row.setMaterialCondition(rs.getString("material_condition"));
                        row.setQuantity(rs.getInt("quantity"));
                        row.setMaterialId(rs.getInt("material_id"));
                        row.setActorName(rs.getString("actor_name"));
                        list.add(row);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
             if (conn != null) {
                try {
                    conn.setTransactionIsolation(originalIsolationLevel);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public int countInventoryHistoryByKeyword(String keyword, String fromDate, String toDate, String transactionType) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM " + BASE_HISTORY_QUERY + " WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(material_name) LIKE ? OR LOWER(actor_name) LIKE ?)");
            params.add("%" + keyword.trim().toLowerCase() + "%");
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append(" AND DATE(transaction_date) >= ?");
            params.add(fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append(" AND DATE(transaction_date) <= ?");
            params.add(toDate);
        }
        if (transactionType != null && !transactionType.isEmpty() && !transactionType.equals("All")) {
            sql.append(" AND transaction_type = ?");
            params.add(transactionType);
        }
        return executeCountQuery(sql.toString(), params);
    }

    public List<InventoryHistoryRow> getInventoryHistoryPagingByKeyword(String keyword, String fromDate, String toDate, String transactionType, int page, int pageSize) {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + BASE_HISTORY_QUERY + " WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(material_name) LIKE ? OR LOWER(actor_name) LIKE ?)");
            params.add("%" + keyword.trim().toLowerCase() + "%");
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append(" AND DATE(transaction_date) >= ?");
            params.add(fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append(" AND DATE(transaction_date) <= ?");
            params.add(toDate);
        }
        if (transactionType != null && !transactionType.isEmpty() && !transactionType.equals("All")) {
            sql.append(" AND transaction_type = ?");
            params.add(transactionType);
        }
        
        sql.append(" ORDER BY transaction_date DESC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);
        
        return executeGetQuery(sql.toString(), params);
    }
}