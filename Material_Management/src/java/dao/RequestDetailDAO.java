package dao;

import dal.DBContext;
import model.RequestDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDetailDAO {

    // Thêm chi tiết yêu cầu
    public void addRequestDetail(RequestDetail detail) throws SQLException {
        String sql = "INSERT INTO request_details "
                + "(request_id, material_id, material_name, parent_category_id, category_id, quantity, unit_name, description, material_condition) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detail.getRequestId());
            if (detail.getMaterialId() != null) {
                stmt.setInt(2, detail.getMaterialId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, detail.getMaterialName());
            if (detail.getParentCategoryId() != null) {
                stmt.setInt(4, detail.getParentCategoryId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            if (detail.getCategoryId() != null) {
                stmt.setInt(5, detail.getCategoryId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setInt(6, detail.getQuantity());
            stmt.setString(7, detail.getUnitName());
            stmt.setString(8, detail.getDescription());
            stmt.setString(9, detail.getMaterialCondition());
            stmt.executeUpdate();
        }
    }

    // Lấy chi tiết yêu cầu theo request_id
    public List<RequestDetail> getRequestDetailsByRequestId(int requestId) throws SQLException {
        List<RequestDetail> details = new ArrayList<>();
        String sql = "SELECT rd.*, m.name as material_name, c.name as category_name "
                + "FROM request_details rd "
                + "LEFT JOIN materials m ON rd.material_id = m.material_id "
                + "LEFT JOIN categories c ON rd.category_id = c.category_id "
                + "WHERE rd.request_id = ?";

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
                int parentCatId = rs.getInt("parent_category_id");
                if (rs.wasNull()) {
                    detail.setParentCategoryId(null);
                } else {
                    detail.setParentCategoryId(parentCatId);
                }
                int catId = rs.getInt("category_id");
                if (rs.wasNull()) {
                    detail.setCategoryId(null);
                } else {
                    detail.setCategoryId(catId);
                }
                detail.setQuantity(rs.getInt("quantity"));
                detail.setUnitName(rs.getString("unit_name"));
                detail.setDescription(rs.getString("description"));
                detail.setMaterialCondition(rs.getString("material_condition"));
                details.add(detail);
            }
        }
        return details;
    }

    // Xóa chi tiết yêu cầu
    public boolean deleteRequestDetail(int requestDetailId) throws SQLException {
        String sql = "DELETE FROM request_details WHERE request_detail_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestDetailId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Cập nhật chi tiết yêu cầu
    public boolean updateRequestDetail(RequestDetail detail) throws SQLException {
        String sql = "UPDATE request_details SET material_id = ?, material_name = ?, parent_category_id = ?, category_id = ?, quantity = ?, unit_name = ?, description = ?, material_condition = ? "
                + "WHERE request_detail_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (detail.getMaterialId() != null) {
                stmt.setInt(1, detail.getMaterialId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, detail.getMaterialName());
            if (detail.getParentCategoryId() != null) {
                stmt.setInt(3, detail.getParentCategoryId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            if (detail.getCategoryId() != null) {
                stmt.setInt(4, detail.getCategoryId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, detail.getQuantity());
            stmt.setString(6, detail.getUnitName());
            stmt.setString(7, detail.getDescription());
            stmt.setString(8, detail.getMaterialCondition());
            stmt.setInt(9, detail.getRequestDetailId());
            return stmt.executeUpdate() > 0;
        }
    }

    // Xóa toàn bộ chi tiết vật tư của 1 yêu cầu
    public boolean deleteAllDetailsByRequestId(int requestId) throws SQLException {
        String sql = "DELETE FROM request_details WHERE request_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            return stmt.executeUpdate() > 0;
        }
    }
}
