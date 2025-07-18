package controller;

import dao.PermissionListDAO;
import dao.UserPermissionDAO;
import dao.UserDAO;
import model.Role;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/permissionList")
public class PermissionListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Debug: Kiểm tra session
        System.out.println("=== DEBUG PERMISSION LIST SERVLET ===");
        System.out.println("Admin attribute: " + session.getAttribute("Admin"));
        System.out.println("User attribute: " + session.getAttribute("user"));
        System.out.println("Role ID: " + session.getAttribute("role_id"));

        // Kiểm tra đăng nhập - cho phép tất cả role đã đăng nhập
        Object adminUser = session.getAttribute("Admin");
        Object normalUser = session.getAttribute("user");
        
        if (adminUser == null && normalUser == null) {
            System.out.println("Không có user nào trong session, redirect to login");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Integer roleId = (Integer) session.getAttribute("role_id");
        if (roleId == null) {
            System.out.println("Không có role_id trong session");
            request.setAttribute("error", "Không tìm thấy vai trò người dùng");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        System.out.println("User đã đăng nhập với roleId: " + roleId);

        // Cho phép tất cả role đã đăng nhập xem danh sách quyền
        // Chỉ Admin mới có quyền chỉnh sửa (được xử lý trong JSP)

        try {
            PermissionListDAO permissionListDAO = new PermissionListDAO();
            UserDAO userDAO = new UserDAO();
            UserPermissionDAO userPermissionDAO = new UserPermissionDAO();

            List<Map<String, Object>> allPermissions = permissionListDAO.getAllPermissions();
            List<Role> roles = userDAO.getAllRoles();

            Map<Integer, Map<String, Boolean>> rolePermissions = new HashMap<>();
            for (Role role : roles) {
                Map<String, Boolean> perms = userPermissionDAO.getRolePermissions(role.getRoleid());
                rolePermissions.put(role.getRoleid(), perms);
            }

            request.setAttribute("allPermissions", allPermissions);
            request.setAttribute("roles", roles);
            request.setAttribute("rolePermissions", rolePermissions);
            request.setAttribute("currentRoleId", roleId); // Để JSP biết role hiện tại

            System.out.println("Chuyển đến permissionList.jsp");
            request.getRequestDispatcher("permissionList.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách quyền: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}