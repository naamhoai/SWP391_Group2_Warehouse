package dao;

import model.User;
import dal.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Role;

public class ResetPasswordDAO extends DBContext {

    public User getUserByEmail(String email) {
        String sql = "SELECT u.*, r.role_name FROM users u JOIN roles r ON u.role_id = r.role_id WHERE LOWER(u.email) = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email.toLowerCase());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Role r = new Role();
                r.setRoleid(rs.getInt("role_id"));
                r.setRolename(rs.getString("role_name"));
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        r,
                        rs.getString("status"),
                        0,
                        rs.getString("image"),
                        rs.getString("gender"),
                        rs.getString("dayofbirth"),
                        null
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
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        r,
                        rs.getString("status"),
                        0, // priority mặc định
                        rs.getString("image"),
                        rs.getString("gender"),
                        rs.getString("dayofbirth"),
                        null // description mặc định
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePassword(String email, String password) {
        String sql = "UPDATE `users` SET `password` = ? WHERE LOWER(`email`) = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, password);
            ps.setString(2, email.toLowerCase());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserByEmailAndFullname(String email, String fullname) {
        String sql = "SELECT u.*, r.role_name FROM users u JOIN roles r ON u.role_id = r.role_id WHERE u.email = ? AND u.full_name = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, fullname);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Role r = new Role();
                r.setRoleid(rs.getInt("role_id"));
                r.setRolename(rs.getString("role_name"));
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        r,
                        rs.getString("status"),
                        0,
                        rs.getString("image"),
                        rs.getString("gender"),
                        rs.getString("dayofbirth"),
                        null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
