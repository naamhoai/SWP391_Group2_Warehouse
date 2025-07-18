package filter;

import dao.SidebarMenuDAO;
import dal.DBContext;
import model.User;
import model.SidebarMenu;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebFilter("/*")
public class SidebarMenuFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        List<SidebarMenu> menuList = new ArrayList<>();

        if (session != null) {
            User currentUser = (User) session.getAttribute("Admin");
            if (currentUser == null) {
                currentUser = (User) session.getAttribute("user");
            }

            if (currentUser != null) {
                try (Connection conn = new DBContext().getConnection()) {
                    SidebarMenuDAO menuDAO = new SidebarMenuDAO(conn);
                    int userId = currentUser.getUser_id();
                    int roleId = (currentUser.getRole() != null) ? currentUser.getRole().getRoleid() : -1;
                    menuList = menuDAO.getSidebarMenuByUser(userId, roleId);
                } catch (SQLException e) {
                    System.err.println("Lỗi khi load sidebar menu: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        
        request.setAttribute("sidebarMenuList", menuList);
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
