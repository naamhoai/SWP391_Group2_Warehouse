package controller;

import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 15
)
public class UpdateUserProfileServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        User user = userDAO.getUserById(userId);

        if (user != null) {
            request.setAttribute("user", user);
            request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "User not found.");
            request.getRequestDispatcher("userProfile.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null || session.getAttribute("roleId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userIdToEdit = Integer.parseInt(request.getParameter("user_id"));
        User user = userDAO.getUserById(userIdToEdit);

        if (user == null) {
            request.setAttribute("error", "User không tồn tại.");
            request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
            return;
        }

        String fullname = request.getParameter("fullname");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String dobInput = request.getParameter("dayofbirth");
        String password = request.getParameter("password");

        user.setFullname(fullname);
        user.setPhone(phone);
        user.setGender(gender);
        user.setDayofbirth(dobInput);

        if (fullname == null || fullname.trim().isEmpty()) {
            request.setAttribute("error", "Họ và tên không được để trống.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
            return;
        }
        if (fullname.length() > 100) {
            request.setAttribute("error", "Họ và tên không được vượt quá 100 ký tự.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
            return;
        }
        if (!fullname.matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
            request.setAttribute("error", "Họ và tên chỉ được chứa chữ cái và khoảng trắng.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
            return;
        }

        if (phone != null && !phone.trim().isEmpty() && !phone.matches("^[0-9]{10,11}$")) {
            request.setAttribute("error", "Số điện thoại phải có 10-11 chữ số.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
            return;
        }

        if (dobInput == null || dobInput.trim().isEmpty()) {
            request.setAttribute("error", "Ngày sinh không được để trống.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
            return;
        }
        try {
            java.time.LocalDate dob = java.time.LocalDate.parse(dobInput);
            if (dob.isAfter(java.time.LocalDate.now())) {
                request.setAttribute("error", "Ngày sinh không được lớn hơn ngày hiện tại.");
                request.setAttribute("user", user);
                request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
                return;
            }
            
            java.time.LocalDate minDate = java.time.LocalDate.now().minusYears(120);
            if (dob.isBefore(minDate)) {
                request.setAttribute("error", "Ngày sinh không hợp lệ. Tuổi không được vượt quá 120.");
                request.setAttribute("user", user);
                request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
                return;
            }
        } catch (java.time.format.DateTimeParseException e) {
            request.setAttribute("error", "Ngày sinh không hợp lệ.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
            return;
        }

        if (password != null && !password.trim().isEmpty()) {
            if (!userDAO.isValidPassword(password)) {
                request.setAttribute("error", "Mật khẩu không hợp lệ! Cần có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.");
                request.setAttribute("user", user);
                request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
                return;
            }
            String hashedPassword = userDAO.hashPassword(password);
            user.setPassword(hashedPassword);
        }
        
        Part imagePart = request.getPart("imageFile");
        if (imagePart != null && imagePart.getSize() > 0) {
            String fileName = Path.of(imagePart.getSubmittedFileName()).getFileName().toString();

            if (!fileName.matches("(?i)^.+\\.(jpg|jpeg|png|gif)$")) {
                request.setAttribute("error", "File ảnh không hợp lệ. Chỉ cho phép jpg, jpeg, png, gif.");
                request.setAttribute("user", user);
                request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
                return;
            }

            String imagePath = "/image/" + fileName;
            imagePart.write(getServletContext().getRealPath(imagePath));
            user.setImage(imagePath);
        }

        boolean updated = userDAO.updateUser(user);
        if (updated) {
            response.sendRedirect("UserDetailServlet?user_id=" + user.getUser_id());
        } else {
            request.setAttribute("error", "Update failed.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
