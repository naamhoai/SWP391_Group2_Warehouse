package controller;

import dao.ImportHistoryDAO;
import model.ImportHistory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ImportHistoryServlet", urlPatterns = {"/importHistory"})
public class ImportHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ImportHistoryDAO dao = new ImportHistoryDAO();
        String search = request.getParameter("search");
        String unit = request.getParameter("unit");
        String date = request.getParameter("date");
        String status = request.getParameter("status");
        System.out.println("search" + search);
        System.out.println("status" + status);
        System.out.println("date" + date);
        System.out.println("unit" + unit);
        int page = 1;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
        } catch (Exception ignored) {
        }

        List<ImportHistory> historyList = dao.getFilteredImportHistory(search, status, date, unit, page);
        int totalPages = 1;
        totalPages = dao.getcountPages();
        try {
//            totalPages = dao.countFiltered(search.trim(), status.trim(), date.trim(), unit.trim());
            
            System.out.println(totalPages + "......");
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("historyList", historyList);
        request.setAttribute("search", search);
        request.setAttribute("unit", unit);
        request.setAttribute("date", date);
        request.setAttribute("status", status);
        request.setAttribute("currentPage", page);
        request.setAttribute("pages", totalPages);
        request.getRequestDispatcher("importHistory.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ImportHistoryDAO dao = new ImportHistoryDAO();
        String search = request.getParameter("search");
        String unit = request.getParameter("unit");
        String date = request.getParameter("date");
        String status = request.getParameter("status");
        System.out.println("search" + search);

        System.out.println("status" + status);
        System.out.println("date" + date);
        System.out.println("unit" + unit);
        int totalRecords = dao.getcountPages();
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception ignored) {
        }

        List<ImportHistory> historyList = dao.getFilteredImportHistory(search, status, date, unit, page);

//        int totalRecords = dao.countFiltered(search, status, date, unit);
        System.out.println("totalRecords" + totalRecords);

        request.setAttribute("historyList", historyList);
        request.setAttribute("search", search);
        request.setAttribute("unit", unit);
        request.setAttribute("date", date);
        request.setAttribute("status", status);
        request.setAttribute("currentPage", page);
        request.setAttribute("pages", totalRecords);
        request.getRequestDispatcher("importHistory.jsp").forward(request, response);
    }
}
