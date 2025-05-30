/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import model.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Path;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1MB bộ đệm
        maxFileSize = 1024 * 1024 * 10, // 10MB tối đa 1 file
        maxRequestSize = 1024 * 1024 * 15 // 15MB tổng request
)
public class UpdateUserProfileServlet extends HttpServlet {

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
            out.println("<title>Servlet UpdateUserServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateUserServlet at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null || session.getAttribute("roleId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy tham số user_id từ URL
        String userIdStr = request.getParameter("user_id");
        if (userIdStr == null || userIdStr.isEmpty()) {
            request.setAttribute("error", "User ID not provided.");
            request.getRequestDispatcher("userdetail.jsp").forward(request, response);
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid User ID format.");
            request.getRequestDispatcher("userdetail.jsp").forward(request, response);
            return;
        }

        // Lấy thông tin người dùng từ cơ sở dữ liệu
        User user = userDAO.getUserById(userId);

        if (user != null) {
            // Truyền thông tin người dùng vào request
            String formattedDOB = user.getDayofbirth() != null ? user.getDayofbirth() : "";
            request.setAttribute("dob", formattedDOB);
            request.setAttribute("user", user);
            request.getRequestDispatcher("updateuserprofile.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "User not found.");
            request.getRequestDispatcher("userdetail.jsp").forward(request, response);
        }
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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null || session.getAttribute("roleId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userIdToEdit = Integer.parseInt(request.getParameter("user_id"));
        User user = userDAO.getUserById(userIdToEdit);

        if (user == null) {
            request.setAttribute("error", "User không tồn tại.");
            request.getRequestDispatcher("updateuserprofile.jsp").forward(request, response);
            return;
        }

        // Lấy thông tin từ form
        String username = request.getParameter("username");
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String dobInput = request.getParameter("dayofbirth");
        String description = request.getParameter("description");

        // Lấy ảnh đại diện (nếu có)
        Part imagePart = request.getPart("imageFile");
        String imagePath = null;
        if (imagePart != null && imagePart.getSize() > 0) {
            // Lưu ảnh vào thư mục server (cập nhật đường dẫn ảnh vào cơ sở dữ liệu)
            String fileName = Path.of(imagePart.getSubmittedFileName()).getFileName().toString();
            String uploadDir = getServletContext().getRealPath("/uploads"); // Thư mục lưu ảnh
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            // Lưu ảnh vào thư mục
            imagePath = "/uploads/" + fileName;
            imagePart.write(uploadDir + File.separator + fileName);
        }

        // Cập nhật thông tin cho đối tượng user
        user.setUsername(username);
        user.setFullname(fullname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setGender(gender);
        user.setDescription(description);

        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(password);
        }
        if (dobInput != null && !dobInput.trim().isEmpty()) {
            // Validate hoặc chuyển đổi định dạng nếu cần
            user.setDayofbirth(dobInput); // vì dayofbirth là String
        }

        if (imagePath != null) {
            user.setImage(imagePath); // Cập nhật ảnh đại diện nếu có
        }

        // Cập nhật thông tin người dùng vào cơ sở dữ liệu
        boolean updated = userDAO.updateUser(user);
        if (updated) {
            response.sendRedirect("UserDetailServlet?user_id=" + user.getUser_id());
        } else {
            request.setAttribute("error", "Cập nhật thất bại.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("updateuserprofile.jsp").forward(request, response);
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
