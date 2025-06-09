package controller;

import dao.UserPermissionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        String fullName = request.getParameter("fullName");
        
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            request.setAttribute("error", "User ID is required: ");
            request.getRequestDispatcher("userPermission.jsp").forward(request, response);
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid user ID format: ");
            request.getRequestDispatcher("/userPermission.jsp").forward(request, response);
            return;
        }

        try {
            UserPermissionDAO userPermissionDAO = new UserPermissionDAO();
            
            // Verify user exists
            List<Map<String, Object>> users = userPermissionDAO.searchUser(String.valueOf(userId));
            if (users.isEmpty()) {
                request.setAttribute("error", "The user with ID " + userId + " does not exist.");
                request.getRequestDispatcher("/userPermission.jsp").forward(request, response);
                return;
            }

            // Collect all checked permissions
            List<String> selectedPermissions = new ArrayList<>();
            for (String perm : VALID_PERMISSIONS) {
                if (request.getParameter(perm) != null) {
                    selectedPermissions.add(perm);
                }
            }
            
            // Update permissions
            userPermissionDAO.updateUserPermissions(userId, selectedPermissions);
            request.setAttribute("success", "The permissions were updated successfully.");

            // Redirect back to userPermission with the userId to avoid ambiguity
            response.sendRedirect("userPermission?keyword=" + userId);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to update permissions: " + e.getMessage());
            request.getRequestDispatcher("userPermission.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An unexpected error has occurred: " + e.getMessage());
            request.getRequestDispatcher("userPermission.jsp").forward(request, response);
        }
    }
}