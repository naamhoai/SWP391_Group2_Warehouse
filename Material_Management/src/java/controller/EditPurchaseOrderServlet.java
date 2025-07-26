package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.PurchaseOrderDAO;
import dao.PurchaseOrderDetailDAO;
import dao.SupplierDAO;
import dao.MaterialDAO;
import dao.UnitDAO;
import dao.UserDAO;
import dao.NotificationDAO;
import model.PurchaseOrder;
import model.PurchaseOrderDetail;
import model.Supplier;
import model.Unit;
import model.User;
import dal.DBContext;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "EditPurchaseOrderServlet", urlPatterns = {"/editPurchaseOrder"})
public class EditPurchaseOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute("Admin");
            if (user == null) {
                user = (User) request.getSession().getAttribute("user");
            }
            if (user == null) {
                user = (User) request.getSession().getAttribute("staff");
            }
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            String orderIdStr = request.getParameter("id");
            if (orderIdStr == null || orderIdStr.isEmpty()) {
                orderIdStr = request.getParameter("orderId"); // Fallback
            }
            if (orderIdStr == null || orderIdStr.isEmpty()) {
                response.sendRedirect("purchaseOrderList?error=true&message=Không tìm thấy đơn hàng");
                return;
            }
            int orderId = Integer.parseInt(orderIdStr);
            PurchaseOrderDAO orderDAO = new PurchaseOrderDAO();
            PurchaseOrder order = orderDAO.getPurchaseOrderById(orderId);
            if (order == null) {
                response.sendRedirect("purchaseOrderList?error=true&message=Không tìm thấy đơn hàng");
                return;
            }
            
            // Debug: In ra trạng thái đơn hàng
            System.out.println("=== DEBUG: EditPurchaseOrderServlet - Order ID: " + orderId);
            System.out.println("=== DEBUG: Order Status: '" + order.getStatus() + "'");
            System.out.println("=== DEBUG: Order Approval Status: '" + order.getApprovalStatus() + "'");
            
            // Chỉ cho phép edit đơn mua khi trạng thái là "Rejected"
            if (!"Rejected".equals(order.getStatus())) {
                System.out.println("=== DEBUG: Order status is not 'Rejected', redirecting...");
                response.sendRedirect("purchaseOrderList?error=true&message=Chỉ có thể chỉnh sửa đơn hàng bị từ chối! Trạng thái hiện tại: " + order.getStatus());
                return;
            }
            // Không tự động chuyển trạng thái nữa, để user tự chọn khi nào gửi lại
            PurchaseOrderDetailDAO detailDAO = new PurchaseOrderDetailDAO();
            List<PurchaseOrderDetail> details = detailDAO.getDetailsByOrderId(orderId);
            SupplierDAO supplierDAO = new SupplierDAO();
            List<Supplier> suppliers = supplierDAO.getAllSuppliers();
            MaterialDAO materialDAO = new MaterialDAO();
            List<String> allMaterialNames = materialDAO.getAllMaterialNames();
            UnitDAO unitDAO = new UnitDAO();
            List<Unit> supplierUnits = unitDAO.getAllUnits().stream()
                .filter(u -> "Hoạt động".equals(u.getStatus()))
                .collect(java.util.stream.Collectors.toList());
            request.setAttribute("supplierUnits", supplierUnits);
            request.setAttribute("order", order);
            request.setAttribute("details", details);
            request.setAttribute("suppliers", suppliers);
            request.setAttribute("allMaterialNames", allMaterialNames);
            request.getRequestDispatcher("editPurchaseOrder.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<h2 style='color:red'>Lỗi khi tải trang chỉnh sửa đơn hàng:</h2>");
            response.getWriter().println("<div style='color:#b71c1c; background:#ffebee; padding:12px 18px; border-radius:8px; margin:12px 0; font-weight:bold;'>" + e.getMessage() + "<br><pre>" + e.toString() + "</pre></div>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        try {
            System.out.println("==== DEBUG: BẮT ĐẦU doPost EditPurchaseOrderServlet ====");
            User user = (User) request.getSession().getAttribute("Admin");
            if (user == null) {
                user = (User) request.getSession().getAttribute("user");
            }
            if (user == null) {
                user = (User) request.getSession().getAttribute("staff");
            }
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            String orderIdStr = request.getParameter("orderId");
            String note = request.getParameter("note");
            String[] materialNames = request.getParameterValues("materialName[]");
            String[] quantities = request.getParameterValues("quantity[]");
            String[] units = request.getParameterValues("unit[]");
            String[] baseUnits = request.getParameterValues("baseUnit[]");
            String[] unitPrices = request.getParameterValues("unitPrice[]");
            String supplierIdStr = request.getParameter("supplierId");
            
            System.out.println("orderIdStr=" + orderIdStr);
            System.out.println("note=" + note);
            System.out.println("materialNames=" + java.util.Arrays.toString(materialNames));
            System.out.println("quantities=" + java.util.Arrays.toString(quantities));
            System.out.println("units=" + java.util.Arrays.toString(units));
            System.out.println("baseUnits=" + java.util.Arrays.toString(baseUnits));
            System.out.println("unitPrices=" + java.util.Arrays.toString(unitPrices));
            System.out.println("supplierIdStr=" + supplierIdStr);
            if (orderIdStr == null || orderIdStr.isEmpty()) {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<h2 style='color:red'>Thiếu orderId!</h2>");
                return;
            }
            int orderId = Integer.parseInt(orderIdStr);
            if (supplierIdStr == null || supplierIdStr.isEmpty()) {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<h2 style='color:red'>Vui lòng chọn nhà cung cấp!</h2>");
                return;
            }
            int supplierId = Integer.parseInt(supplierIdStr);
            if (materialNames == null || materialNames.length == 0) {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<h2 style='color:red'>Vui lòng nhập ít nhất 1 vật tư!</h2>");
                return;
            }
            double totalAmount = 0;
            for (int i = 0; i < materialNames.length; i++) {
                if (materialNames[i] != null && !materialNames[i].trim().isEmpty()) {
                    int quantity = Integer.parseInt(quantities[i]);
                    double unitPrice = Double.parseDouble(unitPrices[i]);
                    totalAmount += quantity * unitPrice;
                }
            }
            PurchaseOrderDAO orderDAO = new PurchaseOrderDAO();
            PurchaseOrder order = orderDAO.getPurchaseOrderById(orderId);
            if (order == null) {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<h2 style='color:red'>Không tìm thấy đơn hàng!</h2>");
                return;
            }
            // Chỉ cho phép edit đơn mua khi trạng thái là "Rejected"
            if (!"Rejected".equals(order.getStatus())) {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<h2 style='color:red'>Chỉ có thể chỉnh sửa đơn hàng bị từ chối!</h2>");
                return;
            }
            
            // Cập nhật thông tin đơn hàng
            order.setSupplierId(supplierId);
            order.setNote(note);
            order.setTotalAmount(totalAmount);
            
            // Gửi lại cho giám đốc
            order.setStatus("Pending");
            order.setApprovalStatus("Pending");
            order.setRejectionReason(null);
            
            // Gửi lại thông báo cho giám đốc
            UserDAO userDAO = new UserDAO();
            Integer directorId = userDAO.getDirectorId();
            if (directorId != null) {
                NotificationDAO notificationDAO = new NotificationDAO();
                String message = "Đơn mua #" + order.getPurchaseOrderId() + " đã được gửi lại sau khi chỉnh sửa. Vui lòng phê duyệt lại.";
                notificationDAO.addPurchaseOrderNotification(directorId, message, order.getPurchaseOrderId());
            }
            
            // Cập nhật lại đơn hàng vào DB
            orderDAO.updatePurchaseOrder(order);
            PurchaseOrderDetailDAO detailDAO = new PurchaseOrderDetailDAO();
            DBContext dbContext = new DBContext();
            conn = dbContext.getConnection();
            detailDAO.deleteDetailsByOrderId(orderId, conn);
            StringBuilder errorMsg = new StringBuilder();
            for (int i = 0; i < materialNames.length; i++) {
                if (materialNames[i] != null && !materialNames[i].trim().isEmpty()) {
                    System.out.println("Insert detail: " + materialNames[i] + ", qty=" + quantities[i] + ", unit=" + units[i] + ", baseUnit=" + baseUnits[i] + ", price=" + unitPrices[i]);
                    PurchaseOrderDetail detail = new PurchaseOrderDetail();
                    detail.setPurchaseOrderId(orderId);
                    detail.setMaterialName(materialNames[i].trim());
                    detail.setQuantity(Integer.parseInt(quantities[i]));
                    detail.setUnit(units[i] != null ? units[i].trim() : "");
                    detail.setConvertedUnit(baseUnits[i] != null ? baseUnits[i].trim() : "");
                    detail.setUnitPrice(Double.parseDouble(unitPrices[i]));
                    detail.setTotalPrice(detail.getQuantity() * detail.getUnitPrice());
                    String error = detailDAO.addPurchaseOrderDetail(detail, conn);
                    if (error != null) {
                        errorMsg.append(error).append("<br>");
                    }
                }
            }
            if (errorMsg.length() > 0) {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<h2 style='color:red'>Lỗi khi lưu chi tiết vật tư:</h2>");
                response.getWriter().println("<div style='color:#b71c1c; background:#ffebee; padding:12px 18px; border-radius:8px; margin:12px 0; font-weight:bold;'>" + errorMsg.toString() + "</div>");
                return;
            }
            
            response.sendRedirect("purchaseOrderList?success=true&message=Đơn hàng đã được gửi lại cho giám đốc phê duyệt!");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<h2 style='color:red'>Lỗi khi lưu thay đổi đơn hàng:</h2>");
            response.getWriter().println("<div style='color:#b71c1c; background:#ffebee; padding:12px 18px; border-radius:8px; margin:12px 0; font-weight:bold;'>" + e.getMessage() + "<br><pre>" + e.toString() + "</pre></div>");
            return;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<h2 style='color:red'>Lỗi khi đóng kết nối DB:</h2>");
                response.getWriter().println("<div style='color:#b71c1c; background:#ffebee; padding:12px 18px; border-radius:8px; margin:12px 0; font-weight:bold;'>" + ex.getMessage() + "<br><pre>" + ex.toString() + "</pre></div>");
            }
        }
    }
} 