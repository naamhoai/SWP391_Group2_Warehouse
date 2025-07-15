package controller;

import dao.RequestHistoryDetailDAO;
import dao.RequestHistoryDAO;
import model.RequestHistoryDetail;
import model.RequestHistory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "RequestHistoryDetailServlet", urlPatterns = {"/requestHistoryDetail"})
public class RequestHistoryDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String historyIdParam = request.getParameter("historyId");
        if (historyIdParam == null || historyIdParam.isEmpty()) {
            response.sendRedirect("requestHistory");
            return;
        }
        
        try {
            int historyId = Integer.parseInt(historyIdParam);
            
            RequestHistoryDetailDAO detailDAO = new RequestHistoryDetailDAO();
            RequestHistoryDAO historyDAO = new RequestHistoryDAO();
            
            RequestHistory history = historyDAO.getRequestHistoryByHistoryId(historyId);
            if (history == null) {
                response.sendRedirect("requestHistory");
                return;
            }
            
            List<RequestHistoryDetail> details = detailDAO.getDetailsByHistoryId(historyId);
            
            request.setAttribute("history", history);
            request.setAttribute("details", details);
            
            request.getRequestDispatcher("requestHistoryDetail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("requestHistory");
        }
    }
} 