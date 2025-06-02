package controller;

import dal.DBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/user-permission")
public class UserPermissionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");

        // Validate input
        if (keyword == null || keyword.trim().isEmpty()) {
            request.setAttribute("error", "Please enter a username or ID to search.");
            request.getRequestDispatcher("/user-permission.jsp").forward(request, response);
            return;
        }

        Connection conn = null;
        try {
            DBContext db = new DBContext();
            conn = db.getConnection();

            // Search user by full_name or user_id
            PreparedStatement userStmt = conn.prepareStatement(
                "SELECT u.user_id, u.full_name, u.role_id, r.role_name " +
                "FROM users u JOIN roles r ON u.role_id = r.role_id " +
                "WHERE u.full_name LIKE ? OR u.user_id = ?");
            userStmt.setString(1, "%" + keyword + "%");
            try {
                userStmt.setInt(2, Integer.parseInt(keyword));
            } catch (NumberFormatException e) {
                userStmt.setInt(2, -1); // Invalid ID, won't match any user_id
            }
            ResultSet userRs = userStmt.executeQuery();

            if (userRs.next()) {
                // User found, set attributes
                request.setAttribute("userId", userRs.getInt("user_id"));
                request.setAttribute("fullName", userRs.getString("full_name"));
                request.setAttribute("roleId", userRs.getInt("role_id"));
                request.setAttribute("roleName", userRs.getString("role_name"));

                // Get role permissions
                PreparedStatement rolePermStmt = conn.prepareStatement(
                    "SELECT p.permission_name FROM role_permissions rp " +
                    "JOIN permissions p ON rp.permission_id = p.permission_id " +
                    "WHERE rp.role_id = ?");
                rolePermStmt.setInt(1, userRs.getInt("role_id"));
                ResultSet rolePermRs = rolePermStmt.executeQuery();
                Map<String, Boolean> rolePermissions = new HashMap<>();
                while (rolePermRs.next()) {
                    rolePermissions.put(rolePermRs.getString("permission_name"), true);
                }
                request.setAttribute("rolePermissions", rolePermissions);

                // Get user permissions
                PreparedStatement userPermStmt = conn.prepareStatement(
                    "SELECT p.permission_name FROM user_permissions up " +
                    "JOIN permissions p ON up.permission_id = p.permission_id " +
                    "WHERE up.user_id = ?");
                userPermStmt.setInt(1, userRs.getInt("user_id"));
                ResultSet userPermRs = userPermStmt.executeQuery();
                List<String> userPermissions = new ArrayList<>();
                while (userPermRs.next()) {
                    userPermissions.add(userPermRs.getString("permission_name"));
                }
                request.setAttribute("userPermissions", userPermissions);
            } else {
                request.setAttribute("error", "No user found with name or ID: " + keyword);
            }

            request.getRequestDispatcher("/user-permission.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "System error: " + e.getMessage());
            request.getRequestDispatcher("/user-permission.jsp").forward(request, response);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}