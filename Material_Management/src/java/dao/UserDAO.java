package dao;

import model.*;
import dal.DBContext;

import java.sql.*;
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

            if (user.getRole().getRoleid() == 0) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, user.getRole().getRoleid());
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

            if (user.getRole() != null && user.getRole().getRoleid() != 0) {
                ps.setInt(6, user.getRole().getRoleid());
            } else {
                ps.setNull(6, Types.INTEGER); // Nếu không có vai trò, set null
            }

            ps.setString(7, user.getStatus());
            ps.setInt(8, user.getPriority());
            ps.setString(9, user.getImage()); // Cập nhật đường dẫn ảnh
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
        // Thêm JOIN để lấy thông tin role_name từ bảng 'roles'
        String sql = "SELECT u.user_id, u.user_name, u.full_name, u.email, u.password, u.phone, u.role_id, r.role_name, u.status, u.priority, u.image, u.gender, u.dayofbirth, u.description "
                + "FROM users u "
                + "INNER JOIN roles r ON u.role_id = r.role_id "
                + // JOIN với bảng roles
                "WHERE u.user_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                // Kiểm tra nếu có kết quả trả về
                if (rs.next()) {
                    User user = new User();
                    Role role = new Role();

                    // Thiết lập thông tin cho đối tượng User từ ResultSet
                    user.setUser_id(rs.getInt("user_id"));
                    user.setUsername(rs.getString("user_name"));
                    user.setFullname(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setPhone(rs.getString("phone"));

                    // Cập nhật thông tin vai trò từ bảng roles
                    role.setRoleid(rs.getInt("role_id"));
                    role.setRolename(rs.getString("role_name"));  // Lấy tên vai trò từ bảng roles
                    user.setRole(role);

                    // Cập nhật các thông tin khác của người dùng
                    user.setStatus(rs.getString("status"));
                    user.setPriority(rs.getInt("priority"));
                    user.setImage(rs.getString("image"));
                    user.setGender(rs.getString("gender"));
                    user.setDayofbirth(rs.getString("dayofbirth"));
                    user.setDescription(rs.getString("description"));

                    return user;
                } else {
                    // Trường hợp không tìm thấy người dùng
                    System.out.println("No user found with user_id = " + userId);
                }
            }
        } catch (SQLException e) {
            // Log chi tiết lỗi nếu có
            System.err.println("Error fetching user with ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;  // Trả về null nếu không tìm thấy hoặc gặp lỗi
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
                    Role role = new Role();
                    role.setRoleid(rs.getInt("role_id"));
                    user.setUser_id(rs.getInt("user_id"));
                    user.setUsername(rs.getString("user_name"));
                    user.setFullname(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setPhone(rs.getString("phone"));
                    user.setRole(role);
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
        // Update SQL query to select all user fields and role information (role_id and role_name)
        String sql = "SELECT u.user_id, u.user_name, u.full_name, u.email, u.password, u.phone, u.status, u.priority, u.image, u.gender, u.dayofbirth, u.description, r.role_id, r.role_name "
                + "FROM users u "
                + "JOIN roles r ON u.role_id = r.role_id";  // JOIN roles table to get role information

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            // Debugging: check the SQL query result
            System.out.println("Executing SQL Query: " + sql);

            while (rs.next()) {
                User user = new User();

                // Set user details from the result set
                user.setUser_id(rs.getInt("user_id"));
                user.setUsername(rs.getString("user_name"));
                user.setFullname(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setStatus(rs.getString("status"));
                user.setPriority(rs.getInt("priority"));
                user.setImage(rs.getString("image"));
                user.setGender(rs.getString("gender"));
                user.setDayofbirth(rs.getString("dayofbirth"));
                user.setDescription(rs.getString("description"));

                // Debugging: check user data retrieved
                System.out.println("User retrieved: " + user);

                // Create a Role object and set the role data from the result set
                Role role = new Role();
                role.setRoleid(rs.getInt("role_id"));
                role.setRolename(rs.getString("role_name"));
                user.setRole(role);  // Set the role for the user

                // Add the user to the list
                list.add(user);
            }

            // Debugging: check the final list size
            System.out.println("Total users retrieved: " + list.size());

        } catch (SQLException e) {
            e.printStackTrace();  // Log any SQL exceptions that occur
        }

        return list;  // Return the list of users with full information
    }

    public List<User> searchUsersByKeyword(String keyword) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.user_id, u.user_name, u.full_name, u.email, u.status,u.role_id, r.role_name "
                + "FROM users u LEFT JOIN roles r ON u.role_id = r.role_id "
                + "WHERE u.user_name LIKE ? OR u.full_name LIKE ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String likeKeyword = "%" + keyword + "%";
            ps.setString(1, likeKeyword);
            ps.setString(2, likeKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();

                    user.setUser_id(rs.getInt("user_id"));
                    user.setUsername(rs.getString("user_name"));
                    user.setFullname(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setStatus(rs.getString("status"));

                    Role role = new Role();
                    role.setRoleid(rs.getInt("role_id"));
                    role.setRolename(rs.getString("role_name"));
                    user.setRole(role);
                    list.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    

}
