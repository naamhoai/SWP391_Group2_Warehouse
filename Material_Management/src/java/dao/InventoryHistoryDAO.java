package dao;

import model.InventoryHistoryRow;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dal.DBContext;

public class InventoryHistoryDAO extends DBContext {
    public List<InventoryHistoryRow> getInventoryHistory(String material, String actorName, String transactionType) {
        List<InventoryHistoryRow> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM MaterialHistoryView WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (material != null && !material.trim().isEmpty()) {
            sql.append(" AND LOWER(material_name) LIKE ?");
            params.add("%" + material.trim().toLowerCase() + "%");
        }
        if (actorName != null && !actorName.trim().isEmpty()) {
            sql.append(" AND LOWER(actor_name) LIKE ?");
            params.add("%" + actorName.trim().toLowerCase() + "%");
        }
        if (transactionType != null && !transactionType.trim().isEmpty()) {
            sql.append(" AND transaction_type = ?");
            params.add(transactionType);
        }
        sql.append(" ORDER BY transaction_date DESC");
        try (Connection conn = new dal.DBContext().getConnection(); PreparedStatement st = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    InventoryHistoryRow row = new InventoryHistoryRow();
                    row.setTransactionType(rs.getString("transaction_type"));
                    row.setReferenceId(rs.getObject("reference_id") != null ? rs.getInt("reference_id") : null);
                    row.setTransactionDate(rs.getTimestamp("transaction_date"));
                    row.setMaterialName(rs.getString("material_name"));
                    row.setMaterialCondition(rs.getString("material_condition"));
                    row.setQuantity(rs.getObject("quantity") != null ? rs.getInt("quantity") : null);
                    row.setMaterialId(rs.getObject("material_id") != null ? rs.getInt("material_id") : null);
                    row.setActorName(rs.getString("actor_name"));
                    list.add(row);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public int countInventoryHistory(String material, String actorName, String transactionType) {
        int count = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM MaterialHistoryView WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (material != null && !material.trim().isEmpty()) {
            sql.append(" AND LOWER(material_name) LIKE ?");
            params.add("%" + material.trim().toLowerCase() + "%");
        }
        if (actorName != null && !actorName.trim().isEmpty()) {
            sql.append(" AND LOWER(actor_name) LIKE ?");
            params.add("%" + actorName.trim().toLowerCase() + "%");
        }
        if (transactionType != null && !transactionType.trim().isEmpty()) {
            sql.append(" AND transaction_type = ?");
            params.add(transactionType);
        }
        try (Connection conn = new dal.DBContext().getConnection(); PreparedStatement st = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return count;
    }

    public List<InventoryHistoryRow> getInventoryHistoryPaging(String material, String actorName, String transactionType, int page, int pageSize) {
        List<InventoryHistoryRow> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM MaterialHistoryView WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (material != null && !material.trim().isEmpty()) {
            sql.append(" AND LOWER(material_name) LIKE ?");
            params.add("%" + material.trim().toLowerCase() + "%");
        }
        if (actorName != null && !actorName.trim().isEmpty()) {
            sql.append(" AND LOWER(actor_name) LIKE ?");
            params.add("%" + actorName.trim().toLowerCase() + "%");
        }
        if (transactionType != null && !transactionType.trim().isEmpty()) {
            sql.append(" AND transaction_type = ?");
            params.add(transactionType);
        }
        sql.append(" ORDER BY transaction_date DESC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);
        try (Connection conn = new dal.DBContext().getConnection(); PreparedStatement st = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    InventoryHistoryRow row = new InventoryHistoryRow();
                    row.setTransactionType(rs.getString("transaction_type"));
                    row.setReferenceId(rs.getObject("reference_id") != null ? rs.getInt("reference_id") : null);
                    row.setTransactionDate(rs.getTimestamp("transaction_date"));
                    row.setMaterialName(rs.getString("material_name"));
                    row.setMaterialCondition(rs.getString("material_condition"));
                    row.setQuantity(rs.getObject("quantity") != null ? rs.getInt("quantity") : null);
                    row.setMaterialId(rs.getObject("material_id") != null ? rs.getInt("material_id") : null);
                    row.setActorName(rs.getString("actor_name"));
                    list.add(row);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public int countInventoryHistoryByKeyword(String keyword, String transactionType, String fromDate, String toDate) {
        int count = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM MaterialHistoryView WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(material_name) LIKE ? OR LOWER(actor_name) LIKE ?)");
            String kw = "%" + keyword.trim().toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
        }
        if (transactionType != null && !transactionType.trim().isEmpty()) {
            sql.append(" AND transaction_type = ?");
            params.add(transactionType);
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append(" AND DATE(transaction_date) >= ?");
            params.add(fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append(" AND DATE(transaction_date) <= ?");
            params.add(toDate);
        }
        try (Connection conn = new dal.DBContext().getConnection(); PreparedStatement st = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return count;
    }

    public List<InventoryHistoryRow> getInventoryHistoryPagingByKeyword(String keyword, String transactionType, String fromDate, String toDate, int page, int pageSize) {
        List<InventoryHistoryRow> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM MaterialHistoryView WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(material_name) LIKE ? OR LOWER(actor_name) LIKE ?)");
            String kw = "%" + keyword.trim().toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
        }
        if (transactionType != null && !transactionType.trim().isEmpty()) {
            sql.append(" AND transaction_type = ?");
            params.add(transactionType);
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append(" AND DATE(transaction_date) >= ?");
            params.add(fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append(" AND DATE(transaction_date) <= ?");
            params.add(toDate);
        }
        sql.append(" ORDER BY transaction_date DESC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);
        try (Connection conn = new dal.DBContext().getConnection(); PreparedStatement st = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    InventoryHistoryRow row = new InventoryHistoryRow();
                    row.setTransactionType(rs.getString("transaction_type"));
                    row.setReferenceId(rs.getObject("reference_id") != null ? rs.getInt("reference_id") : null);
                    row.setTransactionDate(rs.getTimestamp("transaction_date"));
                    row.setMaterialName(rs.getString("material_name"));
                    row.setMaterialCondition(rs.getString("material_condition"));
                    row.setQuantity(rs.getObject("quantity") != null ? rs.getInt("quantity") : null);
                    row.setMaterialId(rs.getObject("material_id") != null ? rs.getInt("material_id") : null);
                    row.setActorName(rs.getString("actor_name"));
                    list.add(row);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
} 