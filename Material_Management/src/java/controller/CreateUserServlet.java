package controller;

import dao.RoleDAO;
import dao.UserDAO;
import model.Role;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 15
)
public class CreateUserServlet extends HttpServlet {

    private RoleDAO roleDAO = new RoleDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roleList = roleDAO.getAllRolesExceptAdmin();
        request.setAttribute("roleList", roleList);
        request.getRequestDispatcher("createUser.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullName");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String roleIdStr = request.getParameter("roleId");
        String status = request.getParameter("status");
        String priorityStr = request.getParameter("priority");
        String gender = request.getParameter("gender");
        String dayofbirth = request.getParameter("dayofbirth");
        String description = request.getParameter("description");
        String email = request.getParameter("email");

        request.setAttribute("fullName", fullName);
        request.setAttribute("phone", phone);
        request.setAttribute("status", status);
        request.setAttribute("priority", priorityStr);
        request.setAttribute("gender", gender);
        request.setAttribute("dayofbirth", dayofbirth);
        request.setAttribute("description", description);
        request.setAttribute("email", email);
        request.setAttribute("roleId", roleIdStr);
        request.setAttribute("password", password);
        request.setAttribute("roleList", roleDAO.getAllRolesExceptAdmin());

        if (fullName == null || fullName.trim().isEmpty() || fullName.length() > 100 || !fullName.matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
            request.setAttribute("error", "Họ và tên không hợp lệ.");
            doGet(request, response);
            return;
        }

        String[] words = fullName.trim().split("\\s+");
        if (words.length < 2) {
            request.setAttribute("error", "Họ và tên phải có ít nhất 2 từ.");
            doGet(request, response);
            return;
        }
        
        boolean validFormat = true;
        for (String word : words) {
            if (!word.isEmpty() && !Character.isUpperCase(word.charAt(0))) {
                validFormat = false;
                break;
            }
        }
        if (!validFormat) {
            request.setAttribute("error", "Mỗi từ trong họ tên phải viết hoa chữ cái đầu.");
            doGet(request, response);
            return;
        }

        if (email == null || email.trim().isEmpty() || userDAO.existsEmail(email)) {
            request.setAttribute("error", "Email không hợp lệ hoặc đã tồn tại.");
            doGet(request, response);
            return;
        }

        if (phone != null && !phone.trim().isEmpty() && !phone.matches("^[0-9]{10,11}$")) {
            request.setAttribute("error", "Số điện thoại phải có 10-11 chữ số.");
            doGet(request, response);
            return;
        }

        if (description != null && description.length() > 500) {
            request.setAttribute("error", "Mô tả không được vượt quá 500 ký tự.");
            doGet(request, response);
            return;
        }

        int roleId = Integer.parseInt(roleIdStr); 

        int priority;
        try {
            priority = Integer.parseInt(priorityStr);
            if (priority < 2 || priority > 4) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mức ưu tiên chỉ được nhập từ 2 đến 4 (Giám đốc-2, Nhân viên kho-3, Nhân viên-4).");
            doGet(request, response);
            return;
        }

        if (gender == null || gender.trim().isEmpty()) {
            request.setAttribute("error", "Giới tính là bắt buộc.");
            doGet(request, response);
            return;
        }

        if (dayofbirth == null || dayofbirth.trim().isEmpty()) {
            request.setAttribute("error", "Ngày sinh không được để trống.");
            doGet(request, response);
            return;
        }

        try {
            java.time.LocalDate dob = java.time.LocalDate.parse(dayofbirth);
            if (dob.isAfter(java.time.LocalDate.now())) {
                request.setAttribute("error", "Ngày sinh không được lớn hơn ngày hiện tại.");
                doGet(request, response);
                return;
            }
        } catch (java.time.format.DateTimeParseException e) {
            request.setAttribute("error", "Ngày sinh không hợp lệ.");
            doGet(request, response);
            return;
        }

        if (password == null || password.trim().isEmpty() || password.length() < 8 || password.length() > 50 || !userDAO.isValidPassword(password)) {
            request.setAttribute("error", "Mật khẩu không hợp lệ.");
            doGet(request, response);
            return;
        }

        String hashedPassword = userDAO.hashPassword(password);

        Part filePart = request.getPart("imageFile");
        String imagePath = null;
        if (filePart != null && filePart.getSize() > 0) {
            String originalFileName = Path.of(filePart.getSubmittedFileName()).getFileName().toString();
            String fileExt = "";
            int i = originalFileName.lastIndexOf('.');
            if (i > 0) {
                fileExt = originalFileName.substring(i + 1).toLowerCase();
            }
            if (!fileExt.matches("jpg|jpeg|png|gif")) {
                request.setAttribute("error", "File ảnh không hợp lệ. Chỉ cho phép jpg, jpeg, png, gif.");
                doGet(request, response);
                return;
            }
            String uploadDir = getServletContext().getRealPath("/image");
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            filePart.write(uploadDir + File.separator + originalFileName);
            imagePath = "/image/" + originalFileName;
        }

        User newUser = new User();
        Role role = new Role();
        role.setRoleid(roleId);
        newUser.setFullname(fullName);
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        newUser.setPhone(phone);
        newUser.setRole(role);
        newUser.setStatus(status);
        newUser.setPriority(priority);
        newUser.setImage(imagePath);
        newUser.setGender(gender);
        newUser.setDayofbirth(dayofbirth);
        newUser.setDescription(description);

        boolean success = userDAO.insertUser(newUser);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/settinglist");
        } else {
            request.setAttribute("error", "Tạo người dùng thất bại. Vui lòng thử lại!");
            request.setAttribute("roleList", roleDAO.getAllRolesExceptAdmin());
            doGet(request, response);
        }
    }
}
