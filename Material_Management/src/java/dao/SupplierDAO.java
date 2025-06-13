package dao;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Supplier;

public class SupplierDAO extends DBContext {
    private static final Logger LOGGER = Logger.getLogger(SupplierDAO.class.getName());
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private void closeResources() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing resources: {0}", e.getMessage());
        }
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String query = "SELECT * FROM supplier ORDER BY created_at DESC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getString("contact_person"),
                    rs.getString("supplier_phone"),
                    rs.getString("address"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                ));
            }
            LOGGER.log(Level.INFO, "Retrieved {0} suppliers", list.size());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getAllSuppliers: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return list;
    }
    
    public Supplier getSupplierById(int id) {
        String query = "SELECT * FROM supplier WHERE supplier_id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getString("contact_person"),
                    rs.getString("supplier_phone"),
                    rs.getString("address"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getSupplierById: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return null;
    }
    
    public boolean updateSupplier(Supplier supplier) {
        String query = "UPDATE supplier SET supplier_name=?, contact_person=?, supplier_phone=?, address=?, status=? WHERE supplier_id=?";
        try {
            conn = new DBContext().getConnection();
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Không thể kết nối đến database");
                return false;
            }

            LOGGER.log(Level.INFO, "Chuẩn bị cập nhật supplier với ID: {0}", supplier.getSupplierId());
            
            // Validate status
            String status = supplier.getStatus();
            if (status == null || (!status.equals("active") && !status.equals("inactive"))) {
                LOGGER.log(Level.WARNING, "Status không hợp lệ: {0}. Sử dụng giá trị mặc định 'active'", status);
                status = "active";
            }

            ps = conn.prepareStatement(query);
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getContactPerson());
            ps.setString(3, supplier.getSupplierPhone());
            ps.setString(4, supplier.getAddress());
            ps.setString(5, status);
            ps.setInt(6, supplier.getSupplierId());
            
            LOGGER.log(Level.INFO, "Executing query: {0}", query);
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Cập nhật thành công supplier với ID: {0}", supplier.getSupplierId());
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Không có dòng nào được cập nhật cho supplier ID: {0}", supplier.getSupplierId());
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi cập nhật supplier: {0}", e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi không mong đợi khi cập nhật supplier: {0}", e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    public boolean addSupplier(Supplier supplier) {
        String query = "INSERT INTO supplier (supplier_name, contact_person, supplier_phone, address, status) VALUES (?, ?, ?, ?, ?)";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getContactPerson());
            ps.setString(3, supplier.getSupplierPhone());
            ps.setString(4, supplier.getAddress());
            // Đảm bảo status luôn là "active" hoặc "inactive"
            String status = supplier.getStatus();
            if (status == null || (!status.equals("active") && !status.equals("inactive"))) {
                status = "active"; // Giá trị mặc định
            }
            ps.setString(5, status);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in addSupplier: {0}", e.getMessage());
            return false;
        } finally {
            closeResources();
        }
    }

    public boolean deleteSupplier(int id) {
        String query = "DELETE FROM supplier WHERE supplier_id=?";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in deleteSupplier: {0}", e.getMessage());
            return false;
        } finally {
            closeResources();
        }
    }

    public List<Supplier> searchSuppliers(String keyword) {
        List<Supplier> list = new ArrayList<>();
        String query = "SELECT * FROM supplier WHERE supplier_name LIKE ? OR contact_person LIKE ? OR supplier_phone LIKE ? OR address LIKE ? ORDER BY created_at DESC";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getString("contact_person"),
                    rs.getString("supplier_phone"),
                    rs.getString("address"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                ));
            }
            LOGGER.log(Level.INFO, "Found {0} suppliers matching keyword: {1}", 
                new Object[]{list.size(), keyword});
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in searchSuppliers: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return list;
    }

    public boolean isSupplierInUse(int supplierId) {
        String query = "SELECT COUNT(*) FROM material WHERE supplier_id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, supplierId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi kiểm tra nhà cung cấp đang sử dụng: {0}", e.getMessage());
            return true; // Trả về true để đảm bảo an toàn
        } finally {
            closeResources();
        }
    }
} 