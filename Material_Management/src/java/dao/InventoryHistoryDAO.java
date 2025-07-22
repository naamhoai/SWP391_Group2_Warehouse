package dao;

import model.InventoryHistoryRow;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dal.DBContext;

public class InventoryHistoryDAO extends DBContext {
    public List<InventoryHistoryRow> getInventoryHistory(String type, String material, String from, String to, String operator) {
        List<InventoryHistoryRow> list = new ArrayList<>();
        if (type == null) {
            List<String> selects = new ArrayList<>();
            StringBuilder importWhere = new StringBuilder(" WHERE 1=1");
            if (material != null && !material.trim().isEmpty()) {
                importWhere.append(" AND material_name LIKE ?");
            }
            if (operator != null && !operator.trim().isEmpty()) {
                importWhere.append(" AND received_by LIKE ?");
            }
            if (from != null && !from.isEmpty()) {
                importWhere.append(" AND created_at >= ?");
            }
            if (to != null && !to.isEmpty()) {
                importWhere.append(" AND created_at <= ?");
            }
            importWhere.append(" AND (status = 'Mới' OR status = 'Cũ')");
            String importSql = "SELECT 'import' AS type, created_at AS date, id AS code, material_name, quantity, unit, received_by AS operator, status, reason AS note FROM import_history" + importWhere.toString();
            StringBuilder exportWhere = new StringBuilder(" WHERE 1=1");
            if (material != null && !material.trim().isEmpty()) {
                exportWhere.append(" AND em.material_name LIKE ?");
            }
            if (operator != null && !operator.trim().isEmpty()) {
                exportWhere.append(" AND u2.full_name LIKE ?");
            }
            if (from != null && !from.isEmpty()) {
                exportWhere.append(" AND ef.export_date >= ?");
            }
            if (to != null && !to.isEmpty()) {
                exportWhere.append(" AND ef.export_date <= ?");
            }
            exportWhere.append(" AND ef.status = 'Đã xuất kho'");
            String exportSql = "SELECT 'export' AS type, ef.export_date AS date, ef.export_id AS code, em.material_name, em.quantity, u.unit_name, u2.full_name AS operator, ef.status, ef.reason AS note FROM export_forms ef JOIN export_materials em ON ef.export_id = em.export_id JOIN units u ON em.warehouse_unit_id = u.unit_id JOIN users u2 ON ef.user_id = u2.user_id" + exportWhere.toString();
            StringBuilder purchaseWhere = new StringBuilder(" WHERE po.status = 'Approved'");
            if (operator != null && !operator.trim().isEmpty()) {
                purchaseWhere.append(" AND u.full_name LIKE ?");
            }
            if (from != null && !from.isEmpty()) {
                purchaseWhere.append(" AND po.order_date >= ?");
            }
            if (to != null && !to.isEmpty()) {
                purchaseWhere.append(" AND po.order_date <= ?");
            }
            String purchaseSql = "SELECT 'purchase' AS type, po.order_date AS date, po.purchase_order_id AS code, NULL AS material_name, NULL AS quantity, NULL AS unit, u.full_name AS operator, po.status, po.note AS note FROM purchase_orders po JOIN users u ON po.user_id = u.user_id" + purchaseWhere.toString();
            selects.add(importSql);
            selects.add(exportSql);
            selects.add(purchaseSql);
            String finalSql = String.join(" UNION ALL ", selects) + " ORDER BY date DESC";
            try (Connection conn = new dal.DBContext().getConnection(); PreparedStatement st = conn.prepareStatement(finalSql)) {
                int paramIndex = 1;
                if (material != null && !material.trim().isEmpty()) st.setString(paramIndex++, "%" + material.trim() + "%");
                if (operator != null && !operator.trim().isEmpty()) st.setString(paramIndex++, "%" + operator.trim() + "%");
                if (from != null && !from.isEmpty()) st.setString(paramIndex++, from + " 00:00:00");
                if (to != null && !to.isEmpty()) st.setString(paramIndex++, to + " 23:59:59");
                if (material != null && !material.trim().isEmpty()) st.setString(paramIndex++, "%" + material.trim() + "%");
                if (operator != null && !operator.trim().isEmpty()) st.setString(paramIndex++, "%" + operator.trim() + "%");
                if (from != null && !from.isEmpty()) st.setString(paramIndex++, from + " 00:00:00");
                if (to != null && !to.isEmpty()) st.setString(paramIndex++, to + " 23:59:59");
                if (operator != null && !operator.trim().isEmpty()) st.setString(paramIndex++, "%" + operator.trim() + "%");
                if (from != null && !from.isEmpty()) st.setString(paramIndex++, from + " 00:00:00");
                if (to != null && !to.isEmpty()) st.setString(paramIndex++, to + " 23:59:59");
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        InventoryHistoryRow row = new InventoryHistoryRow();
                        row.setType(rs.getString("type"));
                        row.setDate(rs.getTimestamp("date"));
                        row.setCode(rs.getString("code"));
                        row.setMaterialName(rs.getString("material_name"));
                        row.setQuantity(rs.getObject("quantity") != null ? rs.getInt("quantity") : null);
                        row.setUnit(rs.getString("unit"));
                        row.setOperator(rs.getString("operator"));
                        row.setStatus(rs.getString("status"));
                        row.setNote(rs.getString("note"));
                        list.add(row);
                    }
                }
            } catch (SQLException e) { e.printStackTrace(); }
        } else {

        }
        return list;
    }

    public int countInventoryHistory(String type, String material, String from, String to, String operator) {
        int count = 0;
        if (type == null) {
            StringBuilder importWhere = new StringBuilder(" WHERE 1=1");
            if (material != null && !material.trim().isEmpty()) {
                importWhere.append(" AND material_name LIKE ?");
            }
            if (operator != null && !operator.trim().isEmpty()) {
                importWhere.append(" AND received_by LIKE ?");
            }
            if (from != null && !from.isEmpty()) {
                importWhere.append(" AND created_at >= ?");
            }
            if (to != null && !to.isEmpty()) {
                importWhere.append(" AND created_at <= ?");
            }
            importWhere.append(" AND (status = 'Mới' OR status = 'Cũ')");
            String importSql = "SELECT COUNT(*) FROM import_history" + importWhere.toString();
            StringBuilder exportWhere = new StringBuilder(" WHERE 1=1");
            if (material != null && !material.trim().isEmpty()) {
                exportWhere.append(" AND em.material_name LIKE ?");
            }
            if (operator != null && !operator.trim().isEmpty()) {
                exportWhere.append(" AND u2.full_name LIKE ?");
            }
            if (from != null && !from.isEmpty()) {
                exportWhere.append(" AND ef.export_date >= ?");
            }
            if (to != null && !to.isEmpty()) {
                exportWhere.append(" AND ef.export_date <= ?");
            }
            exportWhere.append(" AND ef.status = 'Đã xuất kho'");
            String exportSql = "SELECT COUNT(*) FROM export_forms ef JOIN export_materials em ON ef.export_id = em.export_id JOIN units u ON em.warehouse_unit_id = u.unit_id JOIN users u2 ON ef.user_id = u2.user_id" + exportWhere.toString();
            StringBuilder purchaseWhere = new StringBuilder(" WHERE po.status = 'Approved'");
            if (operator != null && !operator.trim().isEmpty()) {
                purchaseWhere.append(" AND u.full_name LIKE ?");
            }
            if (from != null && !from.isEmpty()) {
                purchaseWhere.append(" AND po.order_date >= ?");
            }
            if (to != null && !to.isEmpty()) {
                purchaseWhere.append(" AND po.order_date <= ?");
            }
            String purchaseSql = "SELECT COUNT(*) FROM purchase_orders po JOIN users u ON po.user_id = u.user_id" + purchaseWhere.toString();
            try (Connection conn = new dal.DBContext().getConnection()) {
                int importCount = 0, exportCount = 0, purchaseCount = 0;
                try (PreparedStatement st = conn.prepareStatement(importSql)) {
                    int paramIndex = 1;
                    if (material != null && !material.trim().isEmpty()) st.setString(paramIndex++, "%" + material.trim() + "%");
                    if (operator != null && !operator.trim().isEmpty()) st.setString(paramIndex++, "%" + operator.trim() + "%");
                    if (from != null && !from.isEmpty()) st.setString(paramIndex++, from + " 00:00:00");
                    if (to != null && !to.isEmpty()) st.setString(paramIndex++, to + " 23:59:59");
                    try (ResultSet rs = st.executeQuery()) { if (rs.next()) importCount = rs.getInt(1); }
                }
                try (PreparedStatement st = conn.prepareStatement(exportSql)) {
                    int paramIndex = 1;
                    if (material != null && !material.trim().isEmpty()) st.setString(paramIndex++, "%" + material.trim() + "%");
                    if (operator != null && !operator.trim().isEmpty()) st.setString(paramIndex++, "%" + operator.trim() + "%");
                    if (from != null && !from.isEmpty()) st.setString(paramIndex++, from + " 00:00:00");
                    if (to != null && !to.isEmpty()) st.setString(paramIndex++, to + " 23:59:59");
                    try (ResultSet rs = st.executeQuery()) { if (rs.next()) exportCount = rs.getInt(1); }
                }
                try (PreparedStatement st = conn.prepareStatement(purchaseSql)) {
                    int paramIndex = 1;
                    if (operator != null && !operator.trim().isEmpty()) st.setString(paramIndex++, "%" + operator.trim() + "%");
                    if (from != null && !from.isEmpty()) st.setString(paramIndex++, from + " 00:00:00");
                    if (to != null && !to.isEmpty()) st.setString(paramIndex++, to + " 23:59:59");
                    try (ResultSet rs = st.executeQuery()) { if (rs.next()) purchaseCount = rs.getInt(1); }
                }
                count = importCount + exportCount + purchaseCount;
            } catch (SQLException e) { e.printStackTrace(); }
        } else {
            // Nếu chỉ 1 loại thì có thể tái sử dụng code trên (tùy nhu cầu)
            // Ở đây chỉ làm cho type == null (cả 3 loại)
        }
        return count;
    }

    public List<InventoryHistoryRow> getInventoryHistoryPaging(String type, String material, String from, String to, String operator, int page, int pageSize) {
        List<InventoryHistoryRow> list = new ArrayList<>();
        if (type == null) {
            List<String> selects = new ArrayList<>();
            StringBuilder importWhere = new StringBuilder(" WHERE 1=1");
            if (material != null && !material.trim().isEmpty()) {
                importWhere.append(" AND material_name LIKE ?");
            }
            if (operator != null && !operator.trim().isEmpty()) {
                importWhere.append(" AND received_by LIKE ?");
            }
            if (from != null && !from.isEmpty()) {
                importWhere.append(" AND created_at >= ?");
            }
            if (to != null && !to.isEmpty()) {
                importWhere.append(" AND created_at <= ?");
            }
            importWhere.append(" AND (status = 'Mới' OR status = 'Cũ')");
            String importSql = "SELECT 'import' AS type, created_at AS date, id AS code, material_name, quantity, unit, received_by AS operator, status, reason AS note FROM import_history" + importWhere.toString();
            StringBuilder exportWhere = new StringBuilder(" WHERE 1=1");
            if (material != null && !material.trim().isEmpty()) {
                exportWhere.append(" AND em.material_name LIKE ?");
            }
            if (operator != null && !operator.trim().isEmpty()) {
                exportWhere.append(" AND u2.full_name LIKE ?");
            }
            if (from != null && !from.isEmpty()) {
                exportWhere.append(" AND ef.export_date >= ?");
            }
            if (to != null && !to.isEmpty()) {
                exportWhere.append(" AND ef.export_date <= ?");
            }
            exportWhere.append(" AND ef.status = 'Đã xuất kho'");
            String exportSql = "SELECT 'export' AS type, ef.export_date AS date, ef.export_id AS code, em.material_name, em.quantity, u.unit_name, u2.full_name AS operator, ef.status, ef.reason AS note FROM export_forms ef JOIN export_materials em ON ef.export_id = em.export_id JOIN units u ON em.warehouse_unit_id = u.unit_id JOIN users u2 ON ef.user_id = u2.user_id" + exportWhere.toString();
            StringBuilder purchaseWhere = new StringBuilder(" WHERE po.status = 'Approved'");
            if (operator != null && !operator.trim().isEmpty()) {
                purchaseWhere.append(" AND u.full_name LIKE ?");
            }
            if (from != null && !from.isEmpty()) {
                purchaseWhere.append(" AND po.order_date >= ?");
            }
            if (to != null && !to.isEmpty()) {
                purchaseWhere.append(" AND po.order_date <= ?");
            }
            String purchaseSql = "SELECT 'purchase' AS type, po.order_date AS date, po.purchase_order_id AS code, NULL AS material_name, NULL AS quantity, NULL AS unit, u.full_name AS operator, po.status, po.note AS note FROM purchase_orders po JOIN users u ON po.user_id = u.user_id" + purchaseWhere.toString();
            selects.add(importSql);
            selects.add(exportSql);
            selects.add(purchaseSql);
            String finalSql = String.join(" UNION ALL ", selects) + " ORDER BY date DESC LIMIT ? OFFSET ?";
            try (Connection conn = new dal.DBContext().getConnection(); PreparedStatement st = conn.prepareStatement(finalSql)) {
                int paramIndex = 1;
                if (material != null && !material.trim().isEmpty()) st.setString(paramIndex++, "%" + material.trim() + "%");
                if (operator != null && !operator.trim().isEmpty()) st.setString(paramIndex++, "%" + operator.trim() + "%");
                if (from != null && !from.isEmpty()) st.setString(paramIndex++, from + " 00:00:00");
                if (to != null && !to.isEmpty()) st.setString(paramIndex++, to + " 23:59:59");
                if (material != null && !material.trim().isEmpty()) st.setString(paramIndex++, "%" + material.trim() + "%");
                if (operator != null && !operator.trim().isEmpty()) st.setString(paramIndex++, "%" + operator.trim() + "%");
                if (from != null && !from.isEmpty()) st.setString(paramIndex++, from + " 00:00:00");
                if (to != null && !to.isEmpty()) st.setString(paramIndex++, to + " 23:59:59");
                if (operator != null && !operator.trim().isEmpty()) st.setString(paramIndex++, "%" + operator.trim() + "%");
                if (from != null && !from.isEmpty()) st.setString(paramIndex++, from + " 00:00:00");
                if (to != null && !to.isEmpty()) st.setString(paramIndex++, to + " 23:59:59");
                st.setInt(paramIndex++, pageSize);
                st.setInt(paramIndex++, (page - 1) * pageSize);
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        InventoryHistoryRow row = new InventoryHistoryRow();
                        row.setType(rs.getString("type"));
                        row.setDate(rs.getTimestamp("date"));
                        row.setCode(rs.getString("code"));
                        row.setMaterialName(rs.getString("material_name"));
                        row.setQuantity(rs.getObject("quantity") != null ? rs.getInt("quantity") : null);
                        row.setUnit(rs.getString("unit"));
                        row.setOperator(rs.getString("operator"));
                        row.setStatus(rs.getString("status"));
                        row.setNote(rs.getString("note"));
                        list.add(row);
                    }
                }
            } catch (SQLException e) { e.printStackTrace(); }
        } else {

        }
        return list;
    }
} 