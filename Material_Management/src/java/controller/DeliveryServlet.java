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
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet(name = "DeliveryServlet", urlPatterns = {"/delivery"})
public class DeliveryServlet extends HttpServlet {
    private final DeliveryDAO deliveryDAO = new DeliveryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null && action.equals("delete")) {
            deleteDelivery(request, response);
        } else if (action != null && action.equals("edit")) {
            showEditForm(request, response);
        } else {
            listDeliveries(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "add":
                    addDelivery(request, response);
                    break;
                case "edit":
                    updateDelivery(request, response);
                    break;
                default:
                    listDeliveries(request, response);
                    break;
            }
        } else {
            listDeliveries(request, response);
        }
    }

    private void listDeliveries(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra quyền delivery_view
        if (!hasPermission(request, "delivery_view")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        List<Delivery> deliveries = deliveryDAO.getAllDeliveries();
        request.setAttribute("deliveries", deliveries);
        request.getRequestDispatcher("/delivery.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra quyền delivery_edit
        if (!hasPermission(request, "delivery_edit")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        Delivery delivery = deliveryDAO.getDeliveryById(id);
        request.setAttribute("delivery", delivery);
        request.getRequestDispatcher("/delivery.jsp").forward(request, response);
    }

    private void addDelivery(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Kiểm tra quyền delivery_add
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
            delivery.setDeliveryDate(Timestamp.valueOf(
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(
                    request.getParameter("deliveryDate")).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()));
            delivery.setDescription(request.getParameter("description"));

            if (deliveryDAO.addDelivery(delivery)) {
                response.sendRedirect("delivery?success=Delivery added successfully");
            } else {
                response.sendRedirect("delivery?error=Failed to add delivery");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("delivery?error=Invalid input");
        }
    }

    private void updateDelivery(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Kiểm tra quyền delivery_edit
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
            delivery.setDeliveryDate(Timestamp.valueOf(
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(
                    request.getParameter("deliveryDate")).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()));
            delivery.setDescription(request.getParameter("description"));

            if (deliveryDAO.updateDelivery(delivery)) {
                response.sendRedirect("delivery?success=Delivery updated successfully");
            } else {
                response.sendRedirect("delivery?error=Failed to update delivery");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("delivery?error=Invalid input");
        }
    }

    private void deleteDelivery(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Kiểm tra quyền delivery_delete
        if (!hasPermission(request, "delivery_delete")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        if (deliveryDAO.deleteDelivery(id)) {
            response.sendRedirect("delivery?success=Delivery deleted successfully");
        } else {
            response.sendRedirect("delivery?error=Failed to delete delivery");
        }
    }

    private boolean hasPermission(HttpServletRequest request, String permission) {
        // Giả định lấy role_id từ session
        Integer roleId = (Integer) request.getSession().getAttribute("role_id");
        if (roleId == null) return false;

        // Kiểm tra quyền từ bảng role_permissions (giả định)
        // Thay bằng truy vấn thực tế đến DB hoặc cache
        List<String> permissions = getPermissionsForRole(roleId);
        return permissions.contains(permission);
    }

    private List<String> getPermissionsForRole(int roleId) {
        // Dữ liệu mẫu, thay bằng truy vấn DB thực tế
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