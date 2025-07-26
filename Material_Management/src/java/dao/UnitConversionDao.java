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

    private Connection conn;

    public UnitConversionDao(Connection conn) {
        this.conn = conn;
    }

    public UnitConversionDao() {
    }

   
    public List<Unit> getAllUnits(int page, int pageSize) {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT unit_id, unit_name, status FROM units LIMIT ? OFFSET ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, pageSize);
            st.setInt(2, (page - 1) * pageSize);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Unit unit = new Unit();
                unit.setUnit_id(rs.getInt("unit_id"));
                unit.setUnit_name(rs.getString("unit_name"));
                unit.setStatus(rs.getString("status"));
                list.add(unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countAllUnits() {
        String sql = "SELECT COUNT(*) FROM units";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Unit> searchUnitsByName(String keyword, int page, int pageSize) {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT unit_id, unit_name, status FROM units WHERE unit_name LIKE ? LIMIT ? OFFSET ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, "%" + keyword + "%");
            st.setInt(2, pageSize);
            st.setInt(3, (page - 1) * pageSize);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Unit unit = new Unit();
                unit.setUnit_id(rs.getInt("unit_id"));
                unit.setUnit_name(rs.getString("unit_name"));
                unit.setStatus(rs.getString("status"));
                list.add(unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countUnitsByName(String keyword) {
        String sql = "SELECT COUNT(*) FROM units WHERE unit_name LIKE ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, "%" + keyword + "%");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addUnit(String unitName) {
        String sql = "INSERT INTO units (unit_name, status, is_system_unit) VALUES (?, 'Hoạt động', 1)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, unitName);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void toggleUnitStatus(int unitId) {
        String sql = "UPDATE units SET status = IF(status='Hoạt động','Không hoạt động','Hoạt động') WHERE unit_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, unitId);
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

    public void addUnitWithStatus(String unitName, String status) throws SQLException {
        String sql = "INSERT INTO units (unit_name, status) VALUES (?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, unitName);
            st.setString(2, status);
            st.executeUpdate();
        }
    }

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

   





    public int getUnitIdByNames(String unitName) {
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
        // Kiểm tra xem record có tồn tại không
        String checkSql = "SELECT COUNT(*) FROM inventory WHERE material_id = ? AND material_condition = ?";
        try (PreparedStatement checkPs = connection.prepareStatement(checkSql)) {
            checkPs.setInt(1, materialId);
            checkPs.setString(2, condition);
            ResultSet rs = checkPs.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                // Nếu chưa có record, tạo mới
                String insertSql = "INSERT INTO inventory (material_id, material_condition, quantity_on_hand) VALUES (?, ?, ?)";
                try (PreparedStatement insertPs = connection.prepareStatement(insertSql)) {
                    insertPs.setInt(1, materialId);
                    insertPs.setString(2, condition);
                    insertPs.setInt(3, quantityChange);
                    return insertPs.executeUpdate() > 0;
                }
            } else {
                // Nếu đã có record, update
                String updateSql = "UPDATE inventory SET quantity_on_hand = GREATEST(quantity_on_hand + ?, 0) WHERE material_id = ? AND material_condition = ?";
                try (PreparedStatement updatePs = connection.prepareStatement(updateSql)) {
                    updatePs.setInt(1, quantityChange);
                    updatePs.setInt(2, materialId);
                    updatePs.setString(3, condition);
                    return updatePs.executeUpdate() > 0;
                }
            }
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

    public List<String> getExportedProjects() {
        List<String> projects = new ArrayList<>();
        String sql = "SELECT DISTINCT recipient_name FROM export_forms ";
        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                projects.add(rs.getString("recipient_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public List<Material> getMaterialsByProject(String projectName) {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT m.material_id, m.name, u.unit_name "
                + "FROM export_forms ef "
                + "JOIN export_materials em ON ef.export_id = em.export_id "
                + "JOIN materials m ON em.material_id = m.material_id "
                + "JOIN units u ON em.warehouse_unit_id = u.unit_id "
                + "WHERE ef.recipient_name = ? ";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, projectName);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Material mate = new Material();
                mate.setMaterialId(rs.getInt("material_id"));
                mate.setName(rs.getString("name"));
                mate.setUnitName(rs.getString("unit_name"));
                list.add(mate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy thông tin vật tư theo id
    public Material getMaterialById(int materialId) {
        String sql = "SELECT m.material_id, m.name, u.unit_name FROM materials m JOIN units u ON m.unit_id = u.unit_id WHERE m.material_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, materialId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Material mate = new Material();
                mate.setMaterialId(rs.getInt("material_id"));
                mate.setName(rs.getString("name"));
                mate.setUnitName(rs.getString("unit_name"));
                return mate;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm một dòng vào bảng import_history
    public void insertImportHistory(ImportHistory history) {
        String sql = "INSERT INTO import_history (roles, reason, delivered_by, received_by, delivery_phone, project_name, material_name, quantity, unit, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, history.getRoles());
            st.setString(2, history.getReason());
            st.setString(3, history.getDeliveredBy());
            st.setString(4, history.getReceivedBy());
            st.setString(5, history.getDeliveryPhone());
            st.setString(6, history.getProjectName());
            st.setString(7, history.getMaterialName());
            st.setInt(8, history.getQuantity());
            st.setString(9, history.getUnit());
            st.setString(10, history.getStatus());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ImportHistory> getImportHistoryList() {
        List<ImportHistory> list = new ArrayList<>();
        String sql = "SELECT \n"
                + "  MIN(id) as id, \n"
                + "  project_name, \n"
                + "  MIN(reason) as reason, \n"
                + "  MIN(created_at) as created_at, \n"
                + "  MIN(status) as status, \n"
                + "  COUNT(*) as total_items\n"
                + "FROM import_history\n"
                + "GROUP BY project_name, DATE(created_at), reason\n"
                + "ORDER BY MIN(created_at) DESC;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ImportHistory summary = new ImportHistory();
                summary.setId(rs.getInt("id"));
                summary.setProjectName(rs.getString("project_name"));
                summary.setReason(rs.getString("reason"));
                summary.setCreatedAt(rs.getTimestamp("created_at"));
                summary.setStatus(rs.getString("status"));

                list.add(summary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy chi tiết phiếu nhập (danh sách vật tư) theo import_id
     */
    public List<ImportHistory> getImportHistoryDetail(int importId) {
        List<ImportHistory> list = new ArrayList<>();
        // Lấy tất cả vật tư có cùng project_name, created_at, reason với import_id này
        String sql = "SELECT * FROM import_history WHERE project_name = (SELECT project_name FROM import_history WHERE id = ?) " +
                    "AND DATE(created_at) = (SELECT DATE(created_at) FROM import_history WHERE id = ?) " +
                    "AND reason = (SELECT reason FROM import_history WHERE id = ?) " +
                    "ORDER BY id ASC";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, importId);
            st.setInt(2, importId);
            st.setInt(3, importId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ImportHistory h = new ImportHistory();
                h.setId(rs.getInt("id"));
                h.setRoles(rs.getString("roles"));
                h.setReason(rs.getString("reason"));
                h.setDeliveredBy(rs.getString("delivered_by"));
                h.setReceivedBy(rs.getString("received_by"));
                h.setDeliveryPhone(rs.getString("delivery_phone"));
                h.setProjectName(rs.getString("project_name"));
                h.setMaterialName(rs.getString("material_name"));
                h.setQuantity(rs.getInt("quantity"));
                h.setUnit(rs.getString("unit"));
                h.setStatus(rs.getString("status"));
                h.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách import history với lọc và phân trang
     */
    public List<ImportHistory> getImportHistoryListFiltered(String projectName, String createdDate, int page, int pageSize) {
        List<ImportHistory> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT MIN(id) as id, project_name, MIN(reason) as reason, ");
        sql.append("MIN(created_at) as created_at, MIN(status) as status, COUNT(*) as total_items ");
        sql.append("FROM import_history WHERE 1=1");
        
        List<Object> params = new ArrayList<>();
        int paramIndex = 1;
        
        if (projectName != null && !projectName.trim().isEmpty()) {
            sql.append(" AND project_name LIKE ?");
            params.add("%" + projectName.trim() + "%");
        }
        
        if (createdDate != null && !createdDate.trim().isEmpty()) {
            sql.append(" AND DATE(created_at) = ?");
            params.add(createdDate.trim());
        }
        
        sql.append(" GROUP BY project_name, DATE(created_at), reason ");
        sql.append("ORDER BY MIN(created_at) DESC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add(offset);
        
        try (PreparedStatement st = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ImportHistory summary = new ImportHistory();
                summary.setId(rs.getInt("id"));
                summary.setProjectName(rs.getString("project_name"));
                summary.setReason(rs.getString("reason"));
                summary.setCreatedAt(rs.getTimestamp("created_at"));
                summary.setStatus(rs.getString("status"));
                list.add(summary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Đếm tổng số trang cho phân trang
     */
    public int countImportHistoryFiltered(String projectName, String createdDate) {
        int total = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(DISTINCT CONCAT(project_name, DATE(created_at), reason)) as total ");
        sql.append("FROM import_history WHERE 1=1");
        
        List<Object> params = new ArrayList<>();
        
        if (projectName != null && !projectName.trim().isEmpty()) {
            sql.append(" AND project_name LIKE ?");
            params.add("%" + projectName.trim() + "%");
        }
        
        if (createdDate != null && !createdDate.trim().isEmpty()) {
            sql.append(" AND DATE(created_at) = ?");
            params.add(createdDate.trim());
        }
        
        try (PreparedStatement st = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public static void main(String[] args) {
        UnitConversionDao n = new UnitConversionDao();
//        List<ImportHistory> jjj = n.getImportHistoryDetail("");
//        System.out.println(jjj);
    }

}
