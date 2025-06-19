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
import java.util.List;
import model.Request;
import model.User;

/**
 *
 * @author kimoa
 */
@WebServlet(name = "RequestListServlet", urlPatterns = {"/RequestListServlet"})
public class RequestListServlet extends HttpServlet {

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
            out.println("<title>Servlet RequestListServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RequestListServlet at " + request.getContextPath() + "</h1>");
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
    private RequestDAO requestDAO = new RequestDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy thông tin user từ database
        User user = userDAO.getUserById(userId);
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy tham số phân trang và tìm kiếm
        int page = 1;
        int pageSize = 5;
        
        try {
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            if (request.getParameter("pageSize") != null) {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
            }
        } catch (NumberFormatException e) {
            // Giữ giá trị mặc định nếu có lỗi
        }

        // Lấy và decode các tham số
        String status = request.getParameter("status");
        String date = request.getParameter("date");
        String sort = request.getParameter("sort");
        String requestType = request.getParameter("requestType");
        String search = request.getParameter("search");

        if (sort == null || (!sort.equals("asc") && !sort.equals("desc"))) {
            sort = "asc";
        }

        // Lấy danh sách request tùy theo vai trò
        List<Request> requests = null;
        int totalRequests = 0;
        try {
            boolean isDirector = user.getRole() != null && user.getRole().getRoleid() == 2;

            // Nếu có từ khóa tìm kiếm
            if (search != null && !search.trim().isEmpty()) {
                if (isDirector) {
                    // Giám đốc tìm kiếm trong tất cả requests
                    requests = requestDAO.searchRequests(search, page, pageSize);
                    totalRequests = requestDAO.getTotalSearchResults(search);
                } else {
                    // User khác chỉ tìm kiếm trong requests của họ
                    requests = requestDAO.searchUserRequests(userId, search, page, pageSize);
                    totalRequests = requestDAO.getTotalUserSearchResults(userId, search);
                }
            } 
            // Nếu có bộ lọc
            else if (status != null || date != null || requestType != null) {
                if (isDirector) {
                    // Giám đốc lọc trong tất cả requests
                    requests = requestDAO.filterRequests(status, date, sort, requestType, page, pageSize);
                    totalRequests = requestDAO.getTotalFilteredRequests(status, date, requestType);
                } else {
                    // User khác chỉ lọc trong requests của họ
                    requests = requestDAO.filterUserRequests(userId, status, date, sort, requestType, page, pageSize);
                    totalRequests = requestDAO.getTotalFilteredUserRequests(userId, status, date, requestType);
                }
            }
            // Nếu không có tìm kiếm hay lọc
            else {
                if (isDirector) {
                    // Giám đốc xem tất cả requests
                    requests = requestDAO.getAllRequests(sort, page, pageSize);
                    totalRequests = requestDAO.getTotalRequests();
                } else {
                    // User khác chỉ xem requests của họ
                    requests = requestDAO.getRequestsByUserId(userId, sort, page, pageSize);
                    totalRequests = requestDAO.getTotalRequestsByUserId(userId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            requests = new java.util.ArrayList<>();
            totalRequests = 0;
        }

        // Tính toán thông tin phân trang
        int totalPages = (int) Math.ceil((double) totalRequests / pageSize);
        
        // Đặt các thuộc tính cho JSP
        request.setAttribute("requests", requests);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRequests", totalRequests);
        
        request.getRequestDispatcher("requestList.jsp").forward(request, response);
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
        doGet(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Request List Servlet";
    }// </editor-fold>

}
