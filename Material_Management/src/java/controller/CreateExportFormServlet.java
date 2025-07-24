package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.RequestDAO;
import dao.RequestDetailDAO;
import dao.ExportFormDAO;
import dao.ExportMaterialDAO;
import dao.InventoryDAO;
import dao.NotificationDAO;
import dao.DeliveryDAO;
import dao.UserDAO;
import model.Request;
import model.RequestDetail;
import model.ExportForm;
import model.ExportMaterial;
import model.Delivery;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.User;

@WebServlet(name = "CreateExportFormServlet", urlPatterns = {"/createExportForm"})
public class CreateExportFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));

            RequestDAO requestDAO = new RequestDAO();
            RequestDetailDAO detailDAO = new RequestDetailDAO();
            ExportMaterialDAO exportMaterialDAO = new ExportMaterialDAO();

            Request req = requestDAO.getRequestById(requestId);
            List<RequestDetail> details = detailDAO.getRequestDetailsByRequestId(requestId);

            Map<Integer, Integer> exportedMap = exportMaterialDAO.getExportedQuantitiesByRequestId(requestId);
            for (RequestDetail d : details) {
                int exported = exportedMap != null ? exportedMap.getOrDefault(d.getMaterialId(), 0) : 0;
                int remaining = d.getQuantity() - exported;
                d.setQuantity(remaining > 0 ? remaining : 0);
            }

            InventoryDAO inventoryDAO = new InventoryDAO();
            Map<Integer, Integer> inventoryMap = new HashMap<>();
            for (RequestDetail d : details) {
                int inventoryQty = inventoryDAO.getTotalInventoryByMaterialIdAndCondition(d.getMaterialId(), d.getMaterialCondition());
                inventoryMap.put(d.getMaterialId(), inventoryQty);
            }

            request.setAttribute("request", req);
            request.setAttribute("details", details);
            request.setAttribute("inventoryMap", inventoryMap);
            int userId = (Integer) request.getSession().getAttribute("userId");
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserById(userId);
            request.setAttribute("userName", user.getFullname());

            request.getRequestDispatcher("createExportForm.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không lấy được dữ liệu yêu cầu: " + e.getMessage());
            request.getRequestDispatcher("createExportForm.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Request req = null;
        List<RequestDetail> details = null;
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            int userId = (Integer) request.getSession().getAttribute("userId");
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserById(userId);
            request.setAttribute("userName", user.getFullname());

            RequestDAO requestDAO = new RequestDAO();
            req = requestDAO.getRequestById(requestId);
            RequestDetailDAO detailDAO = new RequestDetailDAO();
            details = detailDAO.getRequestDetailsByRequestId(requestId);

            ExportMaterialDAO exportMaterialDAO = new ExportMaterialDAO();
            Map<Integer, Integer> exportedMap = exportMaterialDAO.getExportedQuantitiesByRequestId(requestId);
            for (RequestDetail d : details) {
                int exported = exportedMap != null ? exportedMap.getOrDefault(d.getMaterialId(), 0) : 0;
                int remaining = d.getQuantity() - exported;
                d.setQuantity(remaining > 0 ? remaining : 0);
            }

            InventoryDAO inventoryDAO = new InventoryDAO();
            boolean hasInsufficientStock = false;
            List<String> shortages = new ArrayList<>();

            for (RequestDetail d : details) {
                int inventoryQty = inventoryDAO.getTotalInventoryByMaterialIdAndCondition(d.getMaterialId(), d.getMaterialCondition());
                if (d.getQuantity() > inventoryQty) {
                    hasInsufficientStock = true;
                    shortages.add(d.getMaterialName() + " (" + inventoryQty + "/" + d.getQuantity() + ")");
                }
            }
            String partialNote = String.join(", ", shortages);

            boolean confirmPartial = "true".equals(request.getParameter("confirmPartialExport"));
            if (hasInsufficientStock && !confirmPartial) {
                NotificationDAO notificationDAO = new NotificationDAO();
                String message = "Yêu cầu #" + requestId + " thiếu: " + partialNote;
                notificationDAO.addNotificationToRole(4, message, requestId); // Gửi cho tất cả nhân viên công ty
                notificationDAO.addNotificationToRole(2, message, requestId); // Gửi cho tất cả giám đốc
                String warning = "Tồn kho không đủ cho một số vật tư. Bạn có muốn xuất từng phần với số lượng hiện có không?";
                String warningMsg = warning + " " + partialNote.toString();
                request.setAttribute("partialExportWarning", warningMsg);
                request.setAttribute("request", req);
                request.setAttribute("details", details);
                Map<Integer, Integer> inventoryMap = new HashMap<>();
                for (RequestDetail d : details) {
                    int inventoryQty = inventoryDAO.getTotalInventoryByMaterialIdAndCondition(d.getMaterialId(), d.getMaterialCondition());
                    inventoryMap.put(d.getMaterialId(), inventoryQty);
                }
                request.setAttribute("inventoryMap", inventoryMap);
                request.getRequestDispatcher("createExportForm.jsp").forward(request, response);
                return;
            }

            if (hasInsufficientStock) {
                String note = "Xuất kho từng phần do tồn kho không đủ. " + partialNote.toString();
                ExportForm exportForm = new ExportForm();
                exportForm.setRequestId(requestId);
                exportForm.setUserId(userId);
                exportForm.setExportDate(new Timestamp(System.currentTimeMillis()));
                exportForm.setReason(req.getReason());
                exportForm.setRecipientName(req.getRecipientName());
                exportForm.setDeliveryAddress(req.getDeliveryAddress());
                exportForm.setContactPerson(req.getContactPerson());
                exportForm.setContactPhone(req.getContactPhone());
                exportForm.setStatus("Đã xuất kho");
                exportForm.setDescription(note);

                ExportFormDAO exportFormDAO = new ExportFormDAO();
                int exportId = exportFormDAO.createPartialExportForm(exportForm, note);

                boolean hasExported = false;
                for (RequestDetail d : details) {
                    int inventoryQty = inventoryDAO.getTotalInventoryByMaterialIdAndCondition(d.getMaterialId(), d.getMaterialCondition());
                    int exportQty = Math.min(d.getQuantity(), inventoryQty);
                    if (exportQty > 0) {
                        ExportMaterial em = new ExportMaterial();
                        em.setExportId(exportId);
                        em.setMaterialId(d.getMaterialId());
                        em.setMaterialName(d.getMaterialName());
                        em.setWarehouseUnitId(d.getWarehouseUnitId());
                        em.setQuantity(exportQty);
                        em.setMaterialCondition(d.getMaterialCondition());
                        exportMaterialDAO.addExportMaterial(em);
                        hasExported = true;
                        inventoryDAO.subtractInventory(d.getMaterialId(), d.getMaterialCondition(), exportQty);
                    }
                }

                if (!hasExported) {
                    request.setAttribute("error", "Không có vật tư nào đủ tồn kho để xuất. Vui lòng kiểm tra lại tồn kho!");
                    request.setAttribute("request", req);
                    request.setAttribute("details", details);
                    Map<Integer, Integer> inventoryMap = new HashMap<>();
                    for (RequestDetail d : details) {
                        int inventoryQty = inventoryDAO.getTotalInventoryByMaterialIdAndCondition(d.getMaterialId(), d.getMaterialCondition());
                        inventoryMap.put(d.getMaterialId(), inventoryQty);
                    }
                    request.setAttribute("inventoryMap", inventoryMap);
                    request.getRequestDispatcher("createExportForm.jsp").forward(request, response);
                    return;
                }

                DeliveryDAO deliveryDAO = new DeliveryDAO();
                Delivery delivery = new Delivery();
                delivery.setExportId(exportId);
                delivery.setUserId(userId);
                delivery.setRecipientName(req.getRecipientName());
                delivery.setDeliveryAddress(req.getDeliveryAddress());
                delivery.setContactPerson(req.getContactPerson());
                delivery.setContactPhone(req.getContactPhone());
                delivery.setStatus("Chờ giao");
                delivery.setDeliveryDate(new Timestamp(System.currentTimeMillis()));
                String userDescription = request.getParameter("description");
                delivery.setDescription(userDescription != null ? userDescription.trim() : "");
                String deliveryType = request.getParameter("deliveryType");
                delivery.setDeliveryType(deliveryType);
                deliveryDAO.insertDelivery(delivery);

                response.sendRedirect("exportFormHistory?success=true");
                return;
            }

            ExportForm exportForm = new ExportForm();
            exportForm.setRequestId(requestId);
            exportForm.setUserId(userId);
            exportForm.setExportDate(new Timestamp(System.currentTimeMillis()));
            exportForm.setReason(req.getReason());
            exportForm.setRecipientName(req.getRecipientName());
            exportForm.setDeliveryAddress(req.getDeliveryAddress());
            exportForm.setContactPerson(req.getContactPerson());
            exportForm.setContactPhone(req.getContactPhone());
            exportForm.setStatus("Đã xuất kho");
            exportForm.setDescription("");

            ExportFormDAO exportFormDAO = new ExportFormDAO();
            int exportId = exportFormDAO.addExportForm(exportForm);

            for (RequestDetail d : details) {
                ExportMaterial em = new ExportMaterial();
                em.setExportId(exportId);
                em.setMaterialId(d.getMaterialId());
                em.setMaterialName(d.getMaterialName());
                em.setWarehouseUnitId(d.getWarehouseUnitId());
                em.setQuantity(d.getQuantity());
                em.setMaterialCondition(d.getMaterialCondition());
                exportMaterialDAO.addExportMaterial(em);
                inventoryDAO.subtractInventory(d.getMaterialId(), d.getMaterialCondition(), d.getQuantity());
            }

            DeliveryDAO deliveryDAO = new DeliveryDAO();
            Delivery delivery = new Delivery();
            delivery.setExportId(exportId);
            delivery.setUserId(userId);
            delivery.setRecipientName(req.getRecipientName());
            delivery.setDeliveryAddress(req.getDeliveryAddress());
            delivery.setContactPerson(req.getContactPerson());
            delivery.setContactPhone(req.getContactPhone());
            delivery.setStatus("Chờ giao");
            delivery.setDeliveryDate(new Timestamp(System.currentTimeMillis()));
            String userDescription = request.getParameter("description");
            delivery.setDescription(userDescription != null ? userDescription.trim() : "");
            String deliveryType = request.getParameter("deliveryType");
            delivery.setDeliveryType(deliveryType);
            deliveryDAO.insertDelivery(delivery);

            response.sendRedirect("exportFormHistory?success=true");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tạo đơn xuất kho: " + e.getMessage());
            request.setAttribute("request", req);
            request.setAttribute("details", details);
            if (details != null) {
                Map<Integer, Integer> inventoryMap = new HashMap<>();
                InventoryDAO inventoryDAO = new InventoryDAO();
                for (RequestDetail d : details) {
                    int inventoryQty = inventoryDAO.getTotalInventoryByMaterialIdAndCondition(d.getMaterialId(), d.getMaterialCondition());
                    inventoryMap.put(d.getMaterialId(), inventoryQty);
                }
                request.setAttribute("inventoryMap", inventoryMap);
            }
            request.getRequestDispatcher("createExportForm.jsp").forward(request, response);
        }
    }
}
