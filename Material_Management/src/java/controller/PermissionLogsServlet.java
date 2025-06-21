package controller;

import dao.PermissionLogDAO;
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

@WebServlet(name = "PermissionLogsServlet", urlPatterns = {"/permissionLogs"})
public class PermissionLogsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PermissionLogDAO permissionLogDAO = new PermissionLogDAO();
        UserDAO userDAO = new UserDAO();

        try {
            String roleIdStr = request.getParameter("roleId");
            if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Role ID is missing");
                request.getRequestDispatcher("permissionLogs.jsp").forward(request, response);
                return;
            }

            int roleId;
            try {
                roleId = Integer.parseInt(roleIdStr);
                if (roleId <= 1 || roleId > 4) {
                    request.setAttribute("error", "Invalid Role ID");
                    request.getRequestDispatcher("permissionLogs.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid Role ID format");
                request.getRequestDispatcher("permissionLogs.jsp").forward(request, response);
                return;
            }

            Role role = userDAO.getRoleById(roleId);
            if (role == null) {
                request.setAttribute("error", "Role not found");
                request.getRequestDispatcher("permissionLogs.jsp").forward(request, response);
                return;
            }

            List<Map<String, Object>> logs = permissionLogDAO.getPermissionLogs(roleId);

            request.setAttribute("role", role);
            request.setAttribute("logs", logs);

            request.getRequestDispatcher("permissionLogs.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("permissionLogs.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Unexpected error: " + e.getMessage());
            request.getRequestDispatcher("permissionLogs.jsp").forward(request, response);
        }
    }
}