package dao;

import dal.DBContext;
import model.Unit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitDAO extends DBContext {

    public List<Unit> getAllUnits() throws SQLException {
        List<Unit> units = new ArrayList<>();
        String sql = "SELECT unit_id, unit_name, is_system_unit, status FROM units ORDER BY unit_name";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Unit unit = new Unit();
                unit.setUnit_id(rs.getInt("unit_id"));
                unit.setUnit_name(rs.getString("unit_name"));
                unit.setIs_system_unit(rs.getBoolean("is_system_unit"));
                unit.setStatus(rs.getString("status"));
                units.add(unit);
            }
        }
        return units;
    }

    public List<Unit> getWarehouseUnits() throws SQLException {
        List<Unit> units = new ArrayList<>();
        String sql = "SELECT unit_id, unit_name, is_system_unit, status FROM units WHERE is_system_unit = 1 AND status = 'Hoạt động' ORDER BY unit_name";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Unit unit = new Unit();
                unit.setUnit_id(rs.getInt("unit_id"));
                unit.setUnit_name(rs.getString("unit_name"));
                unit.setIs_system_unit(rs.getBoolean("is_system_unit"));
                unit.setStatus(rs.getString("status"));
                units.add(unit);
            }
        }
        return units;
    }

    public List<Unit> getSupplierUnits() throws SQLException {
        List<Unit> units = new ArrayList<>();
        String sql = "SELECT unit_id, unit_name, is_system_unit, status FROM units WHERE is_system_unit = 0 AND status = 'Hoạt động' ORDER BY unit_name";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Unit unit = new Unit();
                unit.setUnit_id(rs.getInt("unit_id"));
                unit.setUnit_name(rs.getString("unit_name"));
                unit.setIs_system_unit(rs.getBoolean("is_system_unit"));
                unit.setStatus(rs.getString("status"));
                units.add(unit);
            }
        }
        return units;
    }

    public Unit getUnitById(int unitId) throws SQLException {
        String sql = "SELECT unit_id, unit_name, is_system_unit, status FROM units WHERE unit_id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, unitId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Unit unit = new Unit();
                unit.setUnit_id(rs.getInt("unit_id"));
                unit.setUnit_name(rs.getString("unit_name"));
                unit.setIs_system_unit(rs.getBoolean("is_system_unit"));
                unit.setStatus(rs.getString("status"));
                return unit;
            }
        }
        return null;
    }

    public boolean addUnit(Unit unit) throws SQLException {
        String sql = "INSERT INTO units (unit_name, is_system_unit, status) VALUES (?, ?, ?)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, unit.getUnit_name());
            ps.setBoolean(2, unit.isIs_system_unit());
            ps.setString(3, unit.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateUnit(Unit unit) throws SQLException {
        String sql = "UPDATE units SET unit_name = ?, is_system_unit = ?, status = ? WHERE unit_id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, unit.getUnit_name());
            ps.setBoolean(2, unit.isIs_system_unit());
            ps.setString(3, unit.getStatus());
            ps.setInt(4, unit.getUnit_id());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteUnit(int unitId) throws SQLException {
        String sql = "DELETE FROM units WHERE unit_id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, unitId);
            return ps.executeUpdate() > 0;
        }
    }
} 