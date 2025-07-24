package controller;

import dao.InventoryHistoryDAO;
import model.InventoryHistoryRow;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "InventoryHistoryServlet", urlPatterns = {"/InventoryHistoryServlet"})
public class InventoryHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String transactionType = request.getParameter("transactionType");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");

        int page = 1;
        int pageSize = 10;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
        } catch (Exception ignored) {}
        try {
            String pageSizeParam = request.getParameter("pageSize");
            if (pageSizeParam != null && !pageSizeParam.isEmpty()) {
                pageSize = Integer.parseInt(pageSizeParam);
            }
        } catch (Exception ignored) {}

        InventoryHistoryDAO dao = new InventoryHistoryDAO();
        int totalRecords = dao.countInventoryHistoryByKeyword(keyword, transactionType, fromDate, toDate);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (page < 1) page = 1;
        if (page > totalPages && totalPages > 0) page = totalPages;
        List<InventoryHistoryRow> historyList = dao.getInventoryHistoryPagingByKeyword(keyword, transactionType, fromDate, toDate, page, pageSize);
        request.setAttribute("historyList", historyList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalRecords", totalRecords);
        int startPage = Math.max(1, page - 2);
        int endPage = Math.min(totalPages, page + 2);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("keyword", keyword);
        request.setAttribute("transactionType", transactionType);
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);
        request.getRequestDispatcher("inventoryHistory.jsp").forward(request, response);
    }
} 