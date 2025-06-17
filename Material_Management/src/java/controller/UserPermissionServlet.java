package controller;

import dao.UserPermissionDAO;
import dao.UserDAO;
import model.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UserPermissionServlet", urlPatterns = {"/userPermission"})
public class UserPermissionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        UserPermissionDAO userPermissionDAO = new UserPermissionDAO();
        UserDAO userDAO = new UserDAO();

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
                    request.setAttribute("error", "Invalid Role ID");
                    request.getRequestDispatcher("userPermission.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid Role ID format");
                request.getRequestDispatcher("userPermission.jsp").forward(request, response);
                return;
            }

            // Lấy thông tin vai trò (giả định UserDAO có phương thức getRoleById)
            Role role = userDAO.getRoleById(roleId); // Cần thêm phương thức này vào UserDAO
            if (role == null) {
                request.setAttribute("error", "Role not found");
                request.getRequestDispatcher("userPermission.jsp").forward(request, response);
                return;
            }

            // Lấy quyền của vai trò
            Map<String, Boolean> rolePermissions = userPermissionDAO.getRolePermissions(roleId);
            List<Map<String, Object>> allPermissions = userPermissionDAO.getAllPermissions();

            request.setAttribute("role", role);
            request.setAttribute("rolePermissions", rolePermissions);
            request.setAttribute("permissions", allPermissions);

            request.getRequestDispatcher("userPermission.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("userPermission.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Unexpected error: " + e.getMessage());
            request.getRequestDispatcher("userPermission.jsp").forward(request, response);
        }
    }
}
