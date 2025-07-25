package filter;

import model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AdminAuthFilter", urlPatterns = {"/*"})
public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();
        // Loại trừ file tĩnh ở mọi thư mục và các trang public
        if (path.matches(".*(\\.css|\\.js|\\.png|\\.jpg|\\.jpeg|\\.gif|\\.ico|\\.woff|\\.woff2|\\.ttf|\\.svg)$")
            || path.endsWith("login.jsp") || path.endsWith("login") || path.endsWith("register.jsp") || path.endsWith("resetPassword.jsp")) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = httpRequest.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("Admin") != null);
        boolean isAdmin = false;

        if (isLoggedIn) {
            User user = (User) session.getAttribute("Admin");
            System.out.println("Session Admin: " + user);
            if (user != null && user.getRole() != null) {
                int roleId = user.getRole().getRoleid();
                System.out.println("Role id: " + roleId);
                // Cho phép tất cả các role
                isAdmin = (roleId == 1 || roleId == 2 || roleId == 3 || roleId == 4);
            }
        }

        if (isLoggedIn && isAdmin) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
} 