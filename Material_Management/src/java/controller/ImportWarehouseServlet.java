/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UnitConversionDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.*;

/**
 *
 * @author kien3
 */
@WebServlet(name = "ImportWarehouseServlet", urlPatterns = {"/ImportWarehouseServlet"})
public class ImportWarehouseServlet extends HttpServlet {

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
            out.println("<title>Servlet ImportWarehouseServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ImportWarehouseServlet at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        User nameandid = (User) session.getAttribute("Admin");
        String username = (nameandid != null) ? nameandid.getFullname() : "Rỗng";
        String role = (nameandid != null && nameandid.getRole() != null) ? nameandid.getRole().getRolename() : "Rỗng";
        UnitConversionDao dao = new UnitConversionDao();
        List<Material> list = dao.getALls();
        request.setAttribute("materialList", list);

        request.setAttribute("username", username);
        request.getRequestDispatcher("importWarehouse.jsp").forward(request, response);
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
        UnitConversionDao dao = new UnitConversionDao();
        String name = request.getParameter("name");
        String namevt = request.getParameter("namevt");
        String number = request.getParameter("number");
        String unit = request.getParameter("unit");
        String status = request.getParameter("status");
        String materialId = request.getParameter("materialId");
        HttpSession session = request.getSession();
        User nameandid = (User) session.getAttribute("Admin");
        int materi = 0;
        if (namevt != null && namevt.contains("-")) {
            String[] parts = namevt.split("-");
            materi = Integer.parseInt(parts[0].trim());
        } else if (namevt != null) {
            materi = Integer.parseInt(namevt.trim());
        }

        String username = (nameandid != null) ? nameandid.getFullname() : "Rỗng";
        String role = (nameandid != null && nameandid.getRole() != null) ? nameandid.getRole().getRolename() : "Rỗng";

        int num = Integer.parseInt(number);
        boolean success = dao.updateQuantity(materi, status, num);

        if (success) {
            request.setAttribute("mess", "Nhập đơn vị thành công!");
            List<Material> list = dao.getALls();
            request.setAttribute("materialList", list);
            request.setAttribute("username", username);
            
            
            request.getRequestDispatcher("importWarehouse.jsp").forward(request, response);

        } else {
            response.sendRedirect("error.jsp");
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
