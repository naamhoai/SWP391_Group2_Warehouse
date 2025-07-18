package controller;

import dao.RequestDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "RecentTransactionsServlet", urlPatterns = {"/recentTransactions"})
public class RecentTransactionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDAO requestDAO = new RequestDAO();
        List<Map<String, Object>> recentTransactions = requestDAO.getRecentTransactions(1000); // lấy tối đa 1000 giao dịch
        request.setAttribute("recentTransactions", recentTransactions);
        request.getRequestDispatcher("recentTransactions.jsp").forward(request, response);
    }
} 