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

@WebServlet(name = "PermissionLogsServlet", urlPatterns = {"/permissionlogs"})
public class PermissionLogsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PermissionLogDAO permissionLogDAO = new PermissionLogDAO();
        UserDAO userDAO = new UserDAO();

        try {
            String roleIdStr = request.getParameter("roleId");

            if (roleIdStr != null && !roleIdStr.trim().isEmpty()) {
                int roleId = Integer.parseInt(roleIdStr);
                Role role = userDAO.getRoleById(roleId);
                if (role == null) {
                    request.setAttribute("error", "Role not found");
                } else {
                    request.setAttribute("role", role); // nếu bạn vẫn muốn show thông tin role
                }
                List<Map<String, Object>> logs = permissionLogDAO.getPermissionLogsByRole(roleId);
                request.setAttribute("logs", logs);
            } else {
                List<Map<String, Object>> logs = permissionLogDAO.getAllPermissionLogs();
                request.setAttribute("logs", logs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error while retrieving permission logs: " + e.getMessage());
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Role ID format");
        }

        request.getRequestDispatcher("permissionLogs.jsp").forward(request, response);
    }
}
