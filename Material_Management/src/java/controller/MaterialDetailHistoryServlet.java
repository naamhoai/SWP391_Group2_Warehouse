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
        String keyword = request.getParameter("keyword");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String roleName = request.getParameter("roleName");
        MaterialDetailHistoryDAO dao = new MaterialDetailHistoryDAO();
        if (materialIdStr == null) {
            List<MaterialDetailHistory> historyList = dao.getFilteredHistory(fromDate, toDate, keyword, roleName, keyword);
            request.setAttribute("historyList", historyList);
            request.setAttribute("showAll", true);
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