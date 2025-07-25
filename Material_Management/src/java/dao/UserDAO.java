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
                + "(full_name, email, password, phone, role_id, status, image, gender) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET full_name=?, email=?, password=?, phone=?, role_id=?, status=?, image=?, gender=? WHERE user_id=?";
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
            ps.setInt(9, user.getUser_id());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserById(int userId) {
        String sql = "SELECT u.user_id, u.full_name, u.email, u.password, u.phone, u.role_id, r.role_name, u.status, u.image, u.gender "
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

    public int countAllUsers() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countActiveUsers() {
        String sql = "SELECT COUNT(*) FROM users WHERE LOWER(status) = 'active' OR LOWER(status) = 'hoạt động'";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countInactiveUsers() {
        String sql = "SELECT COUNT(*) FROM users WHERE NOT (LOWER(status) = 'active' OR LOWER(status) = 'hoạt động')";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countAllUsersToMonth(String endDate) {
        String sql = "SELECT COUNT(*) FROM users WHERE 1=1";
        if (endDate != null && !endDate.isEmpty()) {
            // Giả sử có trường created_at kiểu DATETIME
            java.time.YearMonth ym = java.time.YearMonth.parse(endDate);
            int lastDay = ym.lengthOfMonth();
            sql += " AND created_at <= ?";
            endDate = endDate + "-" + lastDay + " 23:59:59";
        }
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (endDate != null && !endDate.isEmpty()) {
                ps.setString(1, endDate);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countUsersByDateRange(String startDate, String endDate) {
        String sql = "SELECT COUNT(*) FROM users WHERE 1=1";
        List<Object> params = new ArrayList<>();
        if (startDate != null && !startDate.isEmpty()) {
            sql += " AND created_at >= ?";
            params.add(startDate + "-01 00:00:00");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql += " AND created_at <= ?";
            params.add(endDate + "-31 23:59:59");
        }
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
