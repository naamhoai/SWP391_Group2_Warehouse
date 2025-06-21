package dao;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExportFormDAO extends DBContext {
    public boolean existsExportFormByRequestId(int requestId) throws SQLException {
        String sql = "SELECT 1 FROM export_forms WHERE request_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
} 