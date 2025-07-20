/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.RequestDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.sql.Timestamp;
import model.Category;
import model.Material;
import model.Request;
import model.User;
import model.UnitConversion;

/**
 *
 * @author kimoa
 */
@WebServlet(name = "SubmitRequestServlet", urlPatterns = {"/SubmitRequestServlet"})
public class SubmitRequestServlet extends HttpServlet {

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
            out.println("<title>Servlet SubmitRequestServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SubmitRequestServlet at " + request.getContextPath() + "</h1>");
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
        // Lấy user_id từ session và truy vấn UserDAO
        HttpSession session = request.getSession(false);
        User user = null;
        if (session != null && session.getAttribute("user_id") != null) {
            int userId = (int) session.getAttribute("user_id");
            UserDAO userDAO = new UserDAO();
            user = userDAO.getUserById(userId);
        }
        request.setAttribute("user", user);
        request.getRequestDispatcher("requestMaterial.jsp").forward(request, response);
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
        response.setContentType("text/html;charset=UTF-8");

        try {
            // Lấy user_id từ session và truy vấn UserDAO
            HttpSession session = request.getSession(false);
            User user = null;
            if (session != null && session.getAttribute("user_id") != null) {
                int userId = (int) session.getAttribute("user_id");
                UserDAO userDAO = new UserDAO();
                user = userDAO.getUserById(userId);
            }
            if (user == null) {
                request.setAttribute("error", "Vui lòng đăng nhập để gửi yêu cầu!");
                request.getRequestDispatcher("requestMaterial.jsp").forward(request, response);
                return;
            }

            // Get data from form
            String[] materialNames = request.getParameterValues("material_name[]");
            String[] quantities = request.getParameterValues("quantity[]");
            String[] materialConditions = request.getParameterValues("material_condition[]");
            String[] reasons = request.getParameterValues("reason[]");
            String[] materialTypes = request.getParameterValues("material_type[]");
            String[] units = request.getParameterValues("unit[]");
            int requestTypeId = Integer.parseInt(request.getParameter("request_type"));

            RequestDAO requestDAO = new RequestDAO();
            boolean allSaved = true;

            // Process each material request
            for (int i = 0; i < materialNames.length; i++) {
                // Create Material object
                Material material = new Material();
                material.setName(materialNames[i]);
                material.setMaterialId(0); // Will be set by database

                // Create Category object
                Category category = new Category();
                category.setCategoryId(Integer.parseInt(materialTypes[i]));

                // Create UnitConversion object
                UnitConversion unitConversion = new UnitConversion();
                unitConversion.setWarehouseunitid(Integer.parseInt(units[i]));

                // Create Request object
                Request requestObj = new Request();
                requestObj.setUserId(user.getUser_id());
                requestObj.setReason(reasons[i]);
                requestObj.setRequestStatus("Chờ duyệt");
                requestObj.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                
                // Save request to database
                boolean saved = requestDAO.createRequest(user.getUser_id(), reasons[i], "", "", "", "");
                if (!saved) {
                    allSaved = false;
                }
            }

            // Notify user
            if (allSaved) {
                request.setAttribute("message", "Đơn yêu cầu của bạn đã được gửi thành công!");
            } else {
                request.setAttribute("error", "Đã xảy ra lỗi khi gửi đơn yêu cầu.");
            }

            // Đặt lại user để hiển thị tên khi forward lại trang
            request.setAttribute("user", user);
            request.getRequestDispatcher("requestMaterial.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("requestMaterial.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Submit Request Servlet";
    }// </editor-fold>

}
