package controller;

import dao.*;
import model.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import dao.PurchaseOrderDetailDAO;

@WebServlet(name="PurchaseOrderListServlet", urlPatterns={"/purchaseOrderList"})
public class PurchaseOrderListServlet extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            // Lấy thông tin user từ session
            User user = (User) request.getSession().getAttribute("Admin");
            if (user == null) {
                user = (User) request.getSession().getAttribute("user");
            }
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            // Lấy tham số tìm kiếm
            String fromDate = request.getParameter("fromDate");
            String toDate = request.getParameter("toDate");
            String status = request.getParameter("status");
            String supplier = request.getParameter("supplier");
            String pageStr = request.getParameter("page");
            String export = request.getParameter("export");
            String dateRange = request.getParameter("dateRange");
            
            // Xử lý thông báo thành công từ createPurchaseOrder
            String success = request.getParameter("success");
            String orderId = request.getParameter("orderId");
            if ("true".equals(success) && orderId != null) {
                request.setAttribute("successMessage", "Đã tạo đơn mua #" + orderId + " thành công!");
            }
            java.time.LocalDate today = java.time.LocalDate.now();
            
            // Mặc định chọn tất cả nếu không có tham số dateRange
            if (dateRange == null || dateRange.isEmpty()) {
                dateRange = "all";
            } else if (!dateRange.equals("all")) {
                switch (dateRange) {
                    case "today":
                        fromDate = toDate = today.toString();
                        break;
                    case "1day":
                        fromDate = today.minusDays(1).toString();
                        toDate = today.toString();
                        break;
                    case "1week":
                        fromDate = today.minusWeeks(1).toString();
                        toDate = today.toString();
                        break;
                    case "1month":
                        fromDate = today.minusMonths(1).toString();
                        toDate = today.toString();
                        break;
                    case "custom":
                        // Sử dụng fromDate và toDate từ form nếu có
                        if (fromDate == null || fromDate.isEmpty()) {
                            fromDate = today.toString();
                        }
                        if (toDate == null || toDate.isEmpty()) {
                            toDate = today.toString();
                        }
                        break;
                }
            }
            
            // Phân trang
            int page = 1;
            int pageSize = 10;
            if (pageStr != null && !pageStr.isEmpty()) {
                page = Integer.parseInt(pageStr);
            }
            
            // Lấy danh sách đơn mua từ DAO
            PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
            List<PurchaseOrder> purchaseOrders = purchaseOrderDAO.getAllPurchaseOrders(fromDate, toDate, status, supplier);
            
            // Sau khi lấy purchaseOrders, nạp details cho từng order
            PurchaseOrderDetailDAO detailDAO = new PurchaseOrderDetailDAO();
            for (PurchaseOrder order : purchaseOrders) {
                order.setDetails(detailDAO.getDetailsByOrderId(order.getPurchaseOrderId()));
            }
            
            // Tính toán thống kê
            int totalOrders = purchaseOrders.size();
            int pendingOrders = 0;
            int approvedOrders = 0;
            int rejectedOrders = 0;
            
            for (PurchaseOrder order : purchaseOrders) {
                if ("Pending".equals(order.getStatus()) || "Chờ duyệt".equals(order.getStatus())) {
                    pendingOrders++;
                } else if ("Approved".equals(order.getStatus()) || "Đã duyệt".equals(order.getStatus())) {
                    approvedOrders++;
                } else if ("Rejected".equals(order.getStatus()) || "Từ chối".equals(order.getStatus())) {
                    rejectedOrders++;
                }
            }
            
            // Phân trang
            int totalPages = (int) Math.ceil((double) totalOrders / pageSize);
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalOrders);
            
            List<PurchaseOrder> pagedOrders = purchaseOrders.subList(startIndex, endIndex);
            
            // Xuất Excel nếu được yêu cầu
            if ("excel".equals(export)) {
                exportToExcel(response, purchaseOrders);
                return;
            }
            
            // Truyền dữ liệu sang JSP
            request.setAttribute("purchaseOrders", pagedOrders);
            request.setAttribute("userName", user.getFullname());
            request.setAttribute("roleName", user.getRole().getRolename());
            request.setAttribute("fromDate", fromDate);
            request.setAttribute("toDate", toDate);
            request.setAttribute("status", status);
            request.setAttribute("supplier", supplier);
            request.setAttribute("dateRange", dateRange);
            
            // Thống kê
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("pendingOrders", pendingOrders);
            request.setAttribute("approvedOrders", approvedOrders);
            request.setAttribute("rejectedOrders", rejectedOrders);
            
            // Lấy danh sách nhà cung cấp thực tế có trong đơn mua
            List<String> supplierNamesInOrders = purchaseOrderDAO.getSupplierNamesInOrders();
            request.setAttribute("supplierNamesInOrders", supplierNamesInOrders);
            
            // Phân trang
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
            
            request.getRequestDispatcher("purchaseOrderList.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Lỗi khi tải danh sách đơn mua</h1>");
            response.getWriter().println("<p><strong>Lỗi:</strong> " + e.getMessage() + "</p>");
            response.getWriter().println("<p><strong>Chi tiết:</strong></p>");
            response.getWriter().println("<pre>");
            e.printStackTrace(response.getWriter());
            response.getWriter().println("</pre>");
            response.getWriter().println("<a href='homepage.jsp'>Quay về trang chủ</a>");
            response.getWriter().println("</body></html>");
        }
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String orderIdStr = request.getParameter("orderId");
        
        if (action != null && orderIdStr != null) {
            try {
                int orderId = Integer.parseInt(orderIdStr);
                PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
                String reason = request.getParameter("reason");
                if ("approve".equals(action)) {
                    // Xử lý duyệt đơn
                    PurchaseOrder order = purchaseOrderDAO.getPurchaseOrderById(orderId);
                    if (order != null) {
                        order.setStatus("Approved");
                        order.setApprovalStatus("Approved");
                        order.setRejectionReason(null);
                        order.setNote(reason != null ? reason : "");
                        purchaseOrderDAO.updatePurchaseOrder(order);
                        // --- BẮT ĐẦU: Cập nhật inventory ---
                        StringBuilder debugLog = new StringBuilder();
                        debugLog.append("DEBUG: Duyệt đơn mua #").append(orderId).append("\n");
                        PurchaseOrderDetailDAO detailDAO = new PurchaseOrderDetailDAO();
                        List<PurchaseOrderDetail> details = detailDAO.getDetailsByOrderId(orderId);
                        InventoryDAO inventoryDAO = new InventoryDAO();
                        MaterialDAO materialDAO = new MaterialDAO();
                        UnitConversionDao unitConversionDao = new UnitConversionDao();
                        for (PurchaseOrderDetail detail : details) {
                            int materialId = detail.getMaterialId();
                            String materialName = detail.getMaterialName();
                            String condition = detail.getMaterialCondition() != null ? detail.getMaterialCondition() : "Mới";
                            int quantity = detail.getQuantity();
                            String baseUnit = detail.getBaseUnit(); // đơn vị gốc
                            String unit = detail.getUnit(); // đơn vị trên đơn mua
                            double unitPrice = detail.getUnitPrice();
                            debugLog.append("Material: ").append(materialName).append(", materialId=").append(materialId).append(", quantity=").append(quantity).append(", unit=").append(unit).append(", baseUnit=").append(baseUnit).append("\n");
                            if (materialId <= 0 && materialName != null && !materialName.trim().isEmpty()) {
                                materialId = materialDAO.getMaterialIdByName(materialName.trim());
                                debugLog.append("  -> Tìm materialId theo tên: ").append(materialId).append("\n");
                            }
                            // Quy đổi về đơn vị gốc
                            int supplierUnitId = unitConversionDao.getUnitIdByName(unit);
                            int warehouseUnitId = unitConversionDao.getUnitIdByName(baseUnit);
                            String conversionFactorStr = unitConversionDao.getOldConversionValue(warehouseUnitId, supplierUnitId);
                            double conversionFactor = 1.0;
                            if (conversionFactorStr != null && !conversionFactorStr.isEmpty()) {
                                try {
                                    conversionFactor = Double.parseDouble(conversionFactorStr);
                                } catch (NumberFormatException e) {}
                            }
                            int quantityInBaseUnit = (int) Math.round(quantity * conversionFactor);
                            debugLog.append("  -> supplierUnitId: ").append(supplierUnitId).append(", warehouseUnitId: ").append(warehouseUnitId).append("\n");
                            debugLog.append("  -> conversionFactorStr: ").append(conversionFactorStr).append(", conversionFactor: ").append(conversionFactor).append("\n");
                            debugLog.append("  -> Quy đổi: ").append(quantity).append(" ").append(unit).append(" x ").append(conversionFactor).append(" = ").append(quantityInBaseUnit).append(" ").append(baseUnit).append("\n");
                            if (materialId > 0) {
                                try {
                                    // Gọi update inventory và log affectedRows
                                    int affectedRows = inventoryDAO.addOrUpdateInventoryWithResult(materialId, materialName, quantityInBaseUnit, baseUnit, unitPrice);
                                    debugLog.append("  -> Đã cập nhật tồn kho, affectedRows = ").append(affectedRows).append("\n");
                                } catch (Exception ex) {
                                    debugLog.append("  -> LỖI cập nhật tồn kho: ").append(ex.getMessage()).append("\n");
                                }
                            } else {
                                debugLog.append("  -> KHÔNG tìm được materialId, bỏ qua\n");
                            }
                        }
                        request.getSession().setAttribute("debugLog", debugLog.toString());
                        // --- KẾT THÚC: Cập nhật inventory ---
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\": true, \"message\": \"Đã duyệt đơn hàng thành công\"}");
                    } else {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\": false, \"message\": \"Không tìm thấy đơn hàng\"}");
                    }
                } else if ("reject".equals(action)) {
                    // Xử lý từ chối đơn
                    PurchaseOrder order = purchaseOrderDAO.getPurchaseOrderById(orderId);
                    if (order != null) {
                        order.setStatus("Rejected");
                        order.setApprovalStatus("Rejected");
                        order.setRejectionReason(reason != null ? reason : "");
                        purchaseOrderDAO.updatePurchaseOrder(order);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\": true, \"message\": \"Đã từ chối đơn hàng\"}");
                    } else {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\": false, \"message\": \"Không tìm thấy đơn hàng\"}");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": false, \"message\": \"Có lỗi xảy ra: " + e.getMessage() + "\"}");
            }
        } else {
            doGet(request, response);
        }
    }
    
    private void exportToExcel(HttpServletResponse response, List<PurchaseOrder> orders) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=purchase_orders.xls");
        
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1'>");
        sb.append("<tr><th>ID</th><th>Người tạo</th><th>Vai trò</th><th>Ngày tạo</th><th>Nhà cung cấp</th><th>Tổng tiền</th><th>Trạng thái</th></tr>");
        
        for (PurchaseOrder order : orders) {
            sb.append("<tr>");
            sb.append("<td>").append(order.getPurchaseOrderId()).append("</td>");
            sb.append("<td>").append(order.getCreatorName()).append("</td>");
            sb.append("<td>").append(order.getCreatorRoleName()).append("</td>");
            sb.append("<td>").append(order.getOrderDate()).append("</td>");
            sb.append("<td>").append(order.getSupplierName()).append("</td>");
            sb.append("<td>").append(order.getTotalAmount()).append("</td>");
            sb.append("<td>").append(order.getStatus()).append("</td>");
            sb.append("</tr>");
        }
        
        sb.append("</table>");
        response.getWriter().write(sb.toString());
    }
} 