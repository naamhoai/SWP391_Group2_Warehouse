package servlet;

import dao.DeliveryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Delivery;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "DeliveryServlet", urlPatterns = {"/delivery"})
public class DeliveryServlet extends HttpServlet {
    private final DeliveryDAO deliveryDAO = new DeliveryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            deleteDelivery(request, response);
        } else if ("edit".equals(action)) {
            showEditForm(request, response);
        } else {
            listDeliveries(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addDelivery(request, response);
        } else if ("edit".equals(action)) {
            updateDelivery(request, response);
        } else {
            listDeliveries(request, response);
        }
    }

    private void listDeliveries(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String status = request.getParameter("status");
        List<Delivery> deliveries = (status != null && !status.isEmpty()) 
            ? deliveryDAO.getDeliveriesByStatus(status) 
            : deliveryDAO.getAllDeliveries();
        request.setAttribute("deliveries", deliveries);
        request.setAttribute("exportForms", deliveryDAO.getAllExportForms()); // Giả định có phương thức này
        request.getRequestDispatcher("/delivery.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!hasPermission(request, "delivery_edit")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        Delivery delivery = deliveryDAO.getDeliveryById(id);
        request.setAttribute("delivery", delivery);
        request.setAttribute("exportForms", deliveryDAO.getAllExportForms()); // Giả định có phương thức này
        request.getRequestDispatcher("/delivery.jsp").forward(request, response);
    }

    private void addDelivery(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!hasPermission(request, "delivery_add")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        try {
            Delivery delivery = new Delivery();
            delivery.setExportId(Integer.parseInt(request.getParameter("exportId")));
            delivery.setReceiverName(request.getParameter("receiverName"));
            delivery.setDeliveryAddress(request.getParameter("deliveryAddress"));
            delivery.setStatus(request.getParameter("status"));
            delivery.setDeliveryDate(Timestamp.valueOf(LocalDateTime.parse(request.getParameter("deliveryDate") + "T00:00:00")));
            delivery.setDescription(request.getParameter("description"));

            if (deliveryDAO.addDelivery(delivery)) {
                response.sendRedirect("delivery?success=Thêm giao hàng thành công");
            } else {
                response.sendRedirect("delivery?error=Thêm giao hàng thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("delivery?error=Dữ liệu không hợp lệ");
        }
    }

    private void updateDelivery(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!hasPermission(request, "delivery_edit")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        try {
            Delivery delivery = new Delivery();
            delivery.setId(Integer.parseInt(request.getParameter("deliveryId")));
            delivery.setExportId(Integer.parseInt(request.getParameter("exportId")));
            delivery.setReceiverName(request.getParameter("receiverName"));
            delivery.setDeliveryAddress(request.getParameter("deliveryAddress"));
            delivery.setStatus(request.getParameter("status"));
            delivery.setDeliveryDate(Timestamp.valueOf(LocalDateTime.parse(request.getParameter("deliveryDate") + "T00:00:00")));
            delivery.setDescription(request.getParameter("description"));

            if (deliveryDAO.updateDelivery(delivery)) {
                response.sendRedirect("delivery?success=Cập nhật giao hàng thành công");
            } else {
                response.sendRedirect("delivery?error=Cập nhật giao hàng thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("delivery?error=Dữ liệu không hợp lệ");
        }
    }

    private void deleteDelivery(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!hasPermission(request, "delivery_delete")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        if (deliveryDAO.deleteDelivery(id)) {
            response.sendRedirect("delivery?success=Xóa giao hàng thành công");
        } else {
            response.sendRedirect("delivery?error=Xóa giao hàng thất bại");
        }
    }

    private boolean hasPermission(HttpServletRequest request, String permission) {
        Integer roleId = (Integer) request.getSession().getAttribute("role_id");
        if (roleId == null) return false;

        List<String> permissions = getPermissionsForRole(roleId);
        return permissions.contains(permission);
    }

    private List<String> getPermissionsForRole(int roleId) {
        switch (roleId) {
            case 1: // Admin
                return List.of("delivery_view", "delivery_add", "delivery_edit", "delivery_delete");
            case 2: // Director
                return List.of("delivery_view");
            case 3: // WarehouseStaff
                return List.of("delivery_view", "delivery_add", "delivery_edit");
            case 4: // Employee
                return List.of("delivery_view");
            default:
                return List.of();
        }
    }
}