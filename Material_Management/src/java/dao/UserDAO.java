package dao;

import model.*;
import dal.DBContext;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public UserDAO() {
    }

    public boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(regex);
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean insertUser(User user) {
        String sql = "INSERT INTO users "
                + "(full_name, email, password, phone, role_id, status, image, gender, dayofbirth) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullname());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            if (user.getRole() == null || user.getRole().getRoleid() == 0) {
                ps.setNull(5, Types.INTEGER);
            } else {
                ps.setInt(5, user.getRole().getRoleid());
            }
            ps.setString(6, user.getStatus());
            ps.setString(7, user.getImage());
            ps.setString(8, user.getGender());
            ps.setString(9, user.getDayofbirth());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET full_name=?, email=?, password=?, phone=?, role_id=?, status=?, image=?, gender=?, dayofbirth=? WHERE user_id=?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullname());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            if (user.getRole() != null && user.getRole().getRoleid() != 0) {
                ps.setInt(5, user.getRole().getRoleid());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setString(6, user.getStatus());
            ps.setString(7, user.getImage());
            ps.setString(8, user.getGender());
            ps.setString(9, user.getDayofbirth());
            ps.setInt(10, user.getUser_id());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserById(int userId) {
        String sql = "SELECT u.user_id, u.full_name, u.email, u.password, u.phone, u.role_id, r.role_name, u.status, u.image, u.gender, u.dayofbirth "
                + "FROM users u "
                + "INNER JOIN roles r ON u.role_id = r.role_id "
                + "WHERE u.user_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    Role role = new Role();
                    user.setUser_id(rs.getInt("user_id"));
                    user.setFullname(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setPhone(rs.getString("phone"));
                    role.setRoleid(rs.getInt("role_id"));
                    role.setRolename(rs.getString("role_name"));
                    user.setRole(role);
                    user.setStatus(rs.getString("status"));
                    user.setImage(rs.getString("image"));
                    user.setGender(rs.getString("gender"));
                    user.setDayofbirth(rs.getString("dayofbirth"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user with ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Role getRoleById(int roleId) {
        String sql = "SELECT role_id, role_name FROM roles WHERE role_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role();
                    role.setRoleid(rs.getInt("role_id"));
                    role.setRolename(rs.getString("role_name"));
                    return role;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching role with ID " + roleId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT role_id, role_name FROM roles WHERE role_id >= 2 AND role_id <= 4";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Role role = new Role();
                    role.setRoleid(rs.getInt("role_id"));
                    role.setRolename(rs.getString("role_name"));
                    roles.add(role);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching roles: " + e.getMessage());
            e.printStackTrace();
        }
        return roles;
    }

    public boolean existsEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE LOWER(email) = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public Integer getDirectorId() {
        String sql = "SELECT user_id FROM users WHERE role_id = 2 LIMIT 1";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
