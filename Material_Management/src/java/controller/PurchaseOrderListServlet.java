package controller;

import dao.*;
import model.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import dao.PurchaseOrderDetailDAO;
import java.nio.charset.StandardCharsets;

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
            String sortOrder = request.getParameter("sortOrder");
            if (sortOrder == null || sortOrder.isEmpty()) sortOrder = "desc";
            
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
            List<PurchaseOrder> purchaseOrders = purchaseOrderDAO.getAllPurchaseOrders(fromDate, toDate, status, supplier, sortOrder);
            
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
                        order.setStatus("Completed");
                        order.setApprovalStatus("Approved");
                        order.setRejectionReason(null);
                        order.setNote(reason != null ? reason : "");
                        purchaseOrderDAO.updatePurchaseOrder(order);
                        // --- Cập nhật supplier nếu chưa hợp tác ---
                        SupplierDAO supplierDAO = new SupplierDAO();
                        supplierDAO.activateSupplierIfInactive(order.getSupplierId(), new java.sql.Date(System.currentTimeMillis()));
                        // --- BẮT ĐẦU: Cập nhật inventory ---
                        try (java.sql.Connection conn = new dal.DBContext().getConnection()) {
                            PurchaseOrderDetailDAO detailDAO = new PurchaseOrderDetailDAO(conn);
                            java.util.List<PurchaseOrderDetail> details = detailDAO.getDetailsByOrderId(orderId);
                            InventoryDAO inventoryDAO = new InventoryDAO(conn);
                            UnitConversionDao unitConversionDao = new UnitConversionDao(conn);
                            StringBuilder inventoryError = new StringBuilder();
                            StringBuilder debugInfo = new StringBuilder();
                            for (PurchaseOrderDetail detail : details) {
                                int materialId = detail.getMaterialId();
                                int quantity = detail.getQuantity();
                                String unit = detail.getUnit();
                                String baseUnit = detail.getConvertedUnit();
                                double unitPrice = detail.getUnitPrice();
                                int supplierUnitId = unitConversionDao.getUnitIdByName(unit);
                                int warehouseUnitId = unitConversionDao.getUnitIdByName(baseUnit);
                                double conversionFactor = unitConversionDao.getConversionFactor(supplierUnitId, warehouseUnitId);
                                if (conversionFactor <= 0) conversionFactor = 1.0; // Nếu không tìm thấy hệ số quy đổi, mặc định là 1
                                int quantityInBaseUnit = (int) Math.round(quantity * conversionFactor);
                                String debugLine = "[DEBUG] ID: " + materialId + ", quantity: " + quantity + ", unit: " + unit + ", baseUnit: " + baseUnit + ", conversionFactor: " + conversionFactor + ", quantityInBaseUnit: " + quantityInBaseUnit + ", unitPrice: " + unitPrice + "<br>";
                                debugInfo.append(debugLine);
                                if (materialId > 0) {
                                    try {
                                        int affectedRows = inventoryDAO.addOrUpdateInventoryWithResult(
                                            materialId,
                                            order.getSupplierId(),
                                            detail.getMaterialName(),
                                            quantityInBaseUnit,
                                            "Mới",
                                            unitPrice
                                        );
                                        if (affectedRows == 0) {
                                            inventoryError.append("Không thể cập nhật tồn kho cho vật tư ID: ").append(materialId)
                                                .append(", Số lượng: ").append(quantityInBaseUnit).append(", Tình trạng: Mới<br>");
                                        }
                                    } catch (Exception ex) {
                                        inventoryError.append("Lỗi cập nhật tồn kho cho vật tư ID: ").append(materialId)
                                            .append(", lỗi: ").append(ex.getMessage()).append("<br>");
                                    }
                                }
                            }
                            if (inventoryError.length() > 0 || debugInfo.length() > 0) {
                                response.setContentType("text/html;charset=UTF-8");
                                response.getWriter().println("<h2 style='color:red'>Lỗi khi cập nhật tồn kho:</h2>");
                                response.getWriter().println("<div style='color:#b71c1c; background:#ffebee; padding:12px 18px; border-radius:8px; margin:12px 0; font-weight:bold;'>" + inventoryError.toString() + "</div>");
                                response.getWriter().println("<h3>Thông tin debug từng vật tư:</h3>");
                                response.getWriter().println("<div style='color:#333; background:#f5f5f5; padding:12px 18px; border-radius:8px; margin:12px 0;'>" + debugInfo.toString() + "</div>");
                                return;
                            }
                        }
                        // --- KẾT THÚC: Cập nhật inventory ---
                        response.sendRedirect("purchaseOrderList?msg=approved");
                        return;
                    } else {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\": false, \"message\": \"Không tìm thấy đơn hàng\"}");
                    }
                } else if ("reject".equals(action)) {
                    // Xử lý từ chối đơn
                    PurchaseOrder order = purchaseOrderDAO.getPurchaseOrderById(orderId);
                    if (order != null) {
                        order.setStatus("Rejected"); // Đảm bảo cập nhật trạng thái
                        order.setApprovalStatus("Rejected");
                        order.setRejectionReason(reason != null ? reason : "");
                        purchaseOrderDAO.updatePurchaseOrder(order);
                        response.sendRedirect("purchaseOrderList?msg=rejected");
                        return;
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