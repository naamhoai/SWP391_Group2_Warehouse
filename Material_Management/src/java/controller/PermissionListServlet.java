
package controller;

import dao.PermissionListDAO;
import dao.UserPermissionDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Role;

@WebServlet("/permissionList")
public class PermissionListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Check if user is logged in
        if (session.getAttribute("Admin") == null) {
            String currentURL = request.getRequestURI();
            session.setAttribute("redirectURL", currentURL);
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // For admin role (roleId = 1), allow access without checking specific permissions
        Integer roleId = (Integer) session.getAttribute("roleId");
        if (roleId == null) {
            request.setAttribute("error", "Không tìm thấy vai trò người dùng");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        if (roleId != 1) {
            // Check if user has permission to view permissions
            try {
                UserPermissionDAO userPermissionDAO = new UserPermissionDAO();
                Map<String, Boolean> rolePermissions = userPermissionDAO.getRolePermissions(roleId);
                Set<String> userPermissions = rolePermissions.keySet();
                if (!userPermissions.contains("user_view")) {
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
            
            // Get permissions grouped by module
            Map<String, List<Map<String, Object>>> permissions = permissionListDAO.getPermissionsByModule();
            
            // Get roles
            List<Role> roles = userDAO.getAllRoles(); // Cần thêm phương thức này vào UserDAO
            
            // Add success message if redirected from permission update
            String success = request.getParameter("success");
            if (success != null) {
                request.setAttribute("success", "Cập nhật quyền thành công");
            }
            
            request.setAttribute("permissions", permissions);
            request.setAttribute("roles", roles);
            request.getRequestDispatcher("permissionList.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách quyền: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
    