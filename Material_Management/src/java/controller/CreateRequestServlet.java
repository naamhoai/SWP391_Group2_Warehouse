package controller;

import dao.*;
import model.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet(name = "CreateRequestServlet", urlPatterns = {"/createRequest"})
public class CreateRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> parentCategories = categoryDAO.getParentCategories();
            List<Category> subCategories = new ArrayList<>();
            for (Category cat : categoryDAO.getAllCategories()) {
                if (cat.getParentId() != null) {
                    subCategories.add(cat);
                }
            }

            Integer userId = (Integer) request.getSession().getAttribute("userId");
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserById(userId);

            InventoryDAO inventoryDAO = new InventoryDAO();
            List<Inventory> inventoryList = inventoryDAO.getInventoryWithMaterialInfo();
            UnitConversionDao unitDao = new UnitConversionDao();
           

            request.setAttribute("userName", user.getFullname());
            request.setAttribute("materialList", inventoryList);
          
            request.setAttribute("parentCategories", parentCategories);
            request.setAttribute("subCategories", subCategories);

            request.getRequestDispatcher("requestForm.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải form: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reason = request.getParameter("reason");
        String recipientName = request.getParameter("recipientName");
        String deliveryAddress = request.getParameter("deliveryAddress");
        String contactPerson = request.getParameter("contactPerson");
        String contactPhone = request.getParameter("contactPhone");
        String[] materialNames = request.getParameterValues("materialName");
        String[] quantities = request.getParameterValues("quantity");
        String[] materialConditions = request.getParameterValues("materialCondition");

        try {
            if (reason == null || reason.trim().isEmpty() || reason.length() > 500) {
                request.setAttribute("error", "Lý do không được để trống và không vượt quá 500 ký tự.");
                request.setAttribute("materialNames", materialNames);
                request.setAttribute("quantities", quantities);
                request.setAttribute("materialConditions", materialConditions);
                doGet(request, response);
                return;
            }
            if (recipientName == null || recipientName.trim().isEmpty() || recipientName.length() > 255) {
                request.setAttribute("error", "Vui lòng nhập tên dự án/khách hàng (không quá 255 ký tự).");
                request.setAttribute("materialNames", materialNames);
                request.setAttribute("quantities", quantities);
                request.setAttribute("materialConditions", materialConditions);
                doGet(request, response);
                return;
            }
            if (deliveryAddress == null || deliveryAddress.trim().isEmpty() || deliveryAddress.length() > 255) {
                request.setAttribute("error", "Vui lòng nhập địa chỉ giao hàng (không quá 255 ký tự).");
                request.setAttribute("materialNames", materialNames);
                request.setAttribute("quantities", quantities);
                request.setAttribute("materialConditions", materialConditions);
                doGet(request, response);
                return;
            }
            if (contactPerson == null || contactPerson.trim().isEmpty() || contactPerson.length() > 100) {
                request.setAttribute("error", "Vui lòng nhập người liên hệ (không quá 100 ký tự).");
                request.setAttribute("materialNames", materialNames);
                request.setAttribute("quantities", quantities);
                request.setAttribute("materialConditions", materialConditions);
                doGet(request, response);
                return;
            }
            if (contactPhone == null || contactPhone.trim().isEmpty()
                    || !contactPhone.matches("^0[0-9]{9,10}$")) {
                request.setAttribute("error", "Số điện thoại liên hệ không hợp lệ (bắt đầu bằng 0, 10-11 số).");
                request.setAttribute("materialNames", materialNames);
                request.setAttribute("quantities", quantities);
                request.setAttribute("materialConditions", materialConditions);
                doGet(request, response);
                return;
            }
            if (materialNames == null || materialNames.length == 0) {
                request.setAttribute("error", "Vui lòng thêm ít nhất một vật tư.");
                request.setAttribute("materialNames", materialNames);
                request.setAttribute("quantities", quantities);
                request.setAttribute("materialConditions", materialConditions);
                doGet(request, response);
                return;
            }
            Set<String> materialSet = new HashSet<>();
            InventoryDAO inventoryDAO = new InventoryDAO();
            List<Inventory> inventoryList = inventoryDAO.getInventoryWithMaterialInfo();
            for (int i = 0; i < materialNames.length; i++) {
                String name = materialNames[i];
                String quantity = quantities[i];
                String condition = (materialConditions != null && materialConditions.length > i) ? materialConditions[i] : null;
                if (name == null || name.trim().isEmpty() || name.length() > 100) {
                    request.setAttribute("error", "Tên vật tư ở dòng " + (i + 1) + " không hợp lệ (không để trống, không quá 100 ký tự).");
                    request.setAttribute("materialNames", materialNames);
                    request.setAttribute("quantities", quantities);
                    request.setAttribute("materialConditions", materialConditions);
                    doGet(request, response);
                    return;
                }
                if (materialSet.contains(name)) {
                    request.setAttribute("error", "Vật tư ở dòng " + (i + 1) + " bị trùng. Vui lòng không chọn 2 lần cùng một vật tư.");
                    request.setAttribute("materialNames", materialNames);
                    request.setAttribute("quantities", quantities);
                    request.setAttribute("materialConditions", materialConditions);
                    doGet(request, response);
                    return;
                }
                materialSet.add(name);
                if (quantity == null || quantity.trim().isEmpty() || !quantity.matches("^[0-9]+$")) {
                    request.setAttribute("error", "Số lượng ở dòng " + (i + 1) + " không hợp lệ (không để trống, phải là số nguyên).");
                    request.setAttribute("materialNames", materialNames);
                    request.setAttribute("quantities", quantities);
                    request.setAttribute("materialConditions", materialConditions);
                    doGet(request, response);
                    return;
                }
                int qty = Integer.parseInt(quantity);
                if (qty <= 0 || qty > 999999) {
                    request.setAttribute("error", "Số lượng ở dòng " + (i + 1) + " phải lớn hơn 0 và không vượt quá 999,999.");
                    request.setAttribute("materialNames", materialNames);
                    request.setAttribute("quantities", quantities);
                    request.setAttribute("materialConditions", materialConditions);
                    doGet(request, response);
                    return;
                }
                Inventory matched = null;
                for (Inventory inv : inventoryList) {
                    if (inv.getMaterialName().equalsIgnoreCase(name.trim()) && inv.getMaterialCondition().equalsIgnoreCase(condition.trim())) {
                        matched = inv;
                        break;
                    }
                }
                if (matched == null) {
                    request.setAttribute("error", "Không tìm thấy vật tư: " + name);
                    doGet(request, response);
                    return;
                }
                if (condition == null || condition.trim().isEmpty()) {
                    request.setAttribute("error", "Điều kiện vật tư ở dòng " + (i + 1) + " không được để trống.");
                    request.setAttribute("materialNames", materialNames);
                    request.setAttribute("quantities", quantities);
                    request.setAttribute("materialConditions", materialConditions);
                    doGet(request, response);
                    return;
                }
            }

            int userId = (int) request.getSession().getAttribute("userId");
            Timestamp now = new Timestamp(System.currentTimeMillis());

            Request req = new Request();
            req.setUserId(userId);
            req.setReason(reason);
            req.setRecipientName(recipientName);
            req.setDeliveryAddress(deliveryAddress);
            req.setContactPerson(contactPerson);
            req.setContactPhone(contactPhone);
            req.setRequestStatus("Chờ duyệt");
            req.setCreatedAt(now);

            RequestDAO requestDAO = new RequestDAO();
            int requestId = requestDAO.addRequest(req);

            RequestHistory history = new RequestHistory();
            history.setRequestId(requestId);
            history.setChangedBy(userId);
            history.setOldStatus(null);
            history.setNewStatus("Chờ duyệt");
            history.setAction("Tạo yêu cầu");
            history.setChangeReason(reason);
            history.setDirectorNote(null);

            for (int i = 0; i < materialNames.length; i++) {
                String name = materialNames[i];
                int qty = Integer.parseInt(quantities[i]);
                String condition = materialConditions[i];

                Inventory matched = null;
                for (Inventory inv : inventoryList) {
                    if (inv.getMaterialName().equalsIgnoreCase(name.trim()) && inv.getMaterialCondition().equalsIgnoreCase(condition.trim())) {
                        matched = inv;
                        break;
                    }
                }

                if (matched != null) {
                    RequestHistoryDetail detail = new RequestHistoryDetail();
                    detail.setMaterialId(matched.getMaterialId());
                    detail.setMaterialName(matched.getMaterialName());
                    detail.setQuantity(qty);
                    detail.setWarehouseUnitId(matched.getUnitId());
                    detail.setUnitName(matched.getUnitName());
                    detail.setMaterialCondition(condition);
                    history.addHistoryDetail(detail);
                }
            }

            new RequestHistoryDAO().addRequestHistory(history);

            dao.RequestDetailDAO detailDAO = new dao.RequestDetailDAO();
            for (int i = 0; i < materialNames.length; i++) {
                String name = materialNames[i];
                int qty = Integer.parseInt(quantities[i]);
                String condition = materialConditions[i];

                Inventory matched = null;
                for (Inventory inv : inventoryList) {
                    if (inv.getMaterialName().equalsIgnoreCase(name.trim()) && inv.getMaterialCondition().equalsIgnoreCase(condition.trim())) {
                        matched = inv;
                        break;
                    }
                }
                if (matched == null) {
                    request.setAttribute("error", "Không tìm thấy vật tư: " + name);
                    doGet(request, response);
                    return;
                }

                RequestDetail detail = new RequestDetail();
                detail.setRequestId(requestId);
                detail.setMaterialId(matched.getMaterialId());
                detail.setMaterialName(matched.getMaterialName());
                detail.setQuantity(qty);
                detail.setWarehouseUnitId(matched.getUnitId());
                detail.setUnitName(matched.getUnitName());
                detail.setMaterialCondition(condition);
                detailDAO.addRequestDetail(detail);
            }

            UserDAO userDAO = new UserDAO();
            Integer directorId = userDAO.getDirectorId();
            User sender = userDAO.getUserById(userId);
            if (directorId != null) {
                NotificationDAO notificationDAO = new NotificationDAO();
                String message = "Có yêu cầu vật tư mới từ " + sender.getFullname();
                notificationDAO.addNotification(directorId, message, requestId);
            }

            response.sendRedirect("RequestListServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi gửi yêu cầu: " + e.getMessage());
        }
    }
}
