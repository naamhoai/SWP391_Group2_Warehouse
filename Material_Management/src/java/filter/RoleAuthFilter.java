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

        if (path.equals("/director")) {
            authorized = (roleId == 1 || roleId == 2);
        } else if (path.equals("/warehouseStaffDashboard.jsp")) {
            authorized = (roleId == 1 || roleId == 3);
        } else if (path.equals("/staffDashboard.jsp")) {
            authorized = (roleId == 1 || roleId == 4);
        } else if (path.equals("/homepage.jsp")) {
            authorized = true;
        } else {
            authorized = true;
        }

        if (authorized) {
            System.out.println("User " + user.getFullname() + " (RoleID: " + roleId + ") authorized to access: " + path);
            chain.doFilter(request, response);
        } else {
            req.setAttribute("error", "Bạn không có quyền truy cập chức năng này!");
            req.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}