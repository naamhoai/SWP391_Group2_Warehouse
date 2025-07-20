/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.*;

/**
 *
 * @author kien3
 */
public class UnitConversionDao extends dal.DBContext {

    public List<UnitConversion> getAllUnit(int pages) {
        List<UnitConversion> list = new ArrayList<>();
        String sql = " SELECT d.conversion_id, u.unit_name, u.status AS unit_status, \n"
                + "d.conversion_factor, d.note, d.status AS conversion_status,\n"
                + "d.supplier_unit_id, \n"
                + "d.warehouse_unit_id,\n"
                + "w.unit_name AS warehouse_unit_name ,\n"
                + "w.unit_id AS warehouse_unit_id\n"
                + "FROM units u \n"
                + "JOIN unit_conversion d ON u.unit_id = d.supplier_unit_id \n"
                + "JOIN units w ON d.warehouse_unit_id = w.unit_id \n"
                + "ORDER BY conversion_id LIMIT 5 OFFSET ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            int offset = (pages - 1) * 5;
            st.setInt(1, offset);
            ResultSet sm = st.executeQuery();
            while (sm.next()) {
                UnitConversion unit = new UnitConversion();
                Unit uni = new Unit();

                unit.setConversionid(sm.getInt("conversion_id"));
                uni.setUnit_id(sm.getInt("warehouse_unit_id"));
                uni.setUnit_name(sm.getString("unit_name"));
                uni.setUnit_namePr(sm.getString("warehouse_unit_name"));
                uni.setStatus(sm.getString("unit_status"));

                unit.setUnits(uni);
                unit.setConversionfactor(sm.getString("conversion_factor"));
                unit.setSupplierUnitId(sm.getInt("supplier_unit_id"));
                unit.setWarehouseunitid(sm.getInt("warehouse_unit_id"));
                unit.setNote(sm.getString("note"));
                unit.setStatus(sm.getString("conversion_status"));

                list.add(unit);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<Material> getMaterial() {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT material_id,name FROM materials;";
        try {
            PreparedStatement ca = connection.prepareStatement(sql);
            ResultSet st = ca.executeQuery();
            while (st.next()) {
                Material mate = new Material();
                mate.setName(st.getString("name"));
                mate.setMaterialId(st.getInt("material_id"));

                list.add(mate);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;

    }

    public List<Category> getAllpre() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE parent_id IS NULL";
        try {
            PreparedStatement ca = connection.prepareStatement(sql);
            ResultSet st = ca.executeQuery();
            while (st.next()) {
                Category cate = new Category();
                cate.setName(st.getString("name"));

                list.add(cate);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;

    }

    public UnitConversion Update(String conversionfactor, int supplierunitid, int warehouseunitid) {
        String sql = "update unit_conversion\n"
                + "set conversion_factor = ?\n"
                + "where supplier_unit_id =?\n"
                + "and warehouse_unit_id = ?";
        try {
            PreparedStatement sm = connection.prepareStatement(sql);

            sm.setString(1, conversionfactor);
            sm.setInt(2, supplierunitid);
            sm.setInt(3, warehouseunitid);
            sm.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    public List<Category> getname() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT name\n"
                + "FROM categories\n"
                + "WHERE parent_id IS NOT NULL;";
        try {
            PreparedStatement ca = connection.prepareStatement(sql);
            ResultSet st = ca.executeQuery();
            while (st.next()) {
                Category cate = new Category();
                cate.setName(st.getString("name"));
                list.add(cate);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public User unitCreate2(String base_unit, String converted_unit, String note, double conversion_factor, int material_id) {
        String sql = "INSERT INTO unit_conversion (base_unit, converted_unit, note,conversion_factor, material_id)\n"
                + "VALUES (?, ? ,?, ?, ?);";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, base_unit);
            st.setString(2, converted_unit);
            st.setString(3, note);
            st.setDouble(4, conversion_factor);
            st.setInt(5, material_id);
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;

    }

    public int getcountPage() {
        String sql = " SELECT COUNT(*) FROM unit_conversion";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int total = rs.getInt(1);
                int countpage = 0;
                countpage = total / 5;
                if (total % 5 != 0) {
                    countpage++;
                }
                return countpage;
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;

    }

    public UnitConversion updateStUnit(String status, int unit_id) {
        String sql = "UPDATE unit_conversion\n"
                + "SET status =?\n"
                + "WHERE conversion_id = ?;";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, status);
            st.setInt(2, unit_id);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;

    }

    public Unit getUnitsconferi(int unit_id) {
        String sql = "UPDATE unit_conversion\n"
                + "SET status =?\n"
                + "WHERE conversion_id = ?;";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, unit_id);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;

    }

    public List<UnitConversion> searchUnit(String unitName, int page) {
        List<UnitConversion> list = new ArrayList<>();
        String sql = "SELECT \n"
                + "d.conversion_id, u.unit_id,u.unit_name,u.status,d.conversion_factor,d.note,d.status,\n"
                + "uw.unit_name AS warehouse_unit_name\n"
                + "FROM units u\n"
                + "JOIN unit_conversion d \n"
                + "ON u.unit_id = d.supplier_unit_id\n"
                + "JOIN units uw \n"
                + "ON uw.unit_id = d.warehouse_unit_id\n"
                + "WHERE u.unit_name LIKE ? \n"
                + "ORDER BY d.conversion_id \n"
                + "LIMIT 5 OFFSET ?;";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            int offset = (page - 1) * 5;
            st.setString(1, "%" + unitName + "%");
            st.setInt(2, offset);
            ResultSet sm = st.executeQuery();
            while (sm.next()) {
                UnitConversion unit = new UnitConversion();
                Unit uni = new Unit();

                unit.setConversionid(sm.getInt("conversion_id"));
                uni.setUnit_id(sm.getInt("unit_id"));
                uni.setUnit_name(sm.getString("unit_name"));
                uni.setUnit_namePr(sm.getString("warehouse_unit_name"));
                uni.setStatus(sm.getString("status"));

                unit.setUnits(uni);
                unit.setConversionfactor(sm.getString("conversion_factor"));
                unit.setNote(sm.getString("note"));
                unit.setStatus(sm.getString("status"));

                list.add(unit);
            }
        } catch (SQLException e) {
            System.out.println("Error in searchUnit(): " + e.getMessage());
        }
        return list;
    }

    public List<Unit> getnameUnit() {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT MIN(unit_id) AS unit_id, unit_name FROM units WHERE is_system_unit != 1 GROUP BY unit_name";
        try {
            PreparedStatement ca = connection.prepareStatement(sql);
            ResultSet st = ca.executeQuery();
            while (st.next()) {
                Unit uni = new Unit();
                uni.setUnit_id(st.getInt("unit_id"));
                uni.setUnit_name(st.getString("unit_name"));
                list.add(uni);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public List<Unit> getnameUnitbase() {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT * FROM units where is_system_unit !=0";
        try {
            PreparedStatement ca = connection.prepareStatement(sql);
            ResultSet st = ca.executeQuery();
            while (st.next()) {
                Unit uni = new Unit();
                uni.setUnit_id(st.getInt("unit_id"));
                uni.setUnit_name(st.getString("unit_name"));
                list.add(uni);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public List<UnitConversion> getALL() {
        List<UnitConversion> list = new ArrayList<>();
        String sql = "SELECT * FROM material_system_5.unit_conversion;";
        try {
            PreparedStatement ca = connection.prepareStatement(sql);
            ResultSet sm = ca.executeQuery();
            while (sm.next()) {
                UnitConversion unit = new UnitConversion();

                unit.setConversionid(sm.getInt("conversion_id"));

                unit.setConversionfactor(sm.getString("conversion_factor"));
                unit.setNote(sm.getString("note"));
                unit.setStatus(sm.getString("status"));

                list.add(unit);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public List<UnitConversion> getConversionFactor(int supplierId, int supplierunitid) {
        List<UnitConversion> list = new ArrayList<>();
        String sql = "SELECT * FROM unit_conversion\n"
                + "where supplier_unit_id = ? and warehouse_unit_id =?;";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, supplierId);
            st.setInt(2, supplierunitid);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                UnitConversion unit = new UnitConversion();
                unit.setSupplierUnitId(rs.getInt("supplier_unit_id"));
                unit.setWarehouseunitid(rs.getInt("warehouse_unit_id"));
                unit.setConversionfactor(rs.getString("conversion_factor"));
                list.add(unit);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;

    }

    public List<Unit> getSupplierUnits() {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT DISTINCT u.unit_id, u.unit_name FROM unit_conversion uc "
                + "JOIN units u ON uc.supplier_unit_id = u.unit_id";
        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Unit u = new Unit();
                u.setUnit_id(rs.getInt("unit_id"));
                u.setUnit_name(rs.getString("unit_name"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<UnitConversion> getBaseUnitsBySupplier(int supplierUnitId) {
        List<UnitConversion> list = new ArrayList<>();
        String sql = "SELECT uc.*, u.unit_name AS warehouse_name FROM unit_conversion uc "
                + "JOIN units u ON uc.warehouse_unit_id = u.unit_id "
                + "WHERE uc.supplier_unit_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, supplierUnitId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                UnitConversion uc = new UnitConversion();

                uc.setWarehouseunitid(rs.getInt("warehouse_unit_id"));
                uc.setConversionfactor(rs.getString("conversion_factor"));
                uc.setStatus(rs.getString("status"));
                Unit baseUnit = new Unit();
                baseUnit.setUnit_id(rs.getInt("warehouse_unit_id"));
                baseUnit.setUnit_name(rs.getString("warehouse_name"));
                uc.setUnits(baseUnit);

                list.add(uc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int unitDesc(String unitName) {
        String sql = "INSERT INTO units (unit_name, is_system_unit) VALUES (?, 0)";
        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, unitName);
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void insertUnitConversion(int supplierUnitId, int warehouseUnitId, String factor, String note, int unitID) {
        String sql = "INSERT INTO unit_conversion (supplier_unit_id, warehouse_unit_id, conversion_factor, note) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {

            int supplierId = (unitID == 0) ? supplierUnitId : unitID;

            st.setInt(1, supplierId);
            st.setInt(2, warehouseUnitId);
            st.setString(3, factor);
            st.setString(4, note);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getUnitIdByName(String unitName) {
        String sql = "SELECT unit_id FROM units WHERE unit_name = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, unitName.trim());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt("unit_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public int isUnitexit(int supplierId, int warehouseId) {
        String sql = "SELECT 1 FROM unit_conversion WHERE supplier_unit_id = ? AND warehouse_unit_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, supplierId);
            st.setInt(2, warehouseId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<UnitChangeHistory> getAllHistories() throws SQLException {
        List<UnitChangeHistory> list = new ArrayList<>();
        String query = "SELECT * FROM unit_change_history ORDER BY changed_at DESC";

        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UnitChangeHistory history = new UnitChangeHistory(
                        rs.getInt("history_id"),
                        rs.getInt("unit_id"),
                        rs.getString("unit_name"),
                        rs.getString("action_type"),
                        rs.getString("old_value"),
                        rs.getString("new_value"),
                        rs.getString("changed_by"),
                        rs.getString("role"),
                        rs.getString("note"),
                        rs.getTimestamp("changed_at")
                );
                list.add(history);
            }
        }
        return list;
    }

    public void insertHistory(UnitChangeHistory history) throws SQLException {
        String sql = "INSERT INTO unit_change_history (unit_id, unit_name, action_type, old_value, new_value, changed_by, role, note, changed_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, history.getUnitId());
            ps.setString(2, history.getUnitName());
            ps.setString(3, history.getActionType());
            ps.setString(4, history.getOldValue());
            ps.setString(5, history.getNewValue());
            ps.setString(6, history.getChangedBy());
            ps.setString(7, history.getRole());
            ps.setString(8, history.getNote());
            ps.setTimestamp(9, history.getChangedAt());
            ps.executeUpdate();
        }
    }

    public String getUnitNameById(int unitId) {
        String sql = "SELECT unit_name FROM units WHERE unit_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, unitId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("unit_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getOldConversionValue(int warehouseUnitId, int supplierUnitId) {
        String sql = "SELECT conversion_factor FROM unit_conversion WHERE warehouse_unit_id = ? AND supplier_unit_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, warehouseUnitId);
            ps.setInt(2, supplierUnitId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("conversion_factor");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy conversion cũ: " + e.getMessage());
        }
        return null;
    }

    public String getUnitName(int unitId) throws SQLException {
        String sql = "SELECT unit_name FROM units WHERE unit_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, unitId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("unit_name");
                }
            }
        }
        return "";
    }

    public String getOldStatus(int unitId) throws SQLException {
        String sql = "SELECT status FROM unit_conversion WHERE conversion_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, unitId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        }
        return "";
    }

    public List<UnitChangeHistory> getFilHistory(String search, String actionType, String role, String date, int page) {
        List<UnitChangeHistory> list = new ArrayList<>();
        int pageSize = 5;
        int offset = (page - 1) * pageSize;

        try {
            String sql = "SELECT * FROM unit_change_history WHERE 1=1";

            if (search != null && !search.isEmpty()) {
                sql += " AND (unit_name LIKE '%" + search + "%' OR changed_by LIKE '%" + search + "%')";
            }

            if (actionType != null && !actionType.isEmpty()) {
                sql += " AND action_type = '" + actionType + "'";
            }

            if (role != null && !role.isEmpty()) {
                sql += " AND role = '" + role + "'";
            }

            if (date != null && !date.isEmpty()) {
                sql += " AND DATE(changed_at) = '" + date + "'";
            }

            sql += " ORDER BY changed_at DESC LIMIT " + pageSize + " OFFSET " + offset;

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UnitChangeHistory h = new UnitChangeHistory();
                h.setHistoryId(rs.getInt("history_id"));
                h.setUnitName(rs.getString("unit_name"));
                h.setActionType(rs.getString("action_type"));
                h.setOldValue(rs.getString("old_value"));
                h.setNewValue(rs.getString("new_value"));
                h.setChangedBy(rs.getString("changed_by"));
                h.setRole(rs.getString("role"));
                h.setNote(rs.getString("note"));
                h.setChangedAt(rs.getTimestamp("changed_at"));
                list.add(h);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countpage(String search, String actionType, String role, String date) {
        int total = 0;

        try {
            String sql = "SELECT COUNT(*) FROM unit_change_history WHERE 1=1";

            if (search != null && !search.isEmpty()) {
                sql += " AND (unit_name LIKE '%" + search + "%' OR changed_by LIKE '%" + search + "%')";
            }

            if (actionType != null && !actionType.isEmpty()) {
                sql += " AND action_type = '" + actionType + "'";
            }

            if (role != null && !role.isEmpty()) {
                sql += " AND role = '" + role + "'";
            }

            if (date != null && !date.isEmpty()) {
                sql += " AND DATE(changed_at) = '" + date + "'";
            }

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        int pageSize = 5;
        return (total + pageSize - 1) / pageSize;
    }

    public List<Material> getALls() {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT m.name, u.unit_name,m.material_id\n"
                + "FROM materials m\n"
                + "JOIN units u ON m.unit_id = u.unit_id";
        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Material mate = new Material();
                mate.setName(rs.getString("name"));
                mate.setUnitName(rs.getString("unit_name"));
                mate.setMaterialId(rs.getInt("material_id"));
                list.add(mate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateQuantity(int materialId, String condition, int quantityChange) {
        String sql = "UPDATE inventory "
                + "SET quantity_on_hand = GREATEST(quantity_on_hand + ?, 0) "
                + "WHERE material_id = ? AND material_condition = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, quantityChange);
            ps.setInt(2, materialId);
            ps.setString(3, condition);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getAllunitCount(int SupplierUnitId, int warehouseunitid) {
        String sql = "SELECT COUNT(*) as unit_id\n"
                + "FROM unit_conversion\n"
                + "wHERE supplier_unit_id = ? and warehouse_unit_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, SupplierUnitId);
            ps.setInt(2, warehouseunitid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("unit_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        UnitConversionDao n = new UnitConversionDao();
        List<Material> k = n.getALls();

        List<UnitConversion> jjj = n.getAllUnit(1);
        System.out.println(k);
//        for (UnitConversion unitConversion : jjj) {
//            System.out.println("sup" + unitConversion.getSupplierUnitId());
//            System.out.println("wa" + unitConversion.getWarehouseunitid());
//        }

//        int f = n.getAllunitCount(999,1);
//        System.out.println(f);
//        
    }

}
