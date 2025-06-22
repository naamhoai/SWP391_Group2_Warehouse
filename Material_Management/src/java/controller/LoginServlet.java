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

            for (User user : users) {
                if (gmail.equals(user.getEmail())) {
                    if (!"active".equalsIgnoreCase(user.getStatus())) {
                        request.setAttribute("mess", "Tài khoản đã bị khóa.");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                        return;
                    }

                    if (BCrypt.checkpw(pass, user.getPassword())) {
                        session.setAttribute("Admin", user);
                        session.setAttribute("userId", user.getUser_id());
                        session.setAttribute("roleId", user.getRole());
                        String redirectURL = (String) session.getAttribute("redirectURL");
                        if (redirectURL != null) {
                            session.removeAttribute("redirectURL");
                            response.sendRedirect(request.getContextPath() + redirectURL);
                            return;
                        }
                        int roleId = user.getRole().getRoleid();
                        switch (roleId) {
                            case 1:
                                response.sendRedirect("adminDashboard.jsp");
                                break;
                            case 2:
                                response.getWriter().print("day la so 2");
                                break;
                            case 3:
                                response.sendRedirect("requestMaterial.jsp");
                                break;
                            case 4:
                                response.getWriter().print("day la so 4");
                                break;
                            default:
                                response.sendRedirect("login.jsp");
                                break;
                        }
                        return;

                    } else {

                        request.setAttribute("mess", "Sai mật khẩu.");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                        return;

                    }
                }
            }
        }

        request.setAttribute("mess", " Sai thông tin tài khoản.");
        request.getRequestDispatcher("login.jsp").forward(request, response);
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
