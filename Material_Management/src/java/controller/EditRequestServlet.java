package controller;

import dao.*;
import model.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "EditRequestServlet", urlPatterns = {"/editRequest"})
public class EditRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));

            RequestDAO requestDAO = new RequestDAO();
            RequestDetailDAO detailDAO = new RequestDetailDAO();
            CategoryDAO categoryDAO = new CategoryDAO();
            UnitConversionDao unitDao = new UnitConversionDao();
            UserDAO userDAO = new UserDAO();
            InventoryDAO inventoryDAO = new InventoryDAO();

            Request req = requestDAO.getRequestById(requestId);
            List<RequestDetail> details = detailDAO.getRequestDetailsByRequestId(requestId);
            Integer roleId = (Integer) request.getSession().getAttribute("roleId");
            boolean canEdit = roleId != null && roleId == 4 && "Từ chối".equals(req.getRequestStatus());
            if (!canEdit) {
                request.setAttribute("error", "Bạn không có quyền chỉnh sửa yêu cầu này");
            }

            List<Category> parentCategories = categoryDAO.getParentCategories();
            List<Category> subCategories = categoryDAO.getAllCategories().stream()
                    .filter(cat -> cat.getParentId() != null)
                    .collect(Collectors.toList());

            List<Inventory> inventoryList = inventoryDAO.getInventoryWithMaterialInfo();

            request.setAttribute("request", req);
            request.setAttribute("details", details);
            request.setAttribute("userName", userDAO.getUserById((Integer) request.getSession().getAttribute("userId")).getFullname());
            request.setAttribute("parentCategories", parentCategories);
            request.setAttribute("subCategories", subCategories);
            request.setAttribute("materialList", inventoryList);

            request.getRequestDispatcher("editRequest.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Lỗi khi tải form chỉnh sửa yêu cầu: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reason = request.getParameter("reason");
        String[] materialNames = request.getParameterValues("materialName");
        String[] quantities = request.getParameterValues("quantity");
        String[] unitNames = request.getParameterValues("unit");
        String[] materialConditions = request.getParameterValues("materialCondition");
        int requestId = Integer.parseInt(request.getParameter("requestId"));

        try {
            String error = null;
            if (reason == null || reason.trim().isEmpty() || reason.length() > 500) {
                error = "Lý do không được để trống và không vượt quá 500 ký tự.";
            }
            if (error == null && (materialNames == null || materialNames.length == 0)) {
                error = "Phải có ít nhất một vật tư trong yêu cầu.";
            }
            InventoryDAO inventoryDAO = new InventoryDAO();
            List<Inventory> inventoryList = inventoryDAO.getInventoryWithMaterialInfo();
            if (error == null) {
                Set<String> materialSet = new HashSet<>();
                for (int i = 0; i < materialNames.length; i++) {
                    String name = materialNames[i];
                    String quantity = quantities[i];
                    String unit = unitNames[i];
                    String condition = (materialConditions != null && materialConditions.length > i) ? materialConditions[i] : null;
                    if (name == null || name.trim().isEmpty() || name.length() > 100) {
                        error = "Tên vật tư ở dòng " + (i + 1) + " không hợp lệ.";
                        break;
                    }
                    String uniqueKey = name.trim().toLowerCase() + "-" + (condition != null ? condition.trim().toLowerCase() : "");
                    if (materialSet.contains(uniqueKey)) {
                        error = "Vật tư ở dòng " + (i + 1) + " bị trùng. Vui lòng không chọn 2 lần cùng một vật tư và tình trạng.";
                        break;
                    }
                    materialSet.add(uniqueKey);
                    if (quantity == null || !quantity.matches("^[0-9]+$")) {
                        error = "Số lượng ở dòng " + (i + 1) + " phải là số nguyên dương.";
                        break;
                    }
                    int q = Integer.parseInt(quantity);
                    if (q <= 0 || q > 999999) {
                        error = "Số lượng ở dòng " + (i + 1) + " phải trong khoảng từ 1 đến 999,999.";
                        break;
                    }
                    Inventory matched = null;
                    for (Inventory inv : inventoryList) {
                        if (inv.getMaterialName().equalsIgnoreCase(name.trim()) && inv.getMaterialCondition().equalsIgnoreCase(condition.trim())) {
                            matched = inv;
                            break;
                        }
                    }
                    
                    if (matched == null) {
                        error = "Không tìm thấy vật tư: " + name + " với tình trạng: " + condition;
                        break;
                    }
                    if (unit == null || unit.trim().isEmpty() || unit.length() > 20) {
                        error = "Đơn vị ở dòng " + (i + 1) + " không hợp lệ.";
                        break;
                    }
                    if (condition == null || condition.trim().isEmpty()) {
                        error = "Điều kiện vật tư ở dòng " + (i + 1) + " không được để trống.";
                        break;
                    }
                }
            }
            if (error != null) {
                request.setAttribute("error", error);
                doGet(request, response);
                return;
            }

            RequestDAO requestDAO = new RequestDAO();
            Request req = requestDAO.getRequestById(requestId);
            if (!"Từ chối".equals(req.getRequestStatus())) {
                request.getSession().setAttribute("error", "Chỉ yêu cầu bị từ chối mới được chỉnh sửa!");
                response.sendRedirect("requestList.jsp");
                return;
            }

            requestDAO.updateRequestReasonAndRecipient(
                requestId,
                reason,
                request.getParameter("recipientName"),
                request.getParameter("deliveryAddress"),
                request.getParameter("contactPerson"),
                request.getParameter("contactPhone")
            );
            requestDAO.updateRequestStatus(requestId, "Chờ duyệt");

            RequestHistory history = new RequestHistory();
            history.setRequestId(requestId);
            history.setChangedBy((Integer) request.getSession().getAttribute("userId"));
            history.setOldStatus("Từ chối");
            history.setNewStatus("Chờ duyệt");
            history.setAction("Gửi lại yêu cầu");
            history.setChangeReason(reason);
            history.setDirectorNote(null);
            
            for (int i = 0; i < materialNames.length; i++) {
                String name = materialNames[i];
                String condition = materialConditions[i];
                int qty = Integer.parseInt(quantities[i]);
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

            RequestDetailDAO detailDAO = new RequestDetailDAO();
            detailDAO.deleteAllDetailsByRequestId(requestId);

            List<RequestDetail> details = new ArrayList<>();
            for (int i = 0; i < materialNames.length; i++) {
                String name = materialNames[i];
                String condition = materialConditions[i];
                int qty = Integer.parseInt(quantities[i]);
                Inventory matched = null;
                for (Inventory inv : inventoryList) {
                    if (inv.getMaterialName().equalsIgnoreCase(name.trim()) && inv.getMaterialCondition().equalsIgnoreCase(condition.trim())) {
                        matched = inv;
                        break;
                    }
                }
                if (matched == null) {
                    throw new Exception("Không tìm thấy vật tư: " + name + " với tình trạng: " + condition);
                }
                RequestDetail detail = new RequestDetail();
                detail.setRequestId(requestId);
                detail.setMaterialId(matched.getMaterialId());
                detail.setMaterialName(matched.getMaterialName());
                detail.setQuantity(qty);
                detail.setWarehouseUnitId(matched.getUnitId());
                detail.setUnitName(matched.getUnitName());
                detail.setMaterialCondition(condition);
                details.add(detail);
            }

            for (RequestDetail d : details) {
                detailDAO.addRequestDetail(d);
            }

            int directorId = requestDAO.getDirectorId();
            if (directorId != -1) {
                new NotificationDAO().addNotification(directorId,
                        "Yêu cầu vật tư #" + requestId + " đã được chỉnh sửa và gửi lại. Vui lòng xem xét.",
                        requestId);
            }

            // Gửi thông báo thành công và điều hướng về RequestListServlet
            request.getSession().setAttribute("success", "Yêu cầu #" + requestId + " đã được gửi lại thành công!");
            response.sendRedirect("RequestListServlet");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            doGet(request, response); 
        }
    }
}
