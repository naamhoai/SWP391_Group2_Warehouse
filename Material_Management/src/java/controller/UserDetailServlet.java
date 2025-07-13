package controller;

import dao.DAO;
import model.User;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@WebServlet(urlPatterns = {"/UserDetailServlet"})
public class UserDetailServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();
    private DAO dao = new DAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String userIdParam = request.getParameter("userId");
        int userId;
        if (userIdParam != null) {
            try {
                userId = Integer.parseInt(userIdParam);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid user ID.");
                request.getRequestDispatcher("userProfile.jsp").forward(request, response);
                return;
            }
        } else {
            userId = (int) session.getAttribute("userId");
        }

        User user = userDAO.getUserById(userId);

        if (user != null) {
            request.setAttribute("user", user);
            String formattedDOB = user.getDayofbirth() != null ? user.getDayofbirth() : "";
            request.setAttribute("dob", formattedDOB);

            String successParam = request.getParameter("success");
            if ("1".equals(successParam)) {
                request.setAttribute("success", "Cập nhật thông tin thành công!");
            }

            request.getRequestDispatcher("userProfile.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "User not found.");
            request.getRequestDispatcher("userProfile.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "";
    }
}
