package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import dao.DAO;
import model.User;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author kimoa
 */
@WebServlet(urlPatterns = {"/UserDetailServlet"})
public class UserDetailServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();
    private DAO dao = new DAO();

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
            out.println("<title>Servlet UserDetailServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserDetailServlet at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("search");
        String pageParam = request.getParameter("page");
        int page = 1; // trang hiện tại mặc định
        int pageSize = 10; // số bản ghi trên mỗi trang

        try {
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1; // tránh trang âm
                }
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        List<User> fullList;
        List<User> userList;
        int totalRecords;

        if (keyword == null || keyword.trim().isEmpty()) {
            fullList = userDAO.getUserListSummary(); // lấy tất cả user
        } else {
            keyword = keyword.trim();
            fullList = userDAO.searchUsersByKeyword(keyword); // lấy tất cả user theo keyword (ko phân trang)
        }
        totalRecords = fullList.size();

        // Sắp xếp toàn bộ danh sách theo user_id trước
        fullList.sort(Comparator.comparingInt(User::getUser_id));

        // Tính toán cắt danh sách cho phân trang
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalRecords);

        if (fromIndex >= totalRecords) {
            userList = Collections.emptyList(); // không có dữ liệu trang này
        } else {
            userList = fullList.subList(fromIndex, toIndex);
        }

        int pages = (int) Math.ceil((double) totalRecords / pageSize);

        // Đẩy dữ liệu xuống JSP
        request.setAttribute("userList", userList);
        request.setAttribute("pages", pages);
        request.setAttribute("currentPage", page);
        request.setAttribute("searchKeyword", keyword); // để giữ giá trị tìm kiếm trên UI nếu có
        request.getRequestDispatcher("userdetail.jsp").forward(request, response);
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
        processRequest(request, response);
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
