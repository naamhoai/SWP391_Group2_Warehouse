package dao;

import model.User;
import dal.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Role;

public class ResetPasswordDAO extends DBContext {

    public User getUserByEmail(String email) {
        String sql = "SELECT u.*, r.role_name FROM users u JOIN roles r ON u.role_id = r.role_id WHERE u.email = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Role r = new Role();
                r.setRoleid(rs.getInt("role_id"));
                r.setRolename(rs.getString("role_name"));
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        r,
                        rs.getString("status"),
                        rs.getInt("priority"),
                        rs.getString("image"),
                        rs.getString("gender"),
                        rs.getString("dayofbirth"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserById(int userId) {
        String sql = "SELECT u.*, r.role_name FROM users u JOIN roles r ON u.role_id = r.role_id WHERE u.user_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Role r = new Role();
                r.setRoleid(rs.getInt("role_id"));
                r.setRolename(rs.getString("role_name"));
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        r,
                        rs.getString("status"),
                        rs.getInt("priority"),
                        rs.getString("image"),
                        rs.getString("gender"),
                        rs.getString("dayofbirth"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePassword(String email, String password) {
        String sql = "UPDATE `users` SET `password` = ? WHERE `email` = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, password);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateUsernameAndPassword(String email, String username, String password) {
        // First check if the username already exists for a different email
        String checkSql = "SELECT COUNT(*) FROM users WHERE user_name = ? AND email != ?";
        try {
            PreparedStatement checkPs = connection.prepareStatement(checkSql);
            checkPs.setString(1, username);
            checkPs.setString(2, email);
            ResultSet rs = checkPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Username already exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // If username is unique, proceed with update
        String sql = "UPDATE users SET user_name = ?, password = ? WHERE email = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
