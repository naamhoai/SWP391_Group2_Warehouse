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
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

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
        String username = (nameandid != null) ? nameandid.getFullname() : "";
        String role = (nameandid != null && nameandid.getRole() != null) ? nameandid.getRole().getRolename() : "";
        System.out.println("usernaem" + username);
        UnitConversionDao dao = new UnitConversionDao();
        List<Material> list = dao.getALls();
        if (list != null) {
System.out.println("materialList: " + request.getAttribute("materialList"));
            System.out.println("username: " + request.getAttribute("username"));
            System.out.println("role: " + request.getAttribute("role"));

            request.setAttribute("materialList", list);
            request.setAttribute("role", role);
            request.setAttribute("username", username);

        }

        Map<String, String> materialUnitMap = new HashMap<>();
        for (Material m : list) {
            materialUnitMap.put(m.getMaterialId() + "-" + m.getName(), m.getUnitName());
        }
        String materialUnitMapJson = new Gson().toJson(materialUnitMap);
        request.setAttribute("materialUnitMapJson", materialUnitMapJson);

        request.getRequestDispatcher("importWarehouse.jsp").forward(request, response);
        return;
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
        request.setCharacterEncoding("UTF-8");
        UnitConversionDao dao = new UnitConversionDao();

        String[] namevts = request.getParameterValues("namevt[]");
        String[] numbers = request.getParameterValues("number[]");
        String[] units = request.getParameterValues("unit[]");
        String[] statuses = request.getParameterValues("status[]");

        String reason = request.getParameter("reason");
        String nguoiGiao = request.getParameter("nguoiGiao");
        String nguoiNhan = request.getParameter("nguoiNhan");
        String soDienThoaiNguoiGiao = request.getParameter("soDienThoaiNguoiGiao");
        String tenDuAn = request.getParameter("tenDuAn");

        HttpSession session = request.getSession();
        User nameandid = (User) session.getAttribute("Admin");
        String username = (nameandid != null) ? nameandid.getFullname() : "";
        String role = (nameandid != null && nameandid.getRole() != null) ? nameandid.getRole().getRolename() : "";

        boolean allSuccess = true;

        for (int i = 0; i < namevts.length; i++) {
            String namevt = namevts[i];
            String number = numbers[i];
            String unit = units[i];
            String status = statuses[i];

            int materi = 0;
            if (namevt != null && namevt.contains("-")) {
                String[] parts = namevt.split("-");
                materi = Integer.parseInt(parts[0].trim());
            } else if (namevt != null) {
                materi = Integer.parseInt(namevt.trim());
            }

            int num = Integer.parseInt(number);
            boolean success = dao.updateQuantity(materi, status, num);

            if (success) {
            
                ImportHistory importHistory = new model.ImportHistory(
                        role,
                        reason,
                        nguoiGiao,
                        nguoiNhan,
                        soDienThoaiNguoiGiao,
                        tenDuAn,
                        namevt,
                        num,
unit,
                        status
                );
                new dao.ImportHistoryDAO().insertImportHistory(importHistory);
            } else {
                allSuccess = false;
                break; 
            }
        }

        if (allSuccess) {
            request.setAttribute("mess", "Nhập đơn vị thành công!");
            List<Material> list = dao.getALls();
            request.setAttribute("materialList", list);
            request.setAttribute("username", username);
            request.setAttribute("role", role);
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