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

/**
 *
 * @author kien3
 */
@WebServlet(name = "detailuser", urlPatterns = {"/detailuser"})
public class DetailUser extends HttpServlet {

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
            out.println("<title>Servlet DetailUser</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DetailUser at " + request.getContextPath() + "</h1>");
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
        DAO dao = new DAO();
        String userid = request.getParameter("userid");
        String roleid = request.getParameter("roleid");
        List<Role> list = dao.getRoles();

        try {
            if (list != null && !list.isEmpty() && userid != null && roleid != null) {

                int id = Integer.parseInt(userid);
                int rid = Integer.parseInt(roleid);
                User usid = dao.userID(id, rid);
                System.out.println(usid);
                request.setAttribute("user", usid);
                request.setAttribute("lits", list);

                request.getRequestDispatcher("detailuser.jsp").forward(request, response);
            }
        } catch (IOException e) {
            System.out.println(e);
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
        HttpSession se = request.getSession();
        String name = request.getParameter("name");
        String valu = request.getParameter("valu");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        String role = request.getParameter("role");
        String description = request.getParameter("description");
        String userid = request.getParameter("userid");

        DAO dao = new DAO();
        String mess = "";

        try {

            int id = Integer.parseInt(userid);
            int rid = Integer.parseInt(role);
            int pri = Integer.parseInt(priority);
            if (pri > 0) {
                dao.userUpdate(name, pri, status, rid, id);
                mess = "Successfully updated";
                se.setAttribute("messUpdate", mess);
                response.sendRedirect("settinglist");
            } else {
                mess = "Priority Invalied!";
                User usid = dao.userID(id, rid);
                System.out.println(usid);
                request.setAttribute("user", usid);
                request.setAttribute("messkk", mess);
                request.getRequestDispatcher("detailuser.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            mess = "Failed to update.";
            se.setAttribute("messkk", mess);
            response.sendRedirect("detailuser");

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
