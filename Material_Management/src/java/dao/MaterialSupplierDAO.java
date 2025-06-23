package dao;

import dal.DBContext;
import model.MaterialSupplier;
import java.sql.*;
import java.util.*;

public class MaterialSupplierDAO {
    public List<MaterialSupplier> getMaterialsBySupplierId(int supplierId) {
        List<MaterialSupplier> list = new ArrayList<>();
        String sql = "SELECT m.material_id, m.name as material_name, s.supplier_id, s.supplier_name " +
                     "FROM materials m JOIN supplier s ON m.supplier_id = s.supplier_id " +
                     "WHERE s.supplier_id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supplierId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MaterialSupplier ms = new MaterialSupplier();
                ms.setMaterialId(rs.getInt("material_id"));
                ms.setMaterialName(rs.getString("material_name"));
                ms.setSupplierId(rs.getInt("supplier_id"));
                ms.setSupplierName(rs.getString("supplier_name"));
                list.add(ms);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
} 