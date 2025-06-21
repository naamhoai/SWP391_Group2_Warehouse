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
        List<User> list = dao.userAccount();
        Boolean vali = false;

        if (list != null && !list.isEmpty()) {
            for (User user2 : list) {

            
                if (gmail.equals(user2.getEmail())) {
                  
                    if (user2.getPassword() != null && !user2.getPassword().startsWith("$2a$")) { 
                        if (pass.equals(user2.getPassword())) {

                         
                            session.setAttribute("Admin", user2);
                            session.setAttribute("userId", user2.getUser_id());
                            session.setAttribute("roleId", user2.getRole().getRoleid());

                            vali = true;

                       
                            switch (user2.getRole().getRoleid()) {
                                case 1:
                                    response.sendRedirect("adminDashboard.jsp");
                                    break;
                                case 2:
                                    {
                                        PrintWriter out = response.getWriter();
                                        out.print("day la so 2");
                                        break;
                                    }
                                case 3:
                                    {
                                        PrintWriter out = response.getWriter();
                                        out.print("day la so 3");
                                        break;
                                    }
                                case 4:
                                    {
                                        PrintWriter out = response.getWriter();
                                        out.print("day la so 4");
                                        break;
                                    }
                                default:
                                    break;
                            }
                            break;
                        }
                    } else {
                     
                        if (BCrypt.checkpw(pass, user2.getPassword())) {
                        
                            if (user2.getStatus() != null && user2.getStatus().equals("active")) {
                                session.setAttribute("Admin", user2);
                                session.setAttribute("userId", user2.getUser_id());
                                session.setAttribute("roleId", user2.getRole());

                                vali = true;

                                // Check if there's a redirect URL stored in session
                                String redirectURL = (String) session.getAttribute("redirectURL");
                                if (redirectURL != null) {
                                    session.removeAttribute("redirectURL"); // Clear the stored URL
                                    response.sendRedirect(request.getContextPath() + redirectURL);
                                } else {
                                    // Default redirect based on role
                                    if (user2.getRole().getRoleid() == 1) {
                                        response.sendRedirect("adminDashboard.jsp");
                                    } else if (user2.getRole().getRoleid() == 2) {
                                        PrintWriter out = response.getWriter();
                                        out.print("day la so 2");
                                    } else if (user2.getRole().getRoleid() == 3) {
                                        response.sendRedirect("requestMaterial.jsp");
                                    } else if (user2.getRole().getRoleid() == 4) {
                                        PrintWriter out = response.getWriter();
                                        out.print("day la so 4");
                                    }
                                }
                                break;
                            } else {
                                String mess = "Account Invalid!";
                                request.setAttribute("mess", mess);
                                request.getRequestDispatcher("login.jsp").forward(request, response);
                            }
                        }
                    }
                }
            }
        }


        if (!vali) {
            String mess = "Incorrect account or password";
            request.setAttribute("mess", mess);
            request.getRequestDispatcher("login.jsp").forward(request, response);
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
