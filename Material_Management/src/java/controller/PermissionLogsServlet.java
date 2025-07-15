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
            String roleParam = request.getParameter("role");
            String pageParam = request.getParameter("page");
            int pageSize = 10;
            int currentPage = 1;
            if (pageParam != null) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }

            List<Map<String, Object>> logs;
            int totalLogs = 0;
            int totalPages = 1;

            if (roleParam == null || roleParam.isEmpty()) {
                logs = null;
            } else {
                if ("all".equals(roleParam)) {
                    List<Map<String, Object>> allLogs = permissionLogDAO.getAllPermissionLogs();
                    totalLogs = allLogs.size();
                    totalPages = (int) Math.ceil((double) totalLogs / pageSize);
                    int fromIndex = (currentPage - 1) * pageSize;
                    int toIndex = Math.min(fromIndex + pageSize, totalLogs);
                    if (fromIndex < totalLogs) {
                        logs = allLogs.subList(fromIndex, toIndex);
                    } else {
                        logs = java.util.Collections.emptyList();
                    }
                } else {
                    int roleId = Integer.parseInt(roleParam);
                    List<Map<String, Object>> roleLogs = permissionLogDAO.getPermissionLogsByRole(roleId);
                    totalLogs = roleLogs.size();
                    totalPages = (int) Math.ceil((double) totalLogs / pageSize);
                    int fromIndex = (currentPage - 1) * pageSize;
                    int toIndex = Math.min(fromIndex + pageSize, totalLogs);
                    if (fromIndex < totalLogs) {
                        logs = roleLogs.subList(fromIndex, toIndex);
                    } else {
                        logs = java.util.Collections.emptyList();
                    }
                }
            }

            request.setAttribute("logs", logs);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("param", new Object() {
                public String role = roleParam;
            });

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error while retrieving permission logs: " + e.getMessage());
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Role ID format");
        }

        request.getRequestDispatcher("permissionLogs.jsp").forward(request, response);
    }
}
