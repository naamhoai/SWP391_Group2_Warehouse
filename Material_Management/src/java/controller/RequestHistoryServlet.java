package controller;

import dao.RequestHistoryDAO;
import dao.RequestDAO;
import dao.UserDAO;
import model.RequestHistory;
import model.Request;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.sql.SQLException;
import java.util.Comparator;

@WebServlet(name = "RequestHistoryServlet", urlPatterns = {"/requestHistory"})
public class RequestHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestHistoryDAO historyDAO = new RequestHistoryDAO();
        RequestDAO requestDAO = new RequestDAO();
        UserDAO userDAO = new UserDAO();

        HttpSession session = request.getSession();
        Integer currentUserId = (Integer) session.getAttribute("userId");
        Integer currentRoleId = (Integer) session.getAttribute("roleId");

        if (currentUserId == null) {
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
        
        String requestIdParam = request.getParameter("requestId");
        List<RequestHistory> historyList;
        
        if (requestIdParam != null && !requestIdParam.isEmpty()) {
            int requestId = Integer.parseInt(requestIdParam);
            Request requestObj = null;
            try {
                requestObj = requestDAO.getRequestById(requestId);
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("requestHistory");
                return;
            }
            if (requestObj == null || (currentRoleId > 2 && requestObj.getUserId() != currentUserId)) {
                response.sendRedirect("requestHistory");
                return;
            }
            historyList = historyDAO.getRequestHistoryByRequestId(requestId);
        } else {
            if (currentRoleId <= 2) {
                historyList = historyDAO.getAllRequestHistory();
            } else {
                historyList = historyDAO.getRequestHistoryByUserId(currentUserId);
            }
        }

        Map<Integer, String> userNameMap = new HashMap<>();
        Map<Integer, String> userRoleMap = new HashMap<>();
        Map<Integer, String> lastDirectorNoteMap = new HashMap<>();

        for (RequestHistory h : historyList) {
            if (!userNameMap.containsKey(h.getChangedBy())) {
                try {
                    User u = userDAO.getUserById(h.getChangedBy());
                    userNameMap.put(h.getChangedBy(), u != null ? u.getFullname() : "");
                    userRoleMap.put(h.getChangedBy(), (u != null && u.getRole() != null) ? u.getRole().getRolename() : "");
                } catch (Exception e) {
                    e.printStackTrace();
                    userNameMap.put(h.getChangedBy(), "");
                    userRoleMap.put(h.getChangedBy(), "");
                }
            }
            
            if (h.getDirectorNote() != null && !h.getDirectorNote().isEmpty()) {
                lastDirectorNoteMap.put(h.getRequestId(), h.getDirectorNote());
            }
        }

        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");
        if (sortBy == null) {
            sortBy = "id";
        }
        if (order == null) {
            order = "asc";
        }
        Comparator<RequestHistory> comparator;
        switch (sortBy) {
            case "name":
                comparator = Comparator.comparing(RequestHistory::getRecipientName, Comparator.nullsFirst(String::compareToIgnoreCase));
                break;
            case "date":
                comparator = Comparator.comparing(RequestHistory::getChangeTime, Comparator.nullsFirst(Comparator.naturalOrder()));
                break;
            default:
                comparator = Comparator.comparing(RequestHistory::getRequestId);
        }
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        historyList.sort(comparator);

        String status = request.getParameter("status");
        String projectName = request.getParameter("projectName");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        if ((status != null && !status.isEmpty())
                || (projectName != null && !projectName.isEmpty())
                || (startDate != null && !startDate.isEmpty())
                || (endDate != null && !endDate.isEmpty())) {
            Iterator<RequestHistory> it = historyList.iterator();
            while (it.hasNext()) {
                RequestHistory h = it.next();
                if (status != null && !status.isEmpty() && (h.getNewStatus() == null || !h.getNewStatus().equals(status))) {
                    it.remove();
                    continue;
                }
                
                if (projectName != null && !projectName.isEmpty()) {
                    String recName = h.getRecipientName();
                    if (recName == null || !recName.toLowerCase().contains(projectName.toLowerCase())) {
                        it.remove();
                        continue;
                    }
                }
                
                if (startDate != null && !startDate.isEmpty() && h.getChangeTime() != null) {
                    java.sql.Date filterStart = java.sql.Date.valueOf(startDate);
                    if (h.getChangeTime().before(filterStart)) {
                        it.remove();
                        continue;
                    }
                }
                
                if (endDate != null && !endDate.isEmpty() && h.getChangeTime() != null) {
                    java.sql.Date filterEnd = java.sql.Date.valueOf(endDate);
                    java.sql.Timestamp endOfDay = new java.sql.Timestamp(filterEnd.getTime() + 24 * 60 * 60 * 1000 - 1);
                    if (h.getChangeTime().after(endOfDay)) {
                        it.remove();
                    }
                }
            }
        }

        int totalRecords = historyList.size();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (page < 1) {
            page = 1;
        }
        if (page > totalPages) {
            page = totalPages;
        }
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalRecords);
        List<RequestHistory> pagedHistoryList = new ArrayList<>();
        boolean noRecords = false;
        if (totalRecords == 0) {
            noRecords = true;
        } else if (fromIndex < toIndex && fromIndex < totalRecords && fromIndex >= 0) {
            pagedHistoryList = historyList.subList(fromIndex, toIndex);
        }
        request.setAttribute("historyList", pagedHistoryList);
        request.setAttribute("noRecords", noRecords);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("userNameMap", userNameMap);
        request.setAttribute("userRoleMap", userRoleMap);
        request.setAttribute("lastDirectorNoteMap", lastDirectorNoteMap);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("order", order);
        request.getRequestDispatcher("requestHistory.jsp").forward(request, response);
    }
}
