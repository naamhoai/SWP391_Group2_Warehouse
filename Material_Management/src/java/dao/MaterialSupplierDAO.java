package dao;

import dal.DBContext;
import model.MaterialSupplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MaterialSupplierDAO {
    private static final Logger LOGGER = Logger.getLogger(MaterialSupplierDAO.class.getName());
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public MaterialSupplierDAO() {  
    }

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing resources: {0}", e.getMessage());
        }
    }

    
    public List<MaterialSupplier> getAllMaterialSuppliers() {
        List<MaterialSupplier> list = new ArrayList<>();
        String sql = "SELECT ms.*, m.name as material_name, s.supplier_name " +
                     "FROM material_suppliers ms " +
                     "JOIN materials m ON ms.material_id = m.material_id " +
                     "JOIN supplier s ON ms.supplier_id = s.supplier_id " +
                     "ORDER BY ms.material_supplier_id DESC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                MaterialSupplier ms = new MaterialSupplier();
                ms.setMaterialSupplierId(rs.getInt("material_supplier_id"));
                ms.setMaterialId(rs.getInt("material_id"));
                ms.setMaterialName(rs.getString("material_name"));
                ms.setSupplierId(rs.getInt("supplier_id"));
                ms.setSupplierName(rs.getString("supplier_name"));
                ms.setStatus(rs.getString("status"));
                list.add(ms);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getAllMaterialSuppliers: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return list;
    }

    // Lấy material-supplier theo ID
    public MaterialSupplier getMaterialSupplierById(int id) {
        String sql = "SELECT ms.*, m.name as material_name, s.supplier_name " +
                     "FROM material_suppliers ms " +
                     "JOIN materials m ON ms.material_id = m.material_id " +
                     "JOIN supplier s ON ms.supplier_id = s.supplier_id " +
                     "WHERE ms.material_supplier_id = ?";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                MaterialSupplier ms = new MaterialSupplier();
                ms.setMaterialSupplierId(rs.getInt("material_supplier_id"));
                ms.setMaterialId(rs.getInt("material_id"));
                ms.setMaterialName(rs.getString("material_name"));
                ms.setSupplierId(rs.getInt("supplier_id"));
                ms.setSupplierName(rs.getString("supplier_name"));
                ms.setStatus(rs.getString("status"));
                return ms;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getMaterialSupplierById: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return null;
    }

    // Thêm material-supplier relationship mới
    public boolean addMaterialSupplier(MaterialSupplier ms) {
        String sql = "INSERT INTO material_suppliers (material_id, supplier_id, status) VALUES (?, ?, ?)";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ms.getMaterialId());
            ps.setInt(2, ms.getSupplierId());
            ps.setString(3, ms.getStatus());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in addMaterialSupplier: {0}", e.getMessage());
            return false;
        } finally {
            closeResources();
        }
    }

    // Cập nhật material-supplier relationship
    public boolean updateMaterialSupplier(MaterialSupplier ms) {
        String sql = "UPDATE material_suppliers SET material_id = ?, supplier_id = ?, status = ? " +
                     "WHERE material_supplier_id = ?";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ms.getMaterialId());
            ps.setInt(2, ms.getSupplierId());
            ps.setString(3, ms.getStatus());
            ps.setInt(4, ms.getMaterialSupplierId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in updateMaterialSupplier: {0}", e.getMessage());
            return false;
        } finally {
            closeResources();
        }
    }

    // Xóa material-supplier relationship
    public boolean deleteMaterialSupplier(int id) {
        String sql = "DELETE FROM material_suppliers WHERE material_supplier_id = ?";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in deleteMaterialSupplier: {0}", e.getMessage());
            return false;
        } finally {
            closeResources();
        }
    }

    // Tìm kiếm material-supplier theo tên material hoặc supplier
    public List<MaterialSupplier> searchMaterialSuppliers(String keyword) {
        List<MaterialSupplier> list = new ArrayList<>();
        String sql = "SELECT ms.*, m.name as material_name, s.supplier_name " +
                     "FROM material_suppliers ms " +
                     "JOIN materials m ON ms.material_id = m.material_id " +
                     "JOIN supplier s ON ms.supplier_id = s.supplier_id " +
                     "WHERE m.name LIKE ? OR s.supplier_name LIKE ? " +
                     "ORDER BY ms.material_supplier_id DESC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                MaterialSupplier ms = new MaterialSupplier();
                ms.setMaterialSupplierId(rs.getInt("material_supplier_id"));
                ms.setMaterialId(rs.getInt("material_id"));
                ms.setMaterialName(rs.getString("material_name"));
                ms.setSupplierId(rs.getInt("supplier_id"));
                ms.setSupplierName(rs.getString("supplier_name"));
                ms.setStatus(rs.getString("status"));
                list.add(ms);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in searchMaterialSuppliers: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return list;
    }

    // Lấy material-supplier theo supplier ID
    public List<MaterialSupplier> getMaterialSuppliersBySupplierId(int supplierId) {
        List<MaterialSupplier> list = new ArrayList<>();
        String sql = "SELECT ms.*, m.name as material_name, s.supplier_name " +
                     "FROM material_suppliers ms " +
                     "JOIN materials m ON ms.material_id = m.material_id " +
                     "JOIN supplier s ON ms.supplier_id = s.supplier_id " +
                     "WHERE ms.supplier_id = ? " +
                     "ORDER BY ms.material_supplier_id DESC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, supplierId);
            rs = ps.executeQuery();

            while (rs.next()) {
                MaterialSupplier ms = new MaterialSupplier();
                ms.setMaterialSupplierId(rs.getInt("material_supplier_id"));
                ms.setMaterialId(rs.getInt("material_id"));
                ms.setMaterialName(rs.getString("material_name"));
                ms.setSupplierId(rs.getInt("supplier_id"));
                ms.setSupplierName(rs.getString("supplier_name"));
                ms.setStatus(rs.getString("status"));
                list.add(ms);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getMaterialSuppliersBySupplierId: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return list;
    }

    // Lấy material-supplier theo material ID
    public List<MaterialSupplier> getMaterialSuppliersByMaterialId(int materialId) {
        List<MaterialSupplier> list = new ArrayList<>();
        String sql = "SELECT ms.*, m.name as material_name, s.supplier_name " +
                     "FROM material_suppliers ms " +
                     "JOIN materials m ON ms.material_id = m.material_id " +
                     "JOIN supplier s ON ms.supplier_id = s.supplier_id " +
                     "WHERE ms.material_id = ? " +
                     "ORDER BY ms.material_supplier_id DESC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, materialId);
            rs = ps.executeQuery();

            while (rs.next()) {
                MaterialSupplier ms = new MaterialSupplier();
                ms.setMaterialSupplierId(rs.getInt("material_supplier_id"));
                ms.setMaterialId(rs.getInt("material_id"));
                ms.setMaterialName(rs.getString("material_name"));
                ms.setSupplierId(rs.getInt("supplier_id"));
                ms.setSupplierName(rs.getString("supplier_name"));
                ms.setStatus(rs.getString("status"));
                list.add(ms);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getMaterialSuppliersByMaterialId: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return list;
    }

    // Lấy material-supplier theo status
    public List<MaterialSupplier> getMaterialSuppliersByStatus(String status) {
        List<MaterialSupplier> list = new ArrayList<>();
        String sql = "SELECT ms.*, m.name as material_name, s.supplier_name " +
                     "FROM material_suppliers ms " +
                     "JOIN materials m ON ms.material_id = m.material_id " +
                     "JOIN supplier s ON ms.supplier_id = s.supplier_id " +
                     "WHERE ms.status = ? " +
                     "ORDER BY ms.material_supplier_id DESC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            rs = ps.executeQuery();

            while (rs.next()) {
                MaterialSupplier ms = new MaterialSupplier();
                ms.setMaterialSupplierId(rs.getInt("material_supplier_id"));
                ms.setMaterialId(rs.getInt("material_id"));
                ms.setMaterialName(rs.getString("material_name"));
                ms.setSupplierId(rs.getInt("supplier_id"));
                ms.setSupplierName(rs.getString("supplier_name"));
                ms.setStatus(rs.getString("status"));
                list.add(ms);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getMaterialSuppliersByStatus: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return list;
    }
}
