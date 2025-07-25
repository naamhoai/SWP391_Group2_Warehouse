package dao;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    private Throwable lastException;

    public Throwable getLastException() {
        return lastException;
    }

    
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing resources: {0}", e.getMessage());
        }
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String query = "SELECT * FROM supplier ORDER BY created_at DESC";
        try {
            conn = getConnection();
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
                    rs.getDate("start_date"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getAllSuppliers: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return list;
    }
    
    public List<Supplier> getActiveSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String query = "SELECT * FROM supplier WHERE status = 'active' ORDER BY created_at DESC";
        try {
            conn = getConnection();
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
                    rs.getDate("start_date"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getActiveSuppliers: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return list;
    }
   
    public Supplier getSupplierById(int id) {
        String query = "SELECT * FROM supplier WHERE supplier_id = ?";
        try {
            conn = getConnection();
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
                    rs.getDate("start_date"),
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
        String query = "UPDATE supplier SET supplier_name=?, contact_person=?, supplier_phone=?, address=?, status=?, status_reason=? WHERE supplier_id=?";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getContactPerson());
            ps.setString(3, supplier.getSupplierPhone());
            ps.setString(4, supplier.getAddress());
            ps.setString(5, supplier.getStatus());
            ps.setString(6, supplier.getStatusReason());
            ps.setInt(7, supplier.getSupplierId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in updateSupplier: {0}", e.getMessage());
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
            String status = supplier.getStatus();
            if (status == null || (!status.equals("active") && !status.equals("inactive") && !status.equals("terminated"))) {
                status = "active";
            }
            ps.setString(5, status); 
            int rowsAffected = ps.executeUpdate(); 
            lastException = null;
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in addSupplier: {0}", e.getMessage());
            e.printStackTrace();
            lastException = e;
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
                    rs.getDate("start_date"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                ));
            }
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
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, supplierId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi kiểm tra nhà cung cấp đang sử dụng: {0}", e.getMessage());
            return true;
        } finally {
            closeResources();
        }
    }

    
    public int countSuppliers(String keyword, String status) {
        String query = "SELECT COUNT(*) FROM supplier WHERE (? IS NULL OR supplier_name LIKE ?) AND (? IS NULL OR status = ?)";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, keyword);
            ps.setString(2, keyword == null ? null : "%" + keyword + "%");
            ps.setString(3, status);
            ps.setString(4, status);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in countSuppliers: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return 0;
    }

    
    public List<Supplier> getSuppliersWithPaging(String keyword, String status, String sortBy, String sortOrder, int page, int itemsPerPage) {
        List<Supplier> list = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM supplier WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.isEmpty()) {
            // Chuẩn hóa keyword: trim, nhiều khoảng trắng thành 1, thay khoảng trắng thành %
            keyword = keyword.trim().replaceAll("\\s+", " ").replace(" ", "%");
            query.append(" AND (supplier_name LIKE ? OR contact_person LIKE ? OR supplier_phone LIKE ? OR address LIKE ?)");
            String searchPattern = "%" + keyword + "%";
            for (int i = 0; i < 4; i++) params.add(searchPattern);
        }
        if (status != null && !status.isEmpty()) {
            query.append(" AND status = ?");
            params.add(status);
        }
        if (sortBy != null && !sortBy.isEmpty()) {
            query.append(" ORDER BY ").append(sortBy);
            if (sortOrder != null && !sortOrder.isEmpty()) {
                query.append(" ").append(sortOrder);
            }
        } else {
            query.append(" ORDER BY created_at DESC");
        }
        query.append(" LIMIT ? OFFSET ?");
        params.add(itemsPerPage);
        params.add((page - 1) * itemsPerPage);
        try {
            conn = getConnection();
            ps = conn.prepareStatement(query.toString());
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof String) {
                    ps.setString(i + 1, (String) params.get(i));
                } else if (params.get(i) instanceof Integer) {
                    ps.setInt(i + 1, (Integer) params.get(i));
                }
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getString("contact_person"),
                    rs.getString("supplier_phone"),
                    rs.getString("address"),
                    rs.getString("status"),
                    rs.getDate("start_date"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getSuppliersWithPaging: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return list;
    }

    // Kiểm tra nhà cung cấp còn vật tư tồn kho không
    public boolean hasMaterialsInStock(int supplierId) {
        String query = "SELECT COUNT(*) FROM material WHERE supplier_id = ? AND quantity > 0";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, supplierId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in hasMaterialsInStock: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return false;
    }

    public Supplier getSupplierByName(String name) {
        String query = "SELECT * FROM supplier WHERE supplier_name = ?";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getString("contact_person"),
                    rs.getString("supplier_phone"),
                    rs.getString("address"),
                    rs.getString("status"),
                    rs.getDate("start_date"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getSupplierByName: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return null;
    }

    public int addSupplierAndReturnId(Supplier supplier) {
        String query = "INSERT INTO supplier (supplier_name, contact_person, supplier_phone, address, status) VALUES (?, ?, ?, ?, ?)";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getContactPerson());
            ps.setString(3, supplier.getSupplierPhone());
            ps.setString(4, supplier.getAddress());
            String status = supplier.getStatus();
            if (status == null || (!status.equals("active") && !status.equals("inactive") && !status.equals("terminated"))) {
                status = "active";
            }
            ps.setString(5, status);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in addSupplierAndReturnId: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return -1;
    }

    /**
     * Cập nhật start_date và status cho supplier nếu đang là inactive
     */
    public boolean activateSupplierIfInactive(int supplierId, java.sql.Date approvedDate) {
        String query = "UPDATE supplier SET status = 'active', start_date = ? WHERE supplier_id = ? AND status = 'inactive'";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setDate(1, approvedDate);
            ps.setInt(2, supplierId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in activateSupplierIfInactive: {0}", e.getMessage());
            return false;
        } finally {
            closeResources();
        }
    }
} 