package controller;

import dao.DeliveryDAO;
import model.Delivery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DeliveryServlet", urlPatterns = {"/delivery"})
public class DeliveryServlet extends HttpServlet {
    private final DeliveryDAO deliveryDAO = new DeliveryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        listDeliveries(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        listDeliveries(request, response);
    }

    private void listDeliveries(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String status = request.getParameter("status");
        String pageParam = request.getParameter("page");
        int pageSize = 5;
        int currentPage = 1;
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        List<Delivery> allDeliveries = (status != null && !status.isEmpty())
                ? deliveryDAO.getDeliveriesByStatus(status)
                : deliveryDAO.getAllDeliveries();

        int totalDeliveries = allDeliveries.size();
        int totalPages = (int) Math.ceil((double) totalDeliveries / pageSize);
        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalDeliveries);

        List<Delivery> deliveries = (fromIndex < totalDeliveries)
                ? allDeliveries.subList(fromIndex, toIndex)
                : java.util.Collections.emptyList();

        request.setAttribute("deliveries", deliveries);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("delivery.jsp").forward(request, response);
    }
}
