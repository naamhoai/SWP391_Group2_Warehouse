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
import model.*;

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

    private void Data(HttpServletRequest request) {
        UnitConversionDao dao = new UnitConversionDao();
        List<Category> cate = dao.getname();
        List<Material> loai = dao.getMaterial();
        request.setAttribute("cateList", cate);
        request.setAttribute("list", loai);
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
        Data(request);
        request.getRequestDispatcher("createUnit.jsp").forward(request, response);
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
        String note = request.getParameter("note");
        String materialids = request.getParameter("materialid");
        String baseunit = request.getParameter("baseunit");
        String unit1 = request.getParameter("unit1");
        String unit2 = request.getParameter("unit2");

        
        String mess = "";
        List<UnitConversion> listid = dao.getAll(1);
        try {

            int materialid = Integer.parseInt(materialids);
            if (!unit1.matches("\\d+") || !unit2.matches("\\d+")) {
                mess = "đơn vị nhập vào phải là số!";
                Data(request);
                request.setAttribute("messs", mess);
                request.getRequestDispatcher("createUnit.jsp").forward(request, response);
                return;
            }
            Double ratio1 = Double.parseDouble(unit1);
            Double ratio2 = Double.parseDouble(unit2);
            Double result = ratio1 * ratio2;
          
            String convertUnit = request.getParameter("convertedunit");
           

            Boolean validate = true;
            for (UnitConversion unitConversion : listid) {

                if (unitConversion.getMaterialid() == materialid) {
                    
                    mess = "Vật tư này đã tồn tại!";
                    request.setAttribute("messs", mess);
                    Data(request);
                    validate = false;
                    
                    break;
                }
            }

            if (result <= 0) {
                mess = "tỉ lệ chuyển đổi phải > 0";
                request.setAttribute("messs", mess);
                Data(request);
                validate = false;
                request.getRequestDispatcher("createUnit.jsp").forward(request, response);
                return;
            }
            if (validate) {
                dao.unitCreate2(baseunit, convertUnit, note, result, materialid);

                
                response.sendRedirect("unitConversionSeverlet");
                return;
            }

        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
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
