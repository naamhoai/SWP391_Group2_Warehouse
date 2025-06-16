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
import java.util.ArrayList;
import java.util.List;
import model.*;

/**
 *
 * @author kien3
 */
@WebServlet(name = "unitConversionSeverlet", urlPatterns = {"/unitConversionSeverlet"})
public class UnitConversionServlet extends HttpServlet {

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
            out.println("<title>Servlet unitConversionSeverlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet unitConversionSeverlet at " + request.getContextPath() + "</h1>");
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
        UnitConversionDao n = new UnitConversionDao();
        List<UnitConversion> list = n.getAll();
        List<UnitConversion> listconverted = n.getAllunitconverted();
        List<UnitConversion> listbase = n.getAllunitbase();
        List<Category> listcat = n.getAllpre();
        if (list != null) {
            request.setAttribute("list", list);
            request.setAttribute("listcat", listcat);
          request.setAttribute("listbase", listbase);
        request.setAttribute("listconverted", listconverted);

        }

        request.getRequestDispatcher("unitManagements.jsp").forward(request, response);
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
        String baseunit = request.getParameter("baseunit");
        String convertedunit = request.getParameter("convertedunit");
        String search = request.getParameter("search");
        
        
        UnitConversionDao n = new UnitConversionDao();

        List<Category> listcat = n.getAllpre();
        List<UnitConversion> listconverted = n.getAllunitconverted();
        List<UnitConversion> listbase = n.getAllunitbase();
        List<UnitConversion> list = n.getAll();
        List<UnitConversion> newlist = n.getFilter(baseunit, convertedunit, search);
        System.out.println("Base unit: " + baseunit);
        System.out.println("Device (parent): " + convertedunit);
        System.out.println("Material name: " + search);

        request.setAttribute("list", list);
        request.setAttribute("listcat", listcat);
        request.setAttribute("listbase", listbase);
        request.setAttribute("listconverted", listconverted);
        
        request.setAttribute("list", newlist);
        request.getRequestDispatcher("unitManagements.jsp").forward(request, response);
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
