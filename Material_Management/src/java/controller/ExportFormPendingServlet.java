package controller;

import dao.RequestDAO;
import model.Request;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.sql.SQLException;

@WebServlet("/exportFormPending")
public class ExportFormPendingServlet extends HttpServlet {

    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String projectName = request.getParameter("projectName");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String sortField = request.getParameter("sortField");
        String sortDir = request.getParameter("sortDir");
        String pageStr = request.getParameter("page");

        int page = 1;
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        if (sortField == null || !(sortField.equals("request_id") || sortField.equals("recipient_name") || sortField.equals("created_at"))) {
            sortField = "created_at";
        }
        if (sortDir == null || !(sortDir.equalsIgnoreCase("ASC") || sortDir.equalsIgnoreCase("DESC"))) {
            sortDir = "DESC";
        }

        Timestamp startDate = null, endDate = null;
        try {
            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = Timestamp.valueOf(startDateStr + " 00:00:00");
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = Timestamp.valueOf(endDateStr + " 23:59:59");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestDAO dao = new RequestDAO();
        List<Request> approvedRequests = null;
        int totalRecords = 0;
        try {
            approvedRequests = dao.getApprovedRequestsWithPendingExport(
                    projectName, startDate, endDate, sortField, sortDir, page, PAGE_SIZE
            );
            totalRecords = dao.countApprovedRequestsWithPendingExport(
                    projectName, startDate, endDate
            );
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        if (startDate != null && endDate != null && startDate.after(endDate)) {
            request.setAttribute("error", "Ngày bắt đầu không được lớn hơn ngày kết thúc!");
            request.getRequestDispatcher("exportFormPending.jsp").forward(request, response);
            return;
        }
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

        request.setAttribute("approvedRequests", approvedRequests);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("projectName", projectName);
        request.setAttribute("startDate", startDateStr);
        request.setAttribute("endDate", endDateStr);
        request.setAttribute("sortField", sortField);
        request.setAttribute("sortDir", sortDir);

        request.getRequestDispatcher("exportFormPending.jsp").forward(request, response);
    }
}
