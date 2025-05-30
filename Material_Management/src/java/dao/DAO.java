/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dal.DBContext;
import model.*;
import dao.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO extends dal.DBContext {

    public DAO() {
        super();
    }

    public List<Role> getRoles() {
        List<Role> role = new ArrayList<>();
        String sql = "SELECT * FROM roles";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Role user = new Role();
                user.setRoleid(rs.getInt("role_id"));
                user.setRolename(rs.getString("role_name"));
                role.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return role;
    }

    public List<User> getUser() {
        List<User> user = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                User u = new User();
                Role rcc = new Role();
                u.setUser_id(rs.getInt("user_id"));
                u.setFullname(rs.getString("full_name"));
                u.setPassword(rs.getString("password"));
                u.setUsername(rs.getString("user_name"));
                u.setPhone(rs.getString("phone"));
                u.setEmail(rs.getString("email"));
                u.setPriority(rs.getInt("priority"));
                rcc.setRoleid(rs.getInt("role_id"));
                rcc.setRolename(rs.getString("role_name"));
                u.setRole(rcc);
                u.setStatus(rs.getString("status"));
                u.setImage(rs.getString("image"));
                user.add(u);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    public List<User> userAccount() {
        List<User> account = new ArrayList<>();
        String sql = "SELECT  u.user_id,u.email,u.password,r.role_id,u.status\n"
                + "FROM users u \n"
                + "join roles r on u.role_id = r.role_id;";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                User acc = new User();
                Role rcc = new Role();
                acc.setUser_id(rs.getInt("user_id"));
                acc.setEmail(rs.getString("email"));
                acc.setPassword(rs.getString("password"));
                rcc.setRoleid(rs.getInt("role_id"));

                acc.setRole(rcc);
                acc.setStatus(rs.getString("status"));
                account.add(acc);

            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        return account;
    }

    public List<User> SettingList() {
        List<User> list = new ArrayList<>();
        String sql = "select user_id,full_name,role_name,r.role_id,status,priority,description from roles r join users u on r.role_id = u.role_id;";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                User acc = new User();
                Role rcc = new Role();
                acc.setUser_id(rs.getInt("user_id"));
                acc.setFullname(rs.getString("full_name"));
                rcc.setRoleid(rs.getInt("role_id"));
                rcc.setRolename(rs.getString("role_name"));
                acc.setRole(rcc);
                acc.setStatus(rs.getString("status"));
                acc.setDescription(rs.getString("description"));
                acc.setPriority(rs.getInt("priority"));
                list.add(acc);

            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        return list;
    }

    public User userID(int user_ID, int role_id) {
        String sql = "select u.user_id,r.role_id,u.full_name,u.priority,u.status,r.role_name \n"
                + "from users u join roles r \n"
                + "on u.role_id = r.role_id\n"
                + "where u.user_id = ? and r.role_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, user_ID);
            st.setInt(2, role_id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                User acc = new User();
                Role rcc = new Role();
                acc.setUser_id(rs.getInt("user_id"));

                acc.setFullname(rs.getString("full_name"));
                acc.setStatus(rs.getString("status"));
                acc.setPriority(rs.getInt("priority"));

                rcc.setRoleid(rs.getInt("role_id"));

                acc.setRole(rcc);
                return acc;
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;

    }

    public User updateSt(String status, int user_id) {
        String sql = "update users\n"
                + "set status = ?\n"
                + "where user_id = ? ;";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, status);
            st.setInt(2, user_id);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;

    }

    public User userUpdate(String fullname, int priority, String status, String description, int roleid, int userid) {
        String sql = "UPDATE Users \n"
                + "SET full_name=?, \n"
                + "priority=?, \n"
                + "status= ?, \n"
                + "description= ?, \n"
                + "role_id=?\n"
                + "WHERE user_id=?;";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, fullname);
            st.setInt(2, priority);
            st.setString(3, status);
            st.setString(4, description);
            st.setInt(5, roleid);
            st.setInt(6, userid);
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;

    }

    public List<User> getStatus(String status) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users\n"
                + "where status = ?;";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, status);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                User u = new User();

                u.setStatus(rs.getString("status"));
                u.setFullname(rs.getString("full_name"));

                list.add(u);
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        return list;

    }

    public List<User> getFilter(String status, String role_name, int priority, String full_name) {
        List<User> list = new ArrayList<>();
        List<Object> param = new ArrayList<>();
        String sql = "SELECT u.user_id,u.full_name,r.role_name,u.priority,u.status \n"
                + "FROM users u \n"
                + "join roles r on u.role_id = r.role_id\n"
                + "where 1 = 1";

        if (status != null && !status.equalsIgnoreCase("all")) {
            sql += " and u.status = ?";
            param.add(status.trim());
        }
        if (role_name != null && !role_name.equalsIgnoreCase("all")) {
            sql += " and r.role_name = ?";
            param.add(role_name.trim());
        }
        if (priority != -1) {

            sql += " and u.priority = ?";
            param.add(priority);
        }
        if (full_name != null && !full_name.equalsIgnoreCase("all")) {
            sql += " and u.full_name like ?";
            param.add("%" + full_name.trim() + "%");
        }

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            for (int i = 0; i < param.size(); i++) {
                st.setObject(i + 1, param.get(i));
            }
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUser_id(rs.getInt("user_id"));
                u.setStatus(rs.getString("status"));
                u.setUsername(rs.getString("role_name"));
                u.setPriority(rs.getInt("priority"));
                u.setFullname(rs.getString("full_name"));

                list.add(u);
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        return list;

    }

    public List<User> getSort(String sort) {
        String sqlsecon = "";
        List<User> list = new ArrayList<>();

        if (sort != null && sort.equalsIgnoreCase("idasc")) {
            sqlsecon += " ORDER BY user_id asc";
        } else if (sort.equalsIgnoreCase("iddesc")) {
            sqlsecon += " ORDER BY user_id desc;";
        } else if (sort.equalsIgnoreCase("nameasc")) {
            sqlsecon += " ORDER BY full_name asc;";
        } else if (sort.equalsIgnoreCase("namedesc")) {
            sqlsecon += " ORDER BY full_name desc;";
        } else if (sort.equalsIgnoreCase("priorityasc")) {
            sqlsecon += " ORDER BY priority asc;";
        } else if (sort.equalsIgnoreCase("prioritydesc")) {
            sqlsecon += " ORDER BY priority desc;";
        }
        try {
            String sql = "select u.user_id,r.role_name,u.full_name,u.status,u.priority \n"
                    + "from users u join roles r \n"
                    + "on u.role_id = r.role_id " + sqlsecon.trim();
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUser_id(rs.getInt("user_id"));
                u.setStatus(rs.getString("status"));
                u.setUsername(rs.getString("role_name"));
                u.setPriority(rs.getInt("priority"));
                u.setFullname(rs.getString("full_name"));

                list.add(u);
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;

    }

    public int getcountPage() {
        String sql = "SELECT COUNT(*) FROM users";
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

    public void close() {
        super.closeConnection();
    }

    public static void main(String[] args) {
        DAO con = new DAO();
        String fullname = "Nguyen Van A";   // tên đầy đủ, chuỗi
        int priority = 1;                   // số nguyên
        String status = "active";           // trạng thái, chuỗi
        String description = "New description"; // mô tả, chuỗi
        int roleid = 4;                    // id vai trò, số nguyên
        int userid = 9;                    // id người dùng, số nguyên

       User l = con.userUpdate(fullname, priority, status, description, roleid, userid);
       System.out.println(l);

    }
}
