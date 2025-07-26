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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String transactionType = request.getParameter("transactionType"); // Lấy tham số mới

        int page = 1;
        int pageSize = 10;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException ignored) {}

        InventoryHistoryDAO dao = new InventoryHistoryDAO();
        // Gọi DAO với tham số mới
        List<InventoryHistoryRow> historyList = dao.getInventoryHistoryPagingByKeyword(keyword, fromDate, toDate, transactionType, page, pageSize);
        int totalRecords = dao.countInventoryHistoryByKeyword(keyword, fromDate, toDate, transactionType);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        
        if (page > totalPages && totalPages > 0) page = totalPages;
        if (page < 1) page = 1;

        request.setAttribute("historyList", historyList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);
        request.setAttribute("keyword", keyword);
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);
        request.setAttribute("transactionType", transactionType); // Truyền lại JSP

        int startPage = Math.max(1, page - 2);
        int endPage = Math.min(totalPages, page + 2);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);

        request.getRequestDispatcher("inventoryHistory.jsp").forward(request, response);
    }
}