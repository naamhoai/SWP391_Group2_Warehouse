package controller;


import dal.DBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@WebServlet(name = "UserPermissionServlet", urlPatterns = {"/searchUsers", "/savePermissions"})
public class UserPermissionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getServletPath();
        
        if ("/searchUsers".equals(pathInfo)) {
            searchUsers(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getServletPath();
        
        if ("/savePermissions".equals(pathInfo)) {
            savePermissions(request, response);
        }
    }

    private void searchUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application");
        response.setCharacterEncoding("UTF-8");
        
        String searchTerm = request.getParameter("term");
        StringBuilder jsonResponse = new StringBuilder();
        
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT u.*, p.* FROM Users u " +
                        "LEFT JOIN Permissions p ON u.user_id = p.user_id " +
                        "WHERE u.username LIKE ? OR u.full_name LIKE ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "%" + searchTerm + "%");
                stmt.setString(2, "%" + searchTerm + "%");
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        jsonResponse.append("{");
                        jsonResponse.append("\"userId\":").append(rs.getInt("user_id")).append(",");
                        jsonResponse.append("\"username\":\"").append(rs.getString("username")).append("\",");
                        jsonResponse.append("\"fullName\":\"").append(rs.getString("full_name")).append("\",");
                        
                        // Add permissions object
                        jsonResponse.append("\"permissions\":{");
                        
                        // Category permissions
                        jsonResponse.append("\"category_view\":").append(rs.getBoolean("category_view")).append(",");
                        jsonResponse.append("\"category_add\":").append(rs.getBoolean("category_add")).append(",");
                        jsonResponse.append("\"category_edit\":").append(rs.getBoolean("category_edit")).append(",");
                        jsonResponse.append("\"category_delete\":").append(rs.getBoolean("category_delete")).append(",");
                        
                        // Inventory permissions
                        jsonResponse.append("\"inventory_view\":").append(rs.getBoolean("inventory_view")).append(",");
                        jsonResponse.append("\"inventory_add\":").append(rs.getBoolean("inventory_add")).append(",");
                        jsonResponse.append("\"inventory_edit\":").append(rs.getBoolean("inventory_edit")).append(",");
                        jsonResponse.append("\"inventory_delete\":").append(rs.getBoolean("inventory_delete")).append(",");
                        
                        // Order permissions
                        jsonResponse.append("\"order_view\":").append(rs.getBoolean("order_view")).append(",");
                        jsonResponse.append("\"order_add\":").append(rs.getBoolean("order_add")).append(",");
                        jsonResponse.append("\"order_edit\":").append(rs.getBoolean("order_edit")).append(",");
                        jsonResponse.append("\"order_delete\":").append(rs.getBoolean("order_delete")).append(",");
                        
                        // Delivery permissions
                        jsonResponse.append("\"delivery_view\":").append(rs.getBoolean("delivery_view")).append(",");
                        jsonResponse.append("\"delivery_add\":").append(rs.getBoolean("delivery_add")).append(",");
                        jsonResponse.append("\"delivery_edit\":").append(rs.getBoolean("delivery_edit")).append(",");
                        jsonResponse.append("\"delivery_delete\":").append(rs.getBoolean("delivery_delete")).append(",");
                        
                        // User permissions
                        jsonResponse.append("\"user_view\":").append(rs.getBoolean("user_view")).append(",");
                        jsonResponse.append("\"user_add\":").append(rs.getBoolean("user_add")).append(",");
                        jsonResponse.append("\"user_edit\":").append(rs.getBoolean("user_edit")).append(",");
                        jsonResponse.append("\"user_delete\":").append(rs.getBoolean("user_delete"));
                        
                        jsonResponse.append("}}");
                    } else {
                        jsonResponse.append("{}");
                    }
                }
            }
            
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse.toString());
            }
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"error\":\"Database error: " + escapeJsonString(e.getMessage()) + "\"}");
            }
        }
    }

    private String escapeJsonString(String input) {
        if (input == null) {
            return "";
        }
        StringBuilder escaped = new StringBuilder();
        for (char c : input.toCharArray()) {
            switch (c) {
                case '"':
                    escaped.append("\\\"");
                    break;
                case '\\':
                    escaped.append("\\\\");
                    break;
                case '\b':
                    escaped.append("\\b");
                    break;
                case '\f':
                    escaped.append("\\f");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                default:
                    escaped.append(c);
            }
        }
        return escaped.toString();
    }

    private void savePermissions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "UPDATE Permissions SET " +
                        "category_view=?, category_add=?, category_edit=?, category_delete=?, " +
                        "inventory_view=?, inventory_add=?, inventory_edit=?, inventory_delete=?, " +
                        "order_view=?, order_add=?, order_edit=?, order_delete=?, " +
                        "delivery_view=?, delivery_add=?, delivery_edit=?, delivery_delete=?, " +
                        "user_view=?, user_add=?, user_edit=?, user_delete=? " +
                        "WHERE user_id=?";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                int paramIndex = 1;
                String[] permissions = {
                    "category_view", "category_add", "category_edit", "category_delete",
                    "inventory_view", "inventory_add", "inventory_edit", "inventory_delete",
                    "order_view", "order_add", "order_edit", "order_delete",
                    "delivery_view", "delivery_add", "delivery_edit", "delivery_delete",
                    "user_view", "user_add", "user_edit", "user_delete"
                };
                
                for (String permission : permissions) {
                    stmt.setBoolean(paramIndex++, request.getParameter(permission) != null);
                }
                stmt.setInt(paramIndex, userId);
                
                stmt.executeUpdate();
                
                response.sendRedirect("user-permission.jsp?success=true");
            }
            
        } catch (SQLException e) {
            response.sendRedirect("user-permission.jsp?error=" + e.getMessage());
        }
    }
} 