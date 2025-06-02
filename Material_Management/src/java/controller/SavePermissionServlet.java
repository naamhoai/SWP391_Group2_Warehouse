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
import java.util.Arrays;
import java.util.List;

@WebServlet("/savePermissions")
public class SavePermissionServlet extends HttpServlet {
    private static final List<String> VALID_PERMISSIONS = Arrays.asList(
        "category_view", "category_add", "category_edit", "category_delete",
        "inventory_view", "inventory_add", "inventory_edit", "inventory_delete",
        "order_view", "order_add", "order_edit", "order_delete",
        "delivery_view", "delivery_add", "delivery_edit", "delivery_delete",
        "user_view", "user_add", "user_edit", "user_delete"
    );

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Invalid user ID.");
            request.getRequestDispatcher("/user-permission.jsp").forward(request, response);
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid user ID format.");
            request.getRequestDispatcher("/user-permission.jsp").forward(request, response);
            return;
        }

        Connection conn = null;
        try {
            DBContext db = new DBContext();
            conn = db.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Delete existing user permissions
            PreparedStatement deleteStmt = conn.prepareStatement(
                "DELETE FROM user_permissions WHERE user_id = ?");
            deleteStmt.setInt(1, userId);
            deleteStmt.executeUpdate();

            // Insert new permissions
            PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO user_permissions (user_id, permission_id) " +
                "SELECT ?, permission_id FROM permissions WHERE permission_name = ?");
            insertStmt.setInt(1, userId);
            for (String perm : VALID_PERMISSIONS) {
                if (request.getParameter(perm) != null) { // Checkbox checked
                    insertStmt.setString(2, perm);
                    insertStmt.executeUpdate();
                }
            }

            conn.commit(); // Commit transaction
            request.setAttribute("success", "Permissions updated successfully.");

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback(); // Rollback on error
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            request.setAttribute("error", "Failed to update permissions: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Redirect back to user-permission with the same user
        String fullName = request.getParameter("fullName"); // Pass fullName from form
        response.sendRedirect("user-permission?keyword=" + java.net.URLEncoder.encode(fullName, "UTF-8"));
    }
}