package dao;

import model.Role;
import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    // 2. Lấy tất cả role trừ Admin (role_id = 1)
    public List<Role> getAllRolesExceptAdmin() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT role_id, role_name FROM roles WHERE role_id <> 1";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Role r = new Role();
                r.setRoleid(rs.getInt("role_id"));
                r.setRolename(rs.getString("role_name"));
                roles.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    // 3. Lấy role theo id
    public Role getRoleById(int roleId) {
        Role role = null;
        String sql = "SELECT role_id, role_name FROM roles WHERE role_id = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    role = new Role();
                    role.setRoleid(rs.getInt("role_id"));
                    role.setRolename(rs.getString("role_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    // Main test
    public static void main(String[] args) {
        RoleDAO roleDAO = new RoleDAO();
        List<Role> roles = roleDAO.getAllRolesExceptAdmin();
        System.out.println("Danh sách vai trò (trừ Admin):");
        for (Role r : roles) {
            System.out.println("Role ID: " + r.getRoleid() + " - Name: " + r.getRolename());
        }
    }
}
