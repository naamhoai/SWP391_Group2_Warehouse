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
import java.sql.SQLException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.*;
import java.sql.Timestamp;

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
        UnitChangeHistory history = new UnitChangeHistory();
        String unitName = request.getParameter("unitName").trim();
        String unitDesc = request.getParameter("unitDesc");
        String baseUnitID = request.getParameter("baseUnitID");
        String ratio = request.getParameter("ratio");

        HttpSession session = request.getSession();
        User nameandid = (User) session.getAttribute("Admin");  
        String username = (nameandid != null) ? nameandid.getFullname() : "Rỗng";
        String role = (nameandid != null && nameandid.getRole() != null) ? nameandid.getRole().getRolename() : "Rỗng";
        int base = Integer.parseInt(baseUnitID);
        int unitIdFromName = dao.getUnitIdByName(unitName);
        int supplierUnitId;
        int validate = dao.isUnitexit(unitIdFromName, base);
        if (validate == 1) {
            request.setAttribute("mess", "Cặp đơn vị này đã tồn tại!. vui lòng tạo cặp đơn vị khác.");
            request.getRequestDispatcher("createUnit.jsp").forward(request, response);
            return;
        }

        if (unitIdFromName != 0) {
            supplierUnitId = unitIdFromName;
        } else {
            supplierUnitId = dao.unitDesc(unitName);
        }

        try {
            if (supplierUnitId != -1) {
                dao.insertUnitConversion(supplierUnitId, base, ratio, unitDesc, unitIdFromName);
               
                String unitBaseName = dao.getUnitNameById(base);
                String supplierUnitName = dao.getUnitNameById(supplierUnitId);

                
                history.setUnitId(supplierUnitId);
                history.setUnitName(supplierUnitName);
                history.setActionType("Thêm mới");
                history.setOldValue("null");
                history.setNewValue(ratio);
                history.setChangedBy(username);
                history.setRole(role);
                history.setNote("Quy đổi sang " + unitBaseName + " với tỉ lệ " + ratio);
                history.setChangedAt(new Timestamp(System.currentTimeMillis()));

                dao.insertHistory(history);
                request.setAttribute("mess", "Thêm đơn vị mới thành công");
                response.sendRedirect("unitConversionSeverlet");
                return;
            } else {
                request.setAttribute("mess", "Thêm đơn vị thất bại");
                request.getRequestDispatcher("createUnit.jsp").forward(request, response);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(CreateUnitServlet.class.getName()).log(Level.SEVERE, null, ex);
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
