/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import model.*;
import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author kien3
 */
@WebServlet(name = "login", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

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
            out.println("<title>Servlet Login</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Login at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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
        HttpSession session = request.getSession();
        String gmail = request.getParameter("gmail");
        String pass = request.getParameter("pass");

        DAO dao = new DAO();

        List<User> users = dao.userAccount();
        if (users != null && !users.isEmpty()) {

            for (User user2 : users) {
                if (gmail.equals(user2.getEmail())) {
                    if (!"active".equalsIgnoreCase(user2.getStatus())) {
                        request.setAttribute("mess", "Tài khoản đã bị khóa.");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                        return;
                    }
                    String Password = user2.getPassword();
                    boolean isBCrypt = Password != null && (
                        Password.startsWith("$2a$") ||
                        Password.startsWith("$2b$") ||
                        Password.startsWith("$2y$")
                    );
                    boolean valid = isBCrypt ? org.mindrot.jbcrypt.BCrypt.checkpw(pass, Password) : pass.equals(Password);
                    if (valid) {
                        session.setAttribute("Admin", user2);
                        session.setAttribute("userId", user2.getUser_id());
                        session.setAttribute("role_id", user2.getRole().getRoleid());
                        String redirectURL = (String) session.getAttribute("redirectURL");
                        if (redirectURL != null) {
                            session.removeAttribute("redirectURL");
                            response.sendRedirect(request.getContextPath() + redirectURL);
                            return;
                        }
                        int roleId = user2.getRole().getRoleid();
                        switch (roleId) {
                            case 1:
                                response.sendRedirect("adminDashboard");
                                break;
                            case 2:
                                response.sendRedirect("director");
                                break;
                            case 3:
                                response.sendRedirect("warehouseStaffDashboard.jsp");
                                break;
                            case 4:
                                response.sendRedirect("staffDashboard");
                                break;
                            default:
                                response.sendRedirect("login.jsp");
                                break;
                        }
                        return;
                    } else {
                        request.setAttribute("mess", "Sai mật khẩu hoặc tài khoản.");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                        return;
                    }
                }
            }
        }

        request.setAttribute("mess", "Sai mật khẩu hoặc tài khoản.");
        request.getRequestDispatcher("login.jsp").forward(request, response);
        return;
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
