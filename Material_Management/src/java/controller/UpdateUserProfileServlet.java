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

        int sessionUserId = (Integer) session.getAttribute("userId");
        int sessionRoleId = (Integer) session.getAttribute("roleId");

        int userIdToEdit;

        if (sessionRoleId == 1) { // admin
            String paramUserId = request.getParameter("user_id");
            if (paramUserId != null) {
                try {
                    userIdToEdit = Integer.parseInt(paramUserId);
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user_id parameter");
                    return;
                }
            } else {
                userIdToEdit = sessionUserId; // nếu không truyền thì admin sửa chính mình
            }
        } else {
            // user thường chỉ sửa chính mình
            userIdToEdit = sessionUserId;
        }

        User user = userDAO.getUserById(userIdToEdit);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("updateuserprofile.jsp").forward(request, response);
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

        int sessionUserId = (Integer) session.getAttribute("userId");
        int sessionRoleId = (Integer) session.getAttribute("roleId");

        int userIdToEdit;
        if (sessionRoleId == 1) { // admin
            String paramUserId = request.getParameter("user_id");
            if (paramUserId != null) {
                try {
                    userIdToEdit = Integer.parseInt(paramUserId);
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user_id parameter");
                    return;
                }
            } else {
                userIdToEdit = sessionUserId; // nếu không truyền thì admin sửa chính mình
            }
        } else {
            // user thường chỉ sửa chính mình
            userIdToEdit = sessionUserId;
        }

        String username = request.getParameter("username");
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        Part filePart = request.getPart("imageFile");
        String existingImage = request.getParameter("existingImage");

        User user = userDAO.getUserById(userIdToEdit);
        if (user == null) {
            request.setAttribute("error", "User không tồn tại.");
            request.getRequestDispatcher("updatesuerprofile.jsp").forward(request, response);
            return;
        }

        user.setUsername(username);
        user.setFullname(fullname);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
// Xử lý ảnh đại diện
        if (filePart != null && filePart.getSize() > 0) {
            // Lấy tên file gốc
            String originalFileName = Path.of(filePart.getSubmittedFileName()).getFileName().toString();
            // Tạo thư mục lưu file ảnh
            String uploadDir = getServletContext().getRealPath("/image");
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            // Lưu file
            filePart.write(uploadDir + File.separator + originalFileName);
            user.setImage("/image/" + originalFileName);
        } else if (existingImage != null && !existingImage.trim().isEmpty()) {
            // Nếu không upload file mới mà chọn ảnh có sẵn
            user.setImage("/" + existingImage);
        } // else giữ nguyên ảnh cũ

        // Giữ nguyên vai trò, trạng thái, ưu tiên
        // user.setRole_id(user.getRole_id());
        // user.setStatus(user.getStatus());
        // user.setPriority(user.getPriority());
        boolean updated = userDAO.updateUser(user);
        if (updated) {
            response.sendRedirect(request.getContextPath() + "/userDetailServlet?userId=" + userIdToEdit);
        } else {
            request.setAttribute("error", "Cập nhật thất bại, vui lòng thử lại.");
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
