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
import java.util.List;
import model.Category;
import model.UnitConversion;

/**
 *
 * @author kien3
 */
@WebServlet(name = "unitEditseverlet", urlPatterns = {"/unitEditseverlet"})
public class UnitEditServlet extends HttpServlet {

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
            out.println("<title>Servlet unitEditseverlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet unitEditseverlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void data(HttpServletRequest request) {
        String baseunitid = request.getParameter("baseunitid");
        String materialid = request.getParameter("materialid");
        String materialname = request.getParameter("materialname");
    

        request.setAttribute("baseunitid", baseunitid);
        request.setAttribute("materialid", materialid);
        UnitConversionDao n = new UnitConversionDao();
        List<UnitConversion> list = n.getAll(1);
        List<UnitConversion> listconverted = n.getAllunitconverted();
        List<UnitConversion> listbase = n.getAllunitbase();
        List<Category> listcat = n.getAllpre();
        if (list != null) {
            request.setAttribute("list", list);
            request.setAttribute("listcat", listcat);
            request.setAttribute("listbase", listbase);
            request.setAttribute("listconverted", listconverted);
            request.setAttribute("baseunitid", baseunitid);
            request.setAttribute("materialid", materialid);
            request.setAttribute("materialname", materialname);

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
        data(request);
        request.getRequestDispatcher("editUnit.jsp").forward(request, response);
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
        String baseunitid = request.getParameter("baseunitid");
        String materialid = request.getParameter("materialid");

        
        String baseunit = request.getParameter("baseunit");
        String convertedunit = request.getParameter("convertedunit");
        String unit1 = request.getParameter("unit1");
        String unit2 = request.getParameter("unit2");
        String note = request.getParameter("note");
        String mess = "";

        
      

        try {
            int baseunitids = Integer.parseInt(baseunitid);
            int materialidids = Integer.parseInt(materialid);

            Double a = Double.parseDouble(unit1);
            Double b = Double.parseDouble(unit2);
            boolean validate = true;
            if (!unit1.matches("\\d+") || !unit2.matches("\\d+")) {
                mess = "đơn vị nhập vào phải là số!";
                validate = false;
                data(request);
                request.setAttribute("messss", mess);
                request.getRequestDispatcher("editUnit.jsp").forward(request, response);

            }

            double result = a * b;
            if (result <= 0) {
                mess = "Đơn vị chuyển đổi phải > 0!";
                validate = false;
                data(request);
                request.setAttribute("messss", mess);
                request.getRequestDispatcher("editUnit.jsp").forward(request, response);

            }
            if (validate) {
               
                
                
                String results = String.valueOf(result);
              
                dao.Update(materialidids, baseunit, convertedunit, results, note, baseunitids);
  
                response.sendRedirect("unitConversionSeverlet");
               

            }

        } catch (NumberFormatException e) {
            System.out.println(e);

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
