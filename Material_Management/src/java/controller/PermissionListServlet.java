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

        // Kiểm tra đăng nhập
        if (session.getAttribute("Admin") == null) {
            String currentURL = request.getRequestURI();
            session.setAttribute("redirectURL", currentURL);
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Lấy roleId từ session
        Integer roleId = (Integer) session.getAttribute("roleId");
        if (roleId == null) {
            request.setAttribute("error", "Không tìm thấy vai trò người dùng");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // Nếu không phải admin, kiểm tra quyền
        if (roleId != 1) {
            try {
                UserPermissionDAO userPermissionDAO = new UserPermissionDAO();
                Map<String, Boolean> rolePermissions = userPermissionDAO.getRolePermissions(roleId);
                Set<String> userPermissions = rolePermissions.keySet();
                if (!userPermissions.contains("user_view")) { // Hoặc "permission_view" nếu bạn có quyền này
                    request.setAttribute("error", "Bạn không có quyền truy cập trang này");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Lỗi khi kiểm tra quyền: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
        }

        try {
            PermissionListDAO permissionListDAO = new PermissionListDAO();
            UserDAO userDAO = new UserDAO();
            UserPermissionDAO userPermissionDAO = new UserPermissionDAO();

            // Lấy toàn bộ quyền
            List<Map<String, Object>> allPermissions = permissionListDAO.getAllPermissions();
            // Lấy toàn bộ vai trò
            List<Role> roles = userDAO.getAllRoles();

            // Lấy quyền của từng role (Map<roleId, Map<permissionName, Boolean>>)
            Map<Integer, Map<String, Boolean>> rolePermissions = new HashMap<>();
            for (Role role : roles) {
                Map<String, Boolean> perms = userPermissionDAO.getRolePermissions(role.getRoleid());
                rolePermissions.put(role.getRoleid(), perms);
            }

            request.setAttribute("allPermissions", allPermissions);
            request.setAttribute("roles", roles);
            request.setAttribute("rolePermissions", rolePermissions);

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
    }// </editor-fold>

}
