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






/**
*
* @author kien3
*/
@WebServlet(name = "CreateUnitServlet", urlPatterns = {"/CreateUnitServlet"})
public class CreateUnitServlet extends HttpServlet {

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
           out.println("<title>Servlet CreateUnitServlet</title>");
           out.println("</head>");
           out.println("<body>");
           out.println("<h1>Servlet CreateUnitServlet at " + request.getContextPath() + "</h1>");
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
       String unitName = request.getParameter("unitName");
       String status = request.getParameter("status");
       if (unitName == null || unitName.trim().isEmpty()) {
           request.setAttribute("mess", "Tên đơn vị không được để trống!");
           request.getRequestDispatcher("createUnit.jsp").forward(request, response);
           return;
       }
     
       int existedId = dao.getUnitIdByName(unitName.trim());
       if (existedId > 0) {
           request.setAttribute("mess", "Tên đơn vị đã tồn tại!");
           request.getRequestDispatcher("createUnit.jsp").forward(request, response);
           return;
       }
       try {
           dao.addUnitWithStatus(unitName.trim(), status);
           response.sendRedirect("unitConversionSeverlet");
       } catch (Exception e) {
           request.setAttribute("mess", "Thêm đơn vị thất bại: " + e.getMessage());
           request.getRequestDispatcher("createUnit.jsp").forward(request, response);
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
