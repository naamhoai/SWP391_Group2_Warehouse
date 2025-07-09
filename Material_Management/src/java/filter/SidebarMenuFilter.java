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
            User user = (User) session.getAttribute("Admin");
            if (user == null) {
                user = (User) session.getAttribute("user");
            }
            if (user != null) {
                try (Connection conn = new DBContext().getConnection()) {
                    SidebarMenuDAO menuDAO = new SidebarMenuDAO(conn);
                    int userId = user.getUser_id();
                    int roleId = (user.getRole() != null) ? user.getRole().getRoleid() : -1;
                    menuList = menuDAO.getSidebarMenuByUser(userId, roleId);
                    // Log để debug
                    System.out.println("[SidebarMenuFilter] User: " + user.getFullname() +
                        ", RoleID: " + roleId + ", Menu count: " + (menuList != null ? menuList.size() : 0));
                } catch (SQLException e) {
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
