package controller;

import dao.TokenForgetDAO;
import dao.ResetPasswordDAO;
import model.TokenForgetPassword;
import model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name="resetPassword", urlPatterns={"/resetPassword"})
public class ResetPasswordServlet extends HttpServlet {
    private final TokenForgetDAO daoToken = new TokenForgetDAO();
    private final ResetPasswordDAO daoUser = new ResetPasswordDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        HttpSession session = request.getSession();

        if (token != null) {
            TokenForgetPassword tokenObj = daoToken.getTokenPassword(token);
            ResetServiceServlet service = new ResetServiceServlet();

            if (tokenObj == null || tokenObj.isUsed() || service.isExpired(tokenObj.getExpiryTime())) {
                request.setAttribute("mess", "Token is invalid or expired.");
                request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
                return;
            }

            User user = daoUser.getUserById(tokenObj.getUser_id());
            request.setAttribute("email", user.getEmail());
            request.setAttribute("fullname", user.getFullname());
            session.setAttribute("token", token);
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");
        if (fullname == null || fullname.trim().isEmpty()) {
            request.setAttribute("mess", "Full name không được để trống.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("mess", "Mật khẩu xác nhận không khớp.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("token");
        TokenForgetPassword tokenObj = daoToken.getTokenPassword(token);

        ResetServiceServlet service = new ResetServiceServlet();
        if (tokenObj == null || tokenObj.isUsed() || service.isExpired(tokenObj.getExpiryTime())) {
            request.setAttribute("mess", "Token không hợp lệ hoặc đã hết hạn.");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        // Chỉ update password theo email
        daoUser.updatePassword(email, password);

        // Gửi email mật khẩu mới cho user
        User user = daoUser.getUserByEmail(email);
        service.sendPasswordEmail(email, user.getFullname(), password);

        tokenObj.setUsed(true);
        daoToken.updateStatus(tokenObj);

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
