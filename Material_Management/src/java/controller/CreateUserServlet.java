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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
        String email = request.getParameter("email");

        request.setAttribute("fullName", fullName);
        request.setAttribute("phone", phone);
        request.setAttribute("status", status);
        request.setAttribute("priority", priorityStr);
        request.setAttribute("gender", gender);
        request.setAttribute("email", email);
        request.setAttribute("roleId", roleIdStr);
        request.setAttribute("password", password);
        request.setAttribute("roleList", roleDAO.getAllRolesExceptAdmin());

        if (fullName == null || fullName.trim().isEmpty() || fullName.length() > 100 || !fullName.matches("^[a-zA-ZÀ-ỹ\s]+$")) {
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
        int roleId = Integer.parseInt(roleIdStr);
        if (gender == null || gender.trim().isEmpty()) {
            request.setAttribute("error", "Giới tính là bắt buộc.");
            doGet(request, response);
            return;
        }
        if (password == null || password.trim().isEmpty() || password.length() < 8 || password.length() > 50 || !userDAO.isValidPassword(password)) {
            request.setAttribute("error", "Mật khẩu không hợp lệ.");
            doGet(request, response);
            return;
        }
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
            imagePath = "/image/" + originalFileName;
            String buildImageDir = request.getServletContext().getRealPath("/image");
            File buildDirFile = new File(buildImageDir);
            if (!buildDirFile.exists()) {
                buildDirFile.mkdirs();
            }
            filePart.write(buildImageDir + File.separator + originalFileName);
            File buildImageDirFile = new File(buildImageDir);
            File projectRoot = buildImageDirFile.getParentFile().getParentFile().getParentFile(); // build/web/image -> build/web -> build -> project
            File sourceImageDirFile = new File(projectRoot, "web/image");
            if (!sourceImageDirFile.exists()) {
                sourceImageDirFile.mkdirs();
            }
            Path source = Paths.get(buildImageDir, originalFileName);
            Path target = Paths.get(sourceImageDirFile.getAbsolutePath(), originalFileName);
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }

        String hashedPassword = userDAO.hashPassword(password);

        User newUser = new User();
        Role role = new Role();
        role.setRoleid(roleId);
        newUser.setFullname(fullName);
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        newUser.setPhone(phone);
        newUser.setRole(role);
        newUser.setStatus(status);
        newUser.setImage(imagePath);
        newUser.setGender(gender);

        boolean success = userDAO.insertUser(newUser);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/settinglist?success=add");
        } else {
            request.setAttribute("error", "Tạo người dùng thất bại. Vui lòng thử lại!");
            request.setAttribute("roleList", roleDAO.getAllRolesExceptAdmin());
            doGet(request, response);
        }
    }
}
