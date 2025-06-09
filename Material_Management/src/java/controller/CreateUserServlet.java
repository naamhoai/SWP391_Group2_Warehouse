/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.RoleDAO;
import dao.UserDAO;
import model.Role;
import model.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.sql.SQLException;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1MB bộ đệm
        maxFileSize = 1024 * 1024 * 10, // 10MB tối đa 1 file
        maxRequestSize = 1024 * 1024 * 15 // 15MB tổng request
)
public class CreateUserServlet extends HttpServlet {

    private RoleDAO roleDAO = new RoleDAO();
    private UserDAO userDAO = new UserDAO();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CreateUserServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateUserServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roleList = roleDAO.getAllRolesExceptAdmin();
        request.setAttribute("roleList", roleList);
        request.getRequestDispatcher("createUser.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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

        String email = null;
        int tries = 0;
        do {
            if (tries++ > 5) {
                request.setAttribute("error", "Không thể tạo email không trùng. Vui lòng đổi tên khác.");
                doGet(request, response);
                return;
            }
            email = userDAO.generateEmail(fullName);
        } while (userDAO.existsEmail(email));

        int roleId = 0;
        int priority;

        // Giữ lại dữ liệu nhập để trả về form khi lỗi
        request.setAttribute("fullName", fullName);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("status", status);
        request.setAttribute("priority", priorityStr);
        request.setAttribute("gender", gender);
        request.setAttribute("dayofbirth", dayofbirth);
        request.setAttribute("description", description);
       
        if (fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("error", "Họ và tên không được để trống!");
            doGet(request, response);
            return;
        }

        // Sinh email tránh trùng
        if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
            request.setAttribute("roleIdError", "Bạn phải chọn vai trò.");
            doGet(request, response);
            return;
        }
        try {
            roleId = Integer.parseInt(roleIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("roleIdError", "Vai trò không hợp lệ.");
            doGet(request, response);
            return;
        }
        if (roleId == 1) {
            request.setAttribute("error", "Bạn không được phép tạo user với vai trò Admin!");
            doGet(request, response);
            return;
        }

        try {
            priority = Integer.parseInt(priorityStr);
            if (priority < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mức ưu tiên phải là số nguyên >= 0.");
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
                request.setAttribute("error", "Chỉ cho phép upload file ảnh định dạng jpg, jpeg, png, gif.");
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
        if (gender == null || gender.trim().isEmpty()) {
            request.setAttribute("error", "Bạn phải chọn giới tính.");
            doGet(request, response);
            return;
        }

        request.setAttribute("dayofbirth", dayofbirth);

        if (dayofbirth == null || dayofbirth.trim().isEmpty()) {
            request.setAttribute("error", "Ngày sinh không được để trống!");
            doGet(request, response);
            return;
        }

        try {
            java.time.LocalDate dob = java.time.LocalDate.parse(dayofbirth);
            java.time.LocalDate today = java.time.LocalDate.now();
            if (dob.isAfter(today)) {
                request.setAttribute("error", "Ngày sinh không được lớn hơn ngày hiện tại!");
                doGet(request, response);
                return;
            }
        } catch (java.time.format.DateTimeParseException e) {
            request.setAttribute("error", "Ngày sinh không hợp lệ!");
            doGet(request, response);
            return;
        }

        if (!userDAO.isValidPassword(password)) {
            request.setAttribute("error", "Mật khẩu không hợp lệ! Cần có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.");
            doGet(request, response);
            return;
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
            List<Role> roleList = roleDAO.getAllRolesExceptAdmin();
            request.setAttribute("roleList", roleList);
            doGet(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
