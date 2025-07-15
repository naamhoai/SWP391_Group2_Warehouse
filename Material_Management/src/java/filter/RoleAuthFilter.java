package filter;

import model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "RoleAuthFilter", urlPatterns = {
    "/director",
    "/warehouseStaffDashboard.jsp", 
    "/staffDashboard.jsp",
    "/homepage.jsp"
})
public class RoleAuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        // Bỏ qua các tài nguyên tĩnh
        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/image/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null) {
            res.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("Admin");
        if (user == null) {
            user = (User) session.getAttribute("user");
        }
        
        if (user == null) {
            res.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        boolean authorized = false;
        int roleId = (user.getRole() != null) ? user.getRole().getRoleid() : -1;

        // Kiểm tra quyền truy cập theo vai trò
        if (path.equals("/director")) {
            // Director (roleid = 2) hoặc Admin (roleid = 1)
            authorized = (roleId == 1 || roleId == 2);
        } else if (path.equals("/warehouseStaffDashboard.jsp")) {
            // Warehouse Staff (roleid = 3) hoặc Admin (roleid = 1)
            authorized = (roleId == 1 || roleId == 3);
        } else if (path.equals("/staffDashboard.jsp")) {
            // Company Employee (roleid = 4) hoặc Admin (roleid = 1)
            authorized = (roleId == 1 || roleId == 4);
        } else if (path.equals("/homepage.jsp")) {
            // Tất cả user đã đăng nhập đều có thể truy cập homepage
            authorized = true;
        } else {
            // Các trang khác không trong danh sách kiểm tra
            authorized = true;
        }

        if (authorized) {
            System.out.println("User " + user.getFullname() + " (RoleID: " + roleId + ") authorized to access: " + path);
            chain.doFilter(request, response);
        } else {
System.out.println("User " + user.getFullname() + " (RoleID: " + roleId + ") denied access to: " + path);
            // Chuyển hướng sang trang báo lỗi quyền truy cập
            req.setAttribute("error", "Bạn không có quyền truy cập chức năng này!");
            req.getRequestDispatcher("error.jsp").forward(request, response);
            // Hoặc nếu muốn redirect:
            // res.sendRedirect(contextPath + "/error.jsp?msg=role_not_authorized");
        }
    }

    @Override
    public void destroy() {
    }
}