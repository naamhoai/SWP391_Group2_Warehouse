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
                // Chuyển từng dòng dữ liệu thành đối tượng Supplier và thêm vào list
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
            LOGGER.log(Level.INFO, "Retrieved {0} suppliers", list.size());
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
                    rs.getDate("start_date"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                ));
            }
            LOGGER.log(Level.INFO, "Retrieved {0} active suppliers", list.size());
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
        String query = "UPDATE supplier SET supplier_name=?, contact_person=?, supplier_phone=?, address=?, status=? WHERE supplier_id=?";
        try {
            conn = new DBContext().getConnection();
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Không thể kết nối đến database");
                return false;
            }

            LOGGER.log(Level.INFO, "Chuẩn bị cập nhật supplier với ID: {0}", supplier.getSupplierId());
            
            String status = supplier.getStatus();
          
            if (status == null || (!status.equals("active") && !status.equals("inactive") && !status.equals("terminated"))) {
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
            String status = supplier.getStatus();
            
            if (status == null || (!status.equals("active") && !status.equals("inactive") && !status.equals("terminated"))) {
                status = "active";
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
        List<Supplier> list = new ArrayList<>(); // Danh sách kết quả
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
            return true; 
        } finally {
            closeResources();
        }
    }

    
    public int countSuppliers(String keyword, String status) {
        int total = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) as total FROM supplier WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (supplier_name LIKE ? OR contact_person LIKE ? OR supplier_phone LIKE ? OR address LIKE ?)");
            String searchPattern = "%" + keyword.trim() + "%";
            for (int i = 0; i < 4; i++) params.add(searchPattern);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
            params.add(status);
        }
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in countSuppliers: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return total;
    }

    
    public List<Supplier> getSuppliersWithPaging(String keyword, String status, String sortBy, String sortOrder, int page, int itemsPerPage) {
        List<Supplier> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM supplier WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (supplier_name LIKE ? OR contact_person LIKE ? OR supplier_phone LIKE ? OR address LIKE ?)");
            String searchPattern = "%" + keyword.trim() + "%";
            for (int i = 0; i < 4; i++) params.add(searchPattern);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
            params.add(status);
        }
       
        if (sortBy != null) {
            String orderDirection = (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) ? "DESC" : "ASC";
            switch (sortBy) {
                case "name":
                    sql.append(" ORDER BY supplier_name ").append(orderDirection);
                    break;
                case "id":
                    sql.append(" ORDER BY supplier_id ").append(orderDirection);
                    break;
                default:
                    sql.append(" ORDER BY created_at DESC");
            }
        } else {
            sql.append(" ORDER BY created_at DESC");
        }
        sql.append(" LIMIT ? OFFSET ?");
        params.add(itemsPerPage);
        params.add((page - 1) * itemsPerPage);
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
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
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, supplierId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi kiểm tra vật tư tồn kho của supplier: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return false;
    }

    public Supplier getSupplierByName(String name) {
        String sql = "SELECT * FROM supplier WHERE LOWER(supplier_name) = LOWER(?) LIMIT 1";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, name.trim());
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
        String query = "INSERT INTO supplier (supplier_name, status) VALUES (?, ?)";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getStatus());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
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
    public boolean activateSupplierIfInactive(int supplierId) {
        String sql = "UPDATE supplier SET start_date = CURDATE(), status = 'active' WHERE supplier_id = ? AND status = 'inactive' AND (start_date IS NULL OR start_date = '')";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, supplierId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật start_date và status cho supplier: {0}", e.getMessage());
            return false;
        } finally {
            closeResources();
        }
    }
} 