/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import model.*;

import dao.*;
import dao.DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author kien3
 */
@WebServlet(name = "settinglist", urlPatterns = {"/settinglist"})
public class SettingList extends HttpServlet {

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
            out.println("<title>Servlet SettingList</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SettingList at " + request.getContextPath() + "</h1>");
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
        DAO con = new DAO();
        
        List<User> list = con.SettingList();
        List<Role> role = con.getRoles();
        if (list != null && !list.isEmpty() || role != null && !role.isEmpty()) {
            request.setAttribute("listrole", role);
            request.setAttribute("list", list);

        } else {
            request.setAttribute("mess", "No data");
        }

        request.getRequestDispatcher("settinglist.jsp").forward(request, response);
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

        String status = request.getParameter("status");
        String type = request.getParameter("type");
        String priority = request.getParameter("priority");
        String searchname = request.getParameter("searchname");
        String sortby = request.getParameter("sortBy");
        String sumbit = request.getParameter("save");
        DAO dao = null;

        try {
            dao = new DAO();
            List<Role> role = dao.getRoles();
            request.setAttribute("listrole", role);
            Integer pri = null;

            if (priority != null && !priority.trim().isEmpty()) {
                try {
                    pri = Integer.parseInt(priority.trim());
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi định dạng số cho priority: " + e.getMessage());
                    request.setAttribute("mess", "Priority phải là một số hợp lệ.");
                }
            }
            List<User> list = null;
            if ("filter".equalsIgnoreCase(sumbit)) {
                list = dao.getFilter(status, type, (pri == null ? -1 : pri), searchname);
                System.out.println(list);
            }
            if ("sort".equalsIgnoreCase(sumbit)) {
                list = dao.getSort(sortby);
                System.out.println(list);
            }

            if (list != null && !list.isEmpty()) {
                request.setAttribute("list", list);
            } else {
                request.setAttribute("mess", "No data");
            }

        } finally {
            if (dao != null) {
                dao.close();
            }
        }

        request.getRequestDispatcher("settinglist.jsp").forward(request, response);
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
