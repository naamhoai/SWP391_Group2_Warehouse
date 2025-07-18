package filter;

import model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AdminAuthFilter", urlPatterns = {
    "/adminDashboard", 
    "/addCategory.jsp",
    "/addSupplier.jsp",
    "/createMaterialDetail.jsp",
    "/createUnit.jsp",
    "/createUser.jsp",
    "/editCategory.jsp",
    "/editSupplier.jsp",
    "/editUnit.jsp",
    "/listCategory.jsp",
    "/listSupplier.jsp",
    "/materialDetailList.jsp",
    "/settingList.jsp",
    "/updateMaterialDetail.jsp",
    "/userPermission.jsp"
})
public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("Admin") != null);
        boolean isAdmin = false;

        if (isLoggedIn) {
            User user = (User) session.getAttribute("Admin");
            if (user != null && user.getRole() != null) {
                isAdmin = (user.getRole().getRoleid() == 1);
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