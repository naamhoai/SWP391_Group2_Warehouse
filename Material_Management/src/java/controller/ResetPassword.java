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
public class ResetPassword extends HttpServlet {
    private final TokenForgetDAO daoToken = new TokenForgetDAO();
    private final ResetPasswordDAO daoUser = new ResetPasswordDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        HttpSession session = request.getSession();

        if (token != null) {
            TokenForgetPassword tokenObj = daoToken.getTokenPassword(token);
            ResetService service = new ResetService();

            if (tokenObj == null || tokenObj.isUsed() || service.isExpired(tokenObj.getExpiryTime())) {
                request.setAttribute("mess", "Token is invalid or expired.");
                request.getRequestDispatcher("requestpassword.jsp").forward(request, response);
                return;
            }

            User user = daoUser.getUserById(tokenObj.getUser_id());  // sửa
            request.setAttribute("email", user.getEmail());
            request.setAttribute("username", user.getUsername());
            session.setAttribute("token", token);
            request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("requestpassword.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        if (!password.equals(confirmPassword)) {
            request.setAttribute("mess", "Mật khẩu xác nhận không khớp.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("token");
        TokenForgetPassword tokenObj = daoToken.getTokenPassword(token);

        ResetService service = new ResetService();
        if (tokenObj == null || tokenObj.isUsed() || service.isExpired(tokenObj.getExpiryTime())) {
            request.setAttribute("mess", "Token không hợp lệ hoặc đã hết hạn.");
            request.getRequestDispatcher("requestpassword.jsp").forward(request, response);
            return;
        }

        daoUser.updatePassword(email, password);
        tokenObj.setUsed(true); // sửa tên hàm setter
        daoToken.updateStatus(tokenObj);

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
