package controller;

import dao.PurchaseOrderDAO;
import dao.PurchaseOrderDetailDAO;
import model.PurchaseOrder;
import model.PurchaseOrderDetail;
import java.sql.SQLException;

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
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
        String idStr = request.getParameter("id");
        int orderId = Integer.parseInt(idStr);

        PurchaseOrderDAO orderDAO = new PurchaseOrderDAO();
        PurchaseOrder order = orderDAO.getPurchaseOrderById(orderId);

        PurchaseOrderDetailDAO detailDAO = new PurchaseOrderDetailDAO();
        List<PurchaseOrderDetail> details = detailDAO.getDetailsByOrderId(orderId);

        request.setAttribute("order", order);
        request.setAttribute("details", details);

        request.getRequestDispatcher("purchaseOrderDetail.jsp").forward(request, response);
    } catch (SQLException e) {
        e.printStackTrace();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<h2 style='color:red'>Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage() + "</h2>");
    } catch (Exception e) {
        e.printStackTrace();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<h2 style='color:red'>Lỗi hệ thống: " + e.getMessage() + "</h2>");
    }
}
} 