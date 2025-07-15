package dao;

import dal.DBContext;
import model.RequestDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDetailDAO {

    public void addRequestDetail(RequestDetail detail) throws SQLException {
        String sql = "INSERT INTO request_details (request_id, material_id, material_name, quantity, warehouse_unit_id, material_condition) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detail.getRequestId());
            if (detail.getMaterialId() != null) {
                stmt.setInt(2, detail.getMaterialId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, detail.getMaterialName());
            stmt.setInt(4, detail.getQuantity());
            stmt.setInt(5, detail.getWarehouseUnitId());
            stmt.setString(6, detail.getMaterialCondition());
            stmt.executeUpdate();
        }
    }

    public List<RequestDetail> getRequestDetailsByRequestId(int requestId) throws SQLException {
        List<RequestDetail> details = new ArrayList<>();
        String sql = "SELECT rd.*, u.unit_name FROM request_details rd JOIN units u ON rd.warehouse_unit_id = u.unit_id WHERE rd.request_id = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RequestDetail detail = new RequestDetail();
                detail.setRequestDetailId(rs.getInt("request_detail_id"));
                detail.setRequestId(rs.getInt("request_id"));
                int materialId = rs.getInt("material_id");
                if (rs.wasNull()) {
                    detail.setMaterialId(null);
                } else {
                    detail.setMaterialId(materialId);
                }
                detail.setMaterialName(rs.getString("material_name"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setWarehouseUnitId(rs.getInt("warehouse_unit_id"));
                detail.setMaterialCondition(rs.getString("material_condition"));
                detail.setUnitName(rs.getString("unit_name"));
                details.add(detail);
            }
        }
        return details;
    }

    public boolean deleteAllDetailsByRequestId(int requestId) throws SQLException {
        String sql = "DELETE FROM request_details WHERE request_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            return stmt.executeUpdate() > 0;
        }
    }
}
