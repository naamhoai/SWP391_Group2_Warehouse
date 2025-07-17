package controller;

import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;

@WebServlet(name = "changePassword", urlPatterns = {"/changePassword"})
public class ChangePasswordServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("changePassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        int userId = (int) session.getAttribute("userId");
        User user = userDAO.getUserById(userId);
        if (user == null) {
            request.setAttribute("mess", "Không tìm thấy người dùng.");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }
        String oldPassword = request.getParameter("old_password");
        String newPassword = request.getParameter("new_password");
        String confirmPassword = request.getParameter("confirm_password");

        if (oldPassword == null || newPassword == null || confirmPassword == null || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("mess", "Vui lòng nhập đầy đủ các trường.");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            request.setAttribute("mess", "Mật khẩu cũ không đúng.");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }
        if (oldPassword.equals(newPassword)) {
            request.setAttribute("mess", "Mật khẩu mới không được trùng mật khẩu cũ.");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }
        if (newPassword.length() < 8 || newPassword.length() > 32) {
            request.setAttribute("mess", "Mật khẩu mới phải từ 8 đến 32 ký tự.");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }
        if (!newPassword.matches(".*[A-Z].*")) {
            request.setAttribute("mess", "Mật khẩu mới phải có ít nhất 1 chữ in hoa.");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }
        if (!newPassword.matches(".*\\d.*")) {
            request.setAttribute("mess", "Mật khẩu mới phải có ít nhất 1 số.");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }
        if (!newPassword.matches(".*[^a-zA-Z0-9].*")) {
            request.setAttribute("mess", "Mật khẩu mới phải có ít nhất 1 ký tự đặc biệt.");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(hashedPassword);
        boolean updated = userDAO.updateUser(user);
        if (updated) {
            response.sendRedirect("homepage.jsp");
            return;
        } else {
            request.setAttribute("mess", "Đổi mật khẩu thất bại. Vui lòng thử lại!");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
        }
    }
} 