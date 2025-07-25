package controller;

import dao.PurchaseOrderDAO;
import dao.PurchaseOrderDetailDAO;
import model.PurchaseOrder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "RecentTransactionsServlet", urlPatterns = {"/recentTransactions"})
public class RecentTransactionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
        List<PurchaseOrder> allPurchases = new ArrayList<>();
        try {
            allPurchases = purchaseOrderDAO.getPurchaseOrdersByStatus("Completed");
            allPurchases = allPurchases.stream().sorted(Comparator.comparing(PurchaseOrder::getOrderDate).reversed()).collect(Collectors.toList());
            PurchaseOrderDetailDAO detailDAO = new PurchaseOrderDetailDAO();
            for (PurchaseOrder order : allPurchases) {
                order.setDetails(detailDAO.getDetailsByOrderId(order.getPurchaseOrderId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("allPurchases", allPurchases);
        request.getRequestDispatcher("recentTransactions.jsp").forward(request, response);
    }
} 