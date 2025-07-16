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

@WebServlet(name = "requestPassword", urlPatterns = {"/requestPassword"})
public class RequestPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        if (email != null) email = email.trim().toLowerCase();

        ResetPasswordDAO daoUser = new ResetPasswordDAO();
        User user = daoUser.getUserByEmail(email);

        if (user == null) {
            request.setAttribute("mess", "Email không đúng hoặc không tồn tại.");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        ResetServiceServlet service = new ResetServiceServlet();
        String token = service.generateToken();
        String resetLink = "http://localhost:8080/Material_Management/resetPassword?token=" + token;

        TokenForgetPassword tokenEntity = new TokenForgetPassword(0, user.getUser_id(), false, token, service.expireDateTime());
        TokenForgetDAO daoToken = new TokenForgetDAO();

        if (!daoToken.insertTokenForget(tokenEntity)) {
            request.setAttribute("mess", "Lỗi hệ thống khi lưu token.");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        String contentName = user.getFullname();
        if (!service.sendEmail(user.getEmail(), resetLink, contentName)) {
            request.setAttribute("mess", "Không thể gửi email đặt lại mật khẩu. Vui lòng thử lại.");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        request.setAttribute("mess", "Đã gửi email đặt lại mật khẩu tới địa chỉ của bạn. Vui lòng kiểm tra email để tiếp tục.");
        request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
    }
}
