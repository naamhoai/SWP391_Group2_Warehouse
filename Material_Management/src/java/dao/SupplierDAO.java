package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Supplier;
import dal.DBContext;

public class SupplierDAO {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String query = "SELECT * FROM supplier ORDER BY supplier_id DESC";
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
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
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
            System.out.println("Error: " + e.getMessage());
        } finally {
            closeResources();
        }
        return null;
    }

    public boolean addSupplier(Supplier supplier) {
        String query = "INSERT INTO supplier (supplier_name, contact_person, supplier_phone, address, status) VALUES (?, ?, ?, ?, ?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getContactPerson());
            ps.setString(3, supplier.getSupplierPhone());
            ps.setString(4, supplier.getAddress());
            ps.setString(5, supplier.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            closeResources();
        }
        return false;
    }

    public boolean updateSupplier(Supplier supplier) {
        String query = "UPDATE supplier SET supplier_name=?, contact_person=?, supplier_phone=?, address=?, status=? WHERE supplier_id=?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getContactPerson());
            ps.setString(3, supplier.getSupplierPhone());
            ps.setString(4, supplier.getAddress());
            ps.setString(5, supplier.getStatus());
            ps.setInt(6, supplier.getSupplierId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            closeResources();
        }
        return false;
    }

    public boolean deleteSupplier(int id) {
        String query = "DELETE FROM supplier WHERE supplier_id=?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            closeResources();
        }
        return false;
    }

    public List<Supplier> searchSuppliers(String keyword, String status) {
        List<Supplier> list = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM supplier WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            queryBuilder.append(" AND (supplier_name LIKE ? OR contact_person LIKE ? OR supplier_phone LIKE ? OR address LIKE ?)");
            String searchPattern = "%" + keyword.trim() + "%";
            parameters.add(searchPattern);
            parameters.add(searchPattern);
            parameters.add(searchPattern);
            parameters.add(searchPattern);
        }

        if (status != null && !status.isEmpty()) {
            queryBuilder.append(" AND status = ?");
            parameters.add(status);
        }

        queryBuilder.append(" ORDER BY supplier_id DESC");

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(queryBuilder.toString());

            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
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
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error in searchSuppliers: " + e.getMessage());
        } finally {
            closeResources();
        }
        return list;
    }

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }
} 