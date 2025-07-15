package controller;

import dao.ExportFormDAO;
import model.ExportForm;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet(name = "ExportFormHistoryServlet", urlPatterns = {"/exportFormHistory"})
public class ExportFormHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String sortField = request.getParameter("sortField");
            String sortDir = request.getParameter("sortDir");
            if (sortField == null) {
                sortField = "export_id";
            }
            if (sortDir == null) {
                sortDir = "asc";
            }

            int page = 1;
            int pageSize = 5;
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
            }

            ExportFormDAO dao = new ExportFormDAO();
            String projectName = request.getParameter("projectName");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            
            LocalDate start = null, end = null;
            if (startDate != null && !startDate.isEmpty()) {
                start = LocalDate.parse(startDate);
            }
            if (endDate != null && !endDate.isEmpty()) {
                end = LocalDate.parse(endDate);
            }

            if (start != null && end != null && start.isAfter(end)) {
                request.setAttribute("error", "Ngày bắt đầu không được lớn hơn ngày kết thúc!");
                request.getRequestDispatcher("exportFormHistory.jsp").forward(request, response);
                return;
            }

            int totalExportForms = dao.countAllExportForms(projectName);
            List<ExportForm> exportForms = dao.getAllExportFormsAndApprovedRequests(projectName, sortField, sortDir, page, pageSize, startDate, endDate);
            request.setAttribute("exportForms", exportForms);
            request.setAttribute("currentPage", page);
            int totalPages = (int) Math.ceil((double) totalExportForms / pageSize);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("sortField", sortField);
            request.setAttribute("sortDir", sortDir);
            request.getRequestDispatcher("exportFormHistory.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách phiếu xuất kho: " + e.getMessage());
            request.getRequestDispatcher("exportFormHistory.jsp").forward(request, response);
        }
    }
}
