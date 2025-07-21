package controller;

import dao.PurchaseOrderDAO;
import dao.PurchaseOrderDetailDAO;
import model.PurchaseOrder;
import model.PurchaseOrderDetail;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/purchaseOrderDetail")
public class PurchaseOrderDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu mã đơn mua!");
            return;
        }
        int orderId = Integer.parseInt(idStr);
        PurchaseOrderDAO orderDAO = new PurchaseOrderDAO();
        PurchaseOrderDetailDAO detailDAO = new PurchaseOrderDetailDAO();

        PurchaseOrder order = null;
        List<PurchaseOrderDetail> details = null;
        try {
            order = orderDAO.getPurchaseOrderById(orderId);
            details = detailDAO.getDetailsByOrderId(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("order", order);
        request.setAttribute("details", details);
        request.getRequestDispatcher("purchaseOrderDetail.jsp").forward(request, response);
    }
} 