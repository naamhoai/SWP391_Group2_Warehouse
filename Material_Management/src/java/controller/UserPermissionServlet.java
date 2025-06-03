package controller;

import dal.DBContext;
import dao.UserPermissionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UserPermissionServlet", urlPatterns = {"/user-permissions"})
public class UserPermissionServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            UserPermissionDAO userPermissionDAO = new UserPermissionDAO();
            
            // Get search keyword
            String keyword = request.getParameter("keyword");
            List<Map<String, Object>> users;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                // Search users by keyword
                users = userPermissionDAO.searchUser(keyword);
                if (users.isEmpty()) {
                    request.setAttribute("error", "No user found for the keyword. : " + keyword);
                } else if (users.size() > 1) {
                    // Multiple users found, let user choose by userId
                    request.setAttribute("multipleUsers", users);
                    request.setAttribute("message",  "Multiple users found with the name '" + keyword + "'. Please select a user by ID.");
                } else {
                    // Single user found
                    request.setAttribute("users", users);
                    request.setAttribute("userId", users.get(0).get("userId"));
                    request.setAttribute("fullName", users.get(0).get("fullName"));
                    request.setAttribute("roleName", users.get(0).get("roleName"));
                    request.setAttribute("rolePermissions", users.get(0).get("rolePermissions"));
                    request.setAttribute("userPermissions", users.get(0).get("userPermissions"));
                }
            } else {
                // Get all users with their permissions
                users = userPermissionDAO.getAllUsersWithPermissions();
                request.setAttribute("users", users);
            }
            
            // Get all available permissions
            List<Map<String, Object>> permissions = userPermissionDAO.getAllPermissions();
            request.setAttribute("permissions", permissions);
            
            request.getRequestDispatcher("user-permission.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("user-permission.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An unexpected error has occurred: " + e.getMessage());
            request.getRequestDispatcher("user-permission.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}