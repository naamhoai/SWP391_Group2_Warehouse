/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

import dao.UnitConversionDao;

import java.util.List;


/**
 *
 * @author kien3
 */
@WebServlet(name = "UnitChangeHistoryServlet", urlPatterns = {"/UnitChangeHistoryServlet"})
public class UnitChangeHistoryServlet extends HttpServlet {

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
            out.println("<title>Servlet UnitChangeHistoryServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UnitChangeHistoryServlet at " + request.getContextPath() + "</h1>");
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
        UnitConversionDao dao = new UnitConversionDao();

        String search = request.getParameter("search");
        String operationFilter = request.getParameter("operationFilter");
        String userFilter = request.getParameter("userFilter");
        String dateFilter = request.getParameter("date");

        int page = 1;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
        } catch (Exception ignored) {
        }
         
        try {
            System.out.println("date" + dateFilter);
            List<UnitChangeHistory> historyList = dao.getFilHistory(search, operationFilter, userFilter, dateFilter, page);
            int totalPages = dao.countpage(search, operationFilter, userFilter, dateFilter);

            request.setAttribute("historyList", historyList);
            request.setAttribute("search", search);
            request.setAttribute("operationFilter", operationFilter);
            request.setAttribute("userFilter", userFilter);
            request.setAttribute("dateFilter", dateFilter);
            request.setAttribute("currentPage", page);
            request.setAttribute("pages", totalPages);

            request.getRequestDispatcher("unitHistory.jsp").forward(request, response);

        } catch (IOException e){
           
          
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
        UnitConversionDao dao = new UnitConversionDao();

        String search = request.getParameter("search");
        String operationFilter = request.getParameter("operationFilter");
        String userFilter = request.getParameter("userFilter");
        String date = request.getParameter("date");

        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception ignored) {
        }

        try {
            
            List<UnitChangeHistory> historyList = dao.getFilHistory(search, operationFilter, userFilter, date, page);
            
            int totalPages = dao.countpage(search, operationFilter, userFilter, date);

            request.setAttribute("historyList", historyList);
            request.setAttribute("search", search);
            request.setAttribute("operationFilter", operationFilter);
            request.setAttribute("userFilter", userFilter);
            request.setAttribute("date", date);
            request.setAttribute("currentPage", page);
            request.setAttribute("pages", totalPages);

            request.getRequestDispatcher("unitHistory.jsp").forward(request, response);

        } catch (IOException ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi xử lý dữ liệu");
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
