package controller;

import dao.RequestDAO;
import dao.UserDAO;
import model.Request;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "RequestListServlet", urlPatterns = {"/RequestListServlet"})
public class RequestListServlet extends HttpServlet {

    private RequestDAO requestDAO = new RequestDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        Integer roleId = (Integer) session.getAttribute("roleId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int page = 1;
        int pageSize = 5;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        String status = request.getParameter("status");
        String requestType = request.getParameter("requestType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String sort = request.getParameter("sort") != null ? request.getParameter("sort") : "desc";

        if (status != null && !status.isEmpty() && !isValidStatus(status)) {
            status = null;
        }
        if (startDate != null && !startDate.isEmpty() && !isValidDate(startDate)) {
            startDate = null;
        }
        if (endDate != null && !endDate.isEmpty() && !isValidDate(endDate)) {
            endDate = null;
        }

        try {
            Integer filterUserId = (roleId <= 2) ? null : userId;

            List<Request> requests = requestDAO.getFilteredRequests(
                filterUserId, status, requestType, startDate, endDate, sort, page, pageSize
            );

            int totalRequests = requestDAO.countFilteredRequests(
                filterUserId, status, requestType, startDate, endDate
            );

            int totalPages = (int) Math.ceil((double) totalRequests / pageSize);

            request.setAttribute("requests", requests);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            request.getRequestDispatcher("requestList.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách yêu cầu.");
            request.getRequestDispatcher("requestList.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Request List Servlet";
    }

    private boolean isValidStatus(String status) {
        List<String> validStatuses = List.of("Pending", "Approved", "Rejected");
        return validStatuses.contains(status);
    }

    private boolean isValidDate(String dateStr) {
        try {
            java.time.LocalDate.parse(dateStr);
            return true;
        } catch (java.time.format.DateTimeParseException e) {
            return false;
        }
    }
}
