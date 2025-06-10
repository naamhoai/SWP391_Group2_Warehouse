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
import model.PurchaseOrder;
import model.PurchaseOrderDetail;

/**
 *
 * @author kimoa
 */
@WebServlet(name="CreatePurchaseOrderServlet", urlPatterns={"/CreatePurchaseOrderServlet"})
public class CreatePurchaseOrderServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet CreatePurchaseOrderServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreatePurchaseOrderServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int supplierId = Integer.parseInt(request.getParameter("supplierId"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
        int materialId = Integer.parseInt(request.getParameter("materialId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        double unitPrice = Double.parseDouble(request.getParameter("unitPrice"));

        // Tạo đơn mua
        PurchaseOrder purchaseOrder = new PurchaseOrder(supplierId, userId, totalAmount);
        
        // Lưu đơn mua vào cơ sở dữ liệu
        // Giả sử có phương thức saveOrder() để lưu vào DB
        // orderDAO.saveOrder(purchaseOrder);

        // Tạo chi tiết đơn hàng
        
        // Lưu chi tiết đơn hàng vào cơ sở dữ liệu
        // orderDetailDAO.saveDetail(purchaseOrderDetail);

        // Redirect tới trang xác nhận
        response.sendRedirect("order_created.jsp");
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
