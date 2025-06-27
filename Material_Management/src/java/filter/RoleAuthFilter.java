package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class RoleAuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần xử lý gì khi khởi tạo
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();

        // Bỏ qua kiểm tra với các trang public
        if (uri.contains("/login") || uri.contains("/logout") || uri.contains("/css/") || uri.contains("/js/") || uri.contains("/image/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String role = (String) session.getAttribute("role");
        boolean authorized = false;

        if (uri.startsWith(req.getContextPath() + "/director")) {
            authorized = "director".equals(role);
        } else if (uri.startsWith(req.getContextPath() + "/warehouse")) {
            authorized = "warehouse_staff".equals(role);
        } else if (uri.startsWith(req.getContextPath() + "/employee")) {
            authorized = "company_employee".equals(role);
        } else {
            // Các trang khác, cho phép truy cập (hoặc bạn có thể chỉnh lại logic này)
            authorized = true;
        }

        if (authorized) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "/login.jsp");
        }
    }

    @Override
    public void destroy() {
        // Không cần xử lý gì khi hủy filter
    }
} 