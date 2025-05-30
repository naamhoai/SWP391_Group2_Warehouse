package dao;

import model.User;
import dal.DBContext;

import java.sql.*;
import java.security.MessageDigest;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public UserDAO() {
    }

    // Thêm user mới
    public boolean insertUser(User user) {
        String sql = "INSERT INTO users "
                + "(user_name, full_name, email, password, phone, role_id, status, priority, image, gender, dayofbirth, description) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullname());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getPhone());

            if (user.getRole_id() == 0) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, user.getRole_id());
            }

            ps.setString(7, user.getStatus());
            ps.setInt(8, user.getPriority());
            ps.setString(9, user.getImage());
            ps.setString(10, user.getGender());
            ps.setString(11, user.getDayofbirth());
            ps.setString(12, user.getDescription());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật user
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET user_name=?, full_name=?, email=?, password=?, phone=?, role_id=?, status=?, priority=?, image=?, gender=?, dayofbirth=?, description=? WHERE user_id=?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullname());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getPhone());

            if (user.getRole_id() == 0) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, user.getRole_id());
            }

            ps.setString(7, user.getStatus());
            ps.setInt(8, user.getPriority());
            ps.setString(9, user.getImage());
            ps.setString(10, user.getGender());
            ps.setString(11, user.getDayofbirth());
            ps.setString(12, user.getDescription());
            ps.setInt(13, user.getUser_id());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy user theo ID
    public User getUserById(int userId) {
        String sql = "SELECT user_id, user_name, full_name, email, password, phone, role_id, status, priority, image, gender, dayofbirth, description FROM users WHERE user_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUser_id(rs.getInt("user_id"));
                    user.setUsername(rs.getString("user_name"));
                    user.setFullname(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setPhone(rs.getString("phone"));
                    user.setRole_id(rs.getInt("role_id"));
                    user.setStatus(rs.getString("status"));
                    user.setPriority(rs.getInt("priority"));
                    user.setImage(rs.getString("image"));
                    user.setGender(rs.getString("gender"));
                    user.setDayofbirth(rs.getString("dayofbirth"));
                    user.setDescription(rs.getString("description"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tên role theo id
    public String getRoleNameById(int roleId) {
        String sql = "SELECT role_name FROM roles WHERE role_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Mã hóa mật khẩu SHA-256
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // Tạo email tự động
    public String generateEmail(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return null;
        }

        fullName = fullName.trim().replaceAll("\\s+", " ");
        String[] parts = fullName.split(" ");
        if (parts.length < 2) {
            return null;
        }

        String lastName = parts[parts.length - 1];
        lastName = removeAccent(lastName);
        lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();

        StringBuilder initials = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            String partNoAccent = removeAccent(parts[i]);
            if (!partNoAccent.isEmpty()) {
                initials.append(partNoAccent.substring(0, 1).toUpperCase());
            }
        }

        int randomNum = 100000 + (int) (Math.random() * 900000);
        return lastName + initials.toString() + randomNum + "@gmail.com";
    }

    // Loại bỏ dấu tiếng Việt
    public String removeAccent(String s) {
        if (s == null) {
            return null;
        }
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("\\p{M}", "");
        s = s.replaceAll("[^\\p{ASCII}]", "");
        return s;
    }

    // Kiểm tra username đã tồn tại
    public boolean existsUsername(String username) {
        String sql = "SELECT 1 FROM users WHERE user_name = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra email đã tồn tại
    public boolean existsEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE user_name = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUser_id(rs.getInt("user_id"));
                    user.setUsername(rs.getString("user_name"));
                    user.setFullname(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setPhone(rs.getString("phone"));
                    user.setRole_id(rs.getInt("role_id"));
                    user.setStatus(rs.getString("status"));
                    user.setPriority(rs.getInt("priority"));
                    user.setImage(rs.getString("image"));
                    user.setGender(rs.getString("gender"));
                    user.setDayofbirth(rs.getString("dayofbirth"));
                    user.setDescription(rs.getString("description"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getUserListSummary() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_name, full_name, email, status FROM users";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("user_name"));
                user.setFullname(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setStatus(rs.getString("status"));
                // Các trường khác không set vì ko cần thiết cho danh sách
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<User> searchUsersByKeyword(String keyword) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_name, full_name, email, status FROM users WHERE user_name LIKE ? OR full_name LIKE ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String likeKeyword = "%" + keyword + "%";
            ps.setString(1, likeKeyword);
            ps.setString(2, likeKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUsername(rs.getString("user_name"));
                    user.setFullname(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setStatus(rs.getString("status"));
                    list.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Test method main
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        String fullName = "Nguyễn Văn A";
        String email = userDAO.generateEmail(fullName);

        User newUser = new User();
        newUser.setUsername(email);
        newUser.setFullname(fullName);
        newUser.setEmail(email);
        newUser.setPassword(userDAO.hashPassword("123456"));
        newUser.setPhone("0909123456");
        newUser.setRole_id(2);
        newUser.setStatus("active");
        newUser.setPriority(1);
        newUser.setImage(null);
        newUser.setGender("Men");
        newUser.setDayofbirth("1990-01-01");
        newUser.setDescription("Test user");

        boolean success = userDAO.insertUser(newUser);
        System.out.println(success ? "Thêm user thành công!" : "Thêm user thất bại!");
    }
}
