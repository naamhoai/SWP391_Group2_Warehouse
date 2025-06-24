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
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");

        if (fullname == null || fullname.trim().isEmpty()) {
            request.setAttribute("mess", "Full name không được để trống.");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        ResetPasswordDAO daoUser = new ResetPasswordDAO();
        User user = daoUser.getUserByEmailAndFullname(email, fullname);

        if (user == null) {
            request.setAttribute("mess", "Email hoặc họ tên không đúng.");
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

        String adminEmail = "ezminh216@gmail.com";
        String contentName = "Yêu cầu reset mật khẩu từ: " + user.getFullname() + " (" + user.getEmail() + ")";

        if (!service.sendEmail(adminEmail, resetLink, contentName)) {
            request.setAttribute("mess", "Không thể gửi email đến quản trị viên.");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        request.setAttribute("mess", "Yêu cầu đặt lại mật khẩu đã được gửi đến quản trị viên.");
        request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
    }
}
