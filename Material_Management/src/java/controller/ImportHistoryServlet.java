package controller;

import dao.UnitConversionDao;
import model.ImportHistory;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ImportHistoryServlet", urlPatterns = {"/importHistoryList"})
public class ImportHistoryServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UnitConversionDao dao = new UnitConversionDao();
        
        // Lấy tham số lọc và phân trang
        String projectName = request.getParameter("projectName");
        String createdDate = request.getParameter("createdDate");
        
        // Phân trang
        int page = 1;
        int pageSize = 5; // 5 dòng mỗi trang
        
        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.trim().isEmpty()) {
                page = Integer.parseInt(pageStr);
            }
        } catch (NumberFormatException e) {
            page = 1;
        }
        
        // Lấy dữ liệu với lọc và phân trang
        List<ImportHistory> importHistoryList = dao.getImportHistoryListFiltered(projectName, createdDate, page, pageSize);
        int totalRecords = dao.countImportHistoryFiltered(projectName, createdDate);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        
        // Set attributes cho JSP
        request.setAttribute("importHistoryList", importHistoryList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("projectName", projectName);
        request.setAttribute("createdDate", createdDate);
        
        request.getRequestDispatcher("importHistoryList.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
