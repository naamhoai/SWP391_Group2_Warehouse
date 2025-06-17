package controller;

import dao.UserPermissionDAO;
import dao.PermissionLogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
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
        HttpSession session = request.getSession();
        Integer adminId = (Integer) session.getAttribute("userId");
        
        if (adminId == null) {
            request.setAttribute("error", "Unauthorized access");
            request.getRequestDispatcher("userPermission.jsp").forward(request, response);
            return;
        }

        try {
            String roleIdStr = request.getParameter("roleId");
            if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Role ID is missing");
                request.getRequestDispatcher("userPermission.jsp").forward(request, response);
                return;
            }

            int roleId;
            try {
                roleId = Integer.parseInt(roleIdStr);
                if (roleId <= 1 || roleId > 4) {
                    request.setAttribute("error", "Invalid Role ID for permission management");
                    request.getRequestDispatcher("userPermission.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid Role ID format");
                request.getRequestDispatcher("userPermission.jsp").forward(request, response);
                return;
            }

            // Get current permissions
            UserPermissionDAO userPermissionDAO = new UserPermissionDAO();
            Map<String, Boolean> currentPermissions = userPermissionDAO.getRolePermissions(roleId);
            
            // Collect new permissions
            List<String> selectedPermissions = new ArrayList<>();
            for (String permission : VALID_PERMISSIONS) {
                String paramValue = request.getParameter(permission);
                if (paramValue != null && !paramValue.trim().isEmpty()) {
                    selectedPermissions.add(permission);
                }
            }

            // Log permission changes
            PermissionLogDAO logDAO = new PermissionLogDAO();
            for (String permission : VALID_PERMISSIONS) {
                boolean oldValue = currentPermissions.getOrDefault(permission, false);
                boolean newValue = selectedPermissions.contains(permission);
                
                if (oldValue != newValue) {
                    logDAO.logPermissionChange(
                        roleId,
                        adminId,
                        newValue ? "GRANT" : "REVOKE",
                        permission,
                        oldValue,
                        newValue
                    );
                }
            }

            // Update permissions in database
            userPermissionDAO.updateRolePermissions(roleId, selectedPermissions);

            // Redirect with success message
            response.sendRedirect(request.getContextPath() + "/permissionList?success=true");
            
        } catch (Exception e) {
            request.setAttribute("error", "Error updating permissions: " + e.getMessage());
            request.getRequestDispatcher("userPermission.jsp").forward(request, response);
        }
    }
}
