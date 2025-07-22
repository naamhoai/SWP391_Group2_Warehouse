package controller;

import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
            request.setAttribute("error", "User không tồn tại.");
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

        boolean changed = false;

        if (!fullname.equals(user.getFullname())) {
            if (fullname == null || fullname.trim().split("\\s+").length < 2) {
                request.setAttribute("error", "Họ và tên phải có ít nhất 2 từ.");
                request.setAttribute("user", user);
                request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
                return;
            }
            for (String word : fullname.trim().split("\\s+")) {
                if (!word.isEmpty() && !Character.isUpperCase(word.charAt(0))) {
                    request.setAttribute("error", "Mỗi từ trong họ tên phải viết hoa chữ cái đầu.");
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
                    return;
                }
            }
            user.setFullname(fullname);
            changed = true;
        }
        if (!phone.equals(user.getPhone())) {
            if (phone != null && !phone.isEmpty() && !phone.matches("^0[0-9]{9,10}$")) {
                request.setAttribute("error", "Số điện thoại phải bắt đầu bằng số 0 và có 10-11 chữ số.");
                request.setAttribute("user", user);
                request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
                return;
            }
            user.setPhone(phone);
            changed = true;
        }
        if (!gender.equals(user.getGender())) {
            if (gender == null || gender.trim().isEmpty()) {
                request.setAttribute("error", "Giới tính không được để trống.");
                request.setAttribute("user", user);
                request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
                return;
            }
            user.setGender(gender);
            changed = true;
        }

        Part imagePart = request.getPart("imageFile");
        if (imagePart != null && imagePart.getSize() > 0) {
            String fileName = Path.of(imagePart.getSubmittedFileName()).getFileName().toString();
            String imagePath = "/image/" + fileName;
            if (!imagePath.equals(user.getImage())) {
                String buildImageDir = request.getServletContext().getRealPath("/image");
                File uploadDirFile = new File(buildImageDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }
                imagePart.write(buildImageDir + File.separator + fileName);

                File buildImageDirFile = new File(buildImageDir);
                File projectRoot = buildImageDirFile.getParentFile().getParentFile().getParentFile();
                File sourceImageDirFile = new File(projectRoot, "web/image");
                if (!sourceImageDirFile.exists()) {
                    sourceImageDirFile.mkdirs();
                }
                Path source = Paths.get(buildImageDir, fileName);
                Path target = Paths.get(sourceImageDirFile.getAbsolutePath(), fileName);
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

                user.setImage(imagePath);
                changed = true;
            }
        }

        if (!changed) {
            request.setAttribute("success", "Không có thông tin nào được thay đổi.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
            return;
        }

        boolean updated = userDAO.updateUser(user);
        if (updated) {
            response.sendRedirect("UserDetailServlet?userId=" + user.getUser_id() + "&success=1");
        } else {
            request.setAttribute("error", "Cập nhật thất bại. Vui lòng thử lại.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("updateUserProfile.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
