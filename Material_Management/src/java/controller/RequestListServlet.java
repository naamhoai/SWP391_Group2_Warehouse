package controller;

import dao.RequestDAO;
import dao.UserDAO;
import dao.RequestHistoryDAO;
import model.Request;
import model.User;
import model.RequestHistory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String sort = request.getParameter("sort") != null ? request.getParameter("sort") : "asc";
        String projectName = request.getParameter("projectName");

        if (startDate != null && !startDate.isEmpty() && !isValidDate(startDate)) {
            startDate = null;
        }
        if (endDate != null && !endDate.isEmpty() && !isValidDate(endDate)) {
            endDate = null;
        }

        try {
            Integer filterUserId = (roleId <= 2) ? null : userId;
            String sortBy = request.getParameter("sortBy");
            String order = request.getParameter("order");
            if (sortBy == null) {
                sortBy = "id";
            }
            if (order == null) {
                order = "asc";
            }
            List<Request> requests = requestDAO.getFilteredRequests(
                    userId, status, startDate, endDate, projectName, sortBy, order, page, pageSize
            );

            int totalRequests = requestDAO.countFilteredRequests(
                    filterUserId, status, startDate, endDate, projectName
            );

            int totalPages = (int) Math.ceil((double) totalRequests / pageSize);

            request.setAttribute("requests", requests);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            RequestHistoryDAO historyDAO = new RequestHistoryDAO();
            Map<Integer, List<RequestHistory>> requestHistoryMap = new HashMap<>();
            for (Request r : requests) {
                List<RequestHistory> historyList = historyDAO.getRequestHistoryByRequestId(r.getRequestId());
                requestHistoryMap.put(r.getRequestId(), historyList);
            }
            request.setAttribute("requestHistoryMap", requestHistoryMap);

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

    private boolean isValidDate(String dateStr) {
        try {
            java.time.LocalDate.parse(dateStr);
            return true;
        } catch (java.time.format.DateTimeParseException e) {
            return false;
        }
    }
}
