package controller;

import dao.UserPermissionDAO;
import dao.UserDAO;
import dao.PermissionLogDAO;
import model.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UserPermissionServlet", urlPatterns = {"/userPermission"})
public class UserPermissionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processGetRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processPostRequest(request, response);
    }

    private void processGetRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

            Role role = userDAO.getRoleById(roleId);
            if (role == null) {
                request.setAttribute("error", "Role not found");
                request.getRequestDispatcher("userPermission.jsp").forward(request, response);
                return;
            }

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

    private void processPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        UserPermissionDAO userPermissionDAO = new UserPermissionDAO();
        PermissionLogDAO permissionLogDAO = new PermissionLogDAO();
        UserDAO userDAO = new UserDAO();
        HttpSession session = request.getSession();

        try {
            // Lấy adminId từ session
            Integer adminId = (Integer) session.getAttribute("userId"); // Giả định userId được lưu trong session
            if (adminId == null) {
                request.setAttribute("error", "Bạn cần đăng nhập để thực hiện thao tác này");
                request.getRequestDispatcher("userPermission.jsp").forward(request, response);
                return;
            }

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

            Role role = userDAO.getRoleById(roleId);
            if (role == null) {
                request.setAttribute("error", "Role not found");
                request.getRequestDispatcher("userPermission.jsp").forward(request, response);
                return;
            }

            // Lấy danh sách quyền mới từ form
            String[] permissionsArray = request.getParameterValues("permissions");
            List<String> newPermissions = permissionsArray != null ? new ArrayList<>(List.of(permissionsArray)) : new ArrayList<>();

            // Lấy danh sách quyền cũ để so sánh
            Map<String, Boolean> oldPermissions = userPermissionDAO.getRolePermissions(roleId);

            // Cập nhật quyền mới
            userPermissionDAO.updateRolePermissions(roleId, newPermissions);

            // Cập nhật lại dữ liệu để hiển thị
            Map<String, Boolean> rolePermissions = userPermissionDAO.getRolePermissions(roleId);
            List<Map<String, Object>> allPermissions = userPermissionDAO.getAllPermissions();

            request.setAttribute("role", role);
            request.setAttribute("rolePermissions", rolePermissions);
            request.setAttribute("permissions", allPermissions);
            request.setAttribute("success", "Cập nhật quyền thành công");

            // Chuyển hướng về permissionList để cập nhật giao diện
            response.sendRedirect("permissionList");

        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("userPermission.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Unexpected error: " + e.getMessage());
            request.getRequestDispatcher("userPermission.jsp").forward(request, response);
        }
    }
}