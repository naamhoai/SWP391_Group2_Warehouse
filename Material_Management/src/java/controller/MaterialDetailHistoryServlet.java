package controller;

import dao.MaterialDetailHistoryDAO;
import model.MaterialDetailHistory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MaterialDetailHistoryServlet", urlPatterns = {"/materialDetailHistory"})
public class MaterialDetailHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String materialIdStr = request.getParameter("materialId");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String materialName = request.getParameter("materialName");
        String roleName = request.getParameter("roleName");
        String userName = request.getParameter("userName");
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
        MaterialDetailHistoryDAO dao = new MaterialDetailHistoryDAO();
        if (materialIdStr == null) {
            int totalRecords = dao.countFilteredHistory(fromDate, toDate, materialName, roleName, userName);
            int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
            if (page < 1) page = 1;
            if (page > totalPages && totalPages > 0) page = totalPages;
            List<MaterialDetailHistory> historyList = dao.getFilteredHistoryPaging(fromDate, toDate, materialName, roleName, userName, page, pageSize);
            request.setAttribute("historyList", historyList);
            request.setAttribute("showAll", true);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
            request.getRequestDispatcher("materialDetailHistory.jsp").forward(request, response);
            return;
        }
        int materialId;
        try {
            materialId = Integer.parseInt(materialIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("materialDetailList.jsp");
            return;
        }
        List<MaterialDetailHistory> historyList = dao.getHistoryByMaterialId(materialId);
        request.setAttribute("historyList", historyList);
        request.setAttribute("materialId", materialId);
        request.setAttribute("showAll", false);
        request.getRequestDispatcher("materialDetailHistory.jsp").forward(request, response);
    }
} 