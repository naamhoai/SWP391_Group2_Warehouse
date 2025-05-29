package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import model.User;
import dao.RoleDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author kimoa
 */
@WebServlet(urlPatterns = {"/UserDetailServlet"})
public class UserDetailServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();
    private RoleDAO roleDAO = new RoleDAO();

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer sessionUserId = (Integer) session.getAttribute("userId");
        Integer sessionRoleId = (Integer) session.getAttribute("roleId");

        if (sessionUserId == null || sessionRoleId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userIdToView;

        if (sessionRoleId == 1) {
            // admin có thể xem info user khác nếu truyền param userId
            String paramUserId = request.getParameter("userId");
            if (paramUserId != null) {
                try {
                    userIdToView = Integer.parseInt(paramUserId);
                } catch (NumberFormatException e) {
                    showErrorPage(response, "Invalid userId parameter");
                    return;
                }
            } else {
                // nếu không truyền param thì xem info chính admin
                userIdToView = sessionUserId;
            }
        } else {
            // user thường chỉ xem info của chính mình
            userIdToView = sessionUserId;
        }

        System.out.println("DEBUG: userIdToView = " + userIdToView);

        User user = userDAO.getUserById(userIdToView);

        String roleName = null;
        if (user.getRole_id() != 0) {
            roleName = userDAO.getRoleNameById(user.getRole_id());
        }

        request.setAttribute("user", user);
        request.setAttribute("roleName", roleName);

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
    private void showErrorPage(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>Error</title></head><body>");
            out.println("<h2 style='color:red;'>Lỗi: " + message + "</h2>");
            out.println("<a href='login.jsp'>Quay lại đăng nhập</a>");
            out.println("</body></html>");
        }
    }

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
