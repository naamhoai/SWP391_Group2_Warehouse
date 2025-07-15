package dao;

import dal.DBContext;
import model.ExportMaterial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ExportMaterialDAO extends DBContext {

    public void addExportMaterial(ExportMaterial em) throws SQLException {
        String sql = "INSERT INTO export_materials (export_id, material_id, material_name, warehouse_unit_id, quantity, material_condition) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, em.getExportId());
            stmt.setInt(2, em.getMaterialId());
            stmt.setString(3, em.getMaterialName());
            stmt.setInt(4, em.getWarehouseUnitId());
            stmt.setInt(5, em.getQuantity());
            stmt.setString(6, em.getMaterialCondition());
            stmt.executeUpdate();
        }
    }

    public List<ExportMaterial> getExportMaterialsByExportId(int exportId) throws SQLException {
        List<ExportMaterial> list = new ArrayList<>();
        String sql = "   SELECT em.*, u.unit_name\n"
                + "   FROM export_materials em\n"
                + "   JOIN units u ON em.warehouse_unit_id = u.unit_id\n"
                + "   WHERE em.export_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, exportId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ExportMaterial em = new ExportMaterial();
                    em.setExportId(rs.getInt("export_id"));
                    em.setMaterialId(rs.getInt("material_id"));
                    em.setMaterialName(rs.getString("material_name"));
                    em.setWarehouseUnitId(rs.getInt("warehouse_unit_id"));
                    em.setUnitName(rs.getString("unit_name"));
                    em.setQuantity(rs.getInt("quantity"));
                    em.setMaterialCondition(rs.getString("material_condition"));
                    list.add(em);
                }
            }
        }
        return list;
    }

    public Map<Integer, Integer> getExportedQuantitiesByRequestId(int requestId) {
        Map<Integer, Integer> exportedMap = new HashMap<>();
        String sql = "SELECT em.material_id, SUM(em.quantity) as total_exported "
                + "FROM export_materials em "
                + "JOIN export_forms ef ON em.export_id = ef.export_id "
                + "WHERE ef.request_id = ? "
                + "GROUP BY em.material_id";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int materialId = rs.getInt("material_id");
                    int totalExported = rs.getInt("total_exported");
                    exportedMap.put(materialId, totalExported);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exportedMap;
    }

    public List<ExportMaterial> getTopExportedMaterials(int limit) {
        List<ExportMaterial> list = new ArrayList<>();
        String sql = "SELECT em.material_id, em.material_name, SUM(em.quantity) as total_quantity " +
                "FROM export_materials em " +
                "GROUP BY em.material_id, em.material_name " +
                "ORDER BY total_quantity DESC " +
                "LIMIT ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ExportMaterial em = new ExportMaterial();
                    em.setMaterialId(rs.getInt("material_id"));
                    em.setMaterialName(rs.getString("material_name"));
                    em.setQuantity(rs.getInt("total_quantity"));
                    list.add(em);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
