/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.PurchaseOrder;
import model.PurchaseOrderDetail;
import model.User;
import model.Supplier;
import model.Inventory;
import dao.PurchaseOrderDAO;
import dao.PurchaseOrderDetailDAO;
import dao.SupplierDAO;
import dao.InventoryDAO;
import dao.MaterialDAO;
import dal.DBContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import com.google.gson.Gson;
import dao.NotificationDAO;
import dao.UserDAO;
import dao.UnitDAO;
import model.Material;
import java.sql.Connection;

@WebServlet(name="CreatePurchaseOrderServlet", urlPatterns={"/createPurchaseOrder"})
public class CreatePurchaseOrderServlet extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            // Kiểm tra user đăng nhập
            User user = (User) request.getSession().getAttribute("Admin");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            // Lấy ngày hiện tại từ server
            String createdDate = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
            request.setAttribute("createdDate", createdDate);
            
            // Set thông tin user vào request
            request.setAttribute("currentUser", user);
            
            // Lấy danh sách nhà cung cấp
            SupplierDAO supplierDAO = new SupplierDAO();
            List<Supplier> suppliers = supplierDAO.getAllSuppliers();
            if (suppliers == null) suppliers = new ArrayList<>(); // Đảm bảo không null
            request.setAttribute("suppliers", suppliers);

            // Lấy danh sách vật tư từ MaterialDAO (tên, đơn vị, đơn giá)
            MaterialDAO materialDAO = new MaterialDAO();
            List<model.Material> materialList = materialDAO.getMaterialsForAdmin(null, null, null, null, 1, 1000, "name", "asc");
            request.setAttribute("materialList", materialList);
            // Truyền materialListJson sang JSP
            String materialListJson = new Gson().toJson(materialList != null ? materialList : new ArrayList<>());
            request.setAttribute("materialListJson", materialListJson);

            // Lấy danh sách vật tư từ inventory (nếu cần)
            InventoryDAO inventoryDAO = new InventoryDAO();
            List<model.Inventory> inventoryList = inventoryDAO.getInventoryWithMaterialInfo();
            request.setAttribute("inventoryList", inventoryList);

            // Lấy danh sách đơn vị (unit_name) có status='Hoạt động' (bất kể is_system_unit)
            UnitDAO unitDAO = new UnitDAO();
            List<model.Unit> supplierUnits = unitDAO.getAllUnits().stream()
                .filter(u -> "Hoạt động".equals(u.getStatus()))
                .collect(java.util.stream.Collectors.toList());
            request.setAttribute("supplierUnits", supplierUnits);

            request.getRequestDispatcher("createPurchaseOrder.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Lỗi: " + e.getMessage());
        }
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Kiểm tra user đăng nhập
            User user = (User) request.getSession().getAttribute("Admin");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Lấy dữ liệu từ form
            String note = request.getParameter("note");
            String[] materialNames = request.getParameterValues("materialName[]");
            String[] quantities = request.getParameterValues("quantity[]");
            String[] units = request.getParameterValues("unit[]");
            String[] unitPrices = request.getParameterValues("unitPrice[]");
            String supplierIdStr = request.getParameter("supplierId");
            if (supplierIdStr == null || supplierIdStr.isEmpty()) {
                throw new Exception("Vui lòng chọn nhà cung cấp");
            }
            int supplierId = Integer.parseInt(supplierIdStr);

            // Validate dữ liệu
            if (materialNames == null || materialNames.length == 0) {
                throw new Exception("Vui lòng nhập ít nhất 1 vật tư");
            }

            System.out.println("=== DEBUG: Tạo đơn mua ===");
            System.out.println("Số lượng vật tư: " + materialNames.length);
            
            // Debug chi tiết từng tham số
            System.out.println("=== DEBUG: MATERIAL NAMES ===");
            for (int i = 0; i < materialNames.length; i++) {
                System.out.println("materialNames[" + i + "]: '" + materialNames[i] + "'");
            }
            System.out.println("=== DEBUG: QUANTITIES ===");
            for (int i = 0; i < quantities.length; i++) {
                System.out.println("quantities[" + i + "]: '" + quantities[i] + "'");
            }
            System.out.println("=== DEBUG: UNITS ===");
            for (int i = 0; i < units.length; i++) {
                System.out.println("units[" + i + "]: '" + units[i] + "'");
            }
            System.out.println("=== DEBUG: UNIT PRICES ===");
            for (int i = 0; i < unitPrices.length; i++) {
                System.out.println("unitPrices[" + i + "]: '" + unitPrices[i] + "'");
            }
            
            int validCount = 0;
            double totalAmount = 0;

            // Tính tổng tiền
            for (int i = 0; i < materialNames.length; i++) {
                if (materialNames[i] != null && !materialNames[i].trim().isEmpty()) {
                    int quantity = Integer.parseInt(quantities[i]);
                    double unitPrice = Double.parseDouble(unitPrices[i]);
                    totalAmount += quantity * unitPrice;
                    validCount++;
                    System.out.println("Vật tư " + (i+1) + ": " + materialNames[i] + " - SL: " + quantities[i]);
                } else {
                    System.out.println("Dòng " + (i+1) + ": TRỐNG");
                }
            }
            System.out.println("=== DEBUG: Có " + validCount + " dòng có dữ liệu ===");

            // Tạo đơn mua
            PurchaseOrder order = new PurchaseOrder();
            order.setSupplierId(supplierId);
            order.setUserId(user.getUser_id());
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setStatus("Pending");
            order.setNote(note != null ? note : "");
            order.setTotalAmount(totalAmount);
            order.setApprovalStatus("Pending");

            // Lưu đơn mua
            Connection conn = null;
            int insertedCount = 0;
            StringBuilder errorMsg = new StringBuilder();
            try {
                conn = new DBContext().getConnection();
                PurchaseOrderDAO orderDAO = new PurchaseOrderDAO(conn);
                PurchaseOrderDetailDAO detailDAO = new PurchaseOrderDetailDAO();
                // Lưu đơn mua
                orderDAO.addPurchaseOrder(order); // id sẽ được set vào order
                for (int i = 0; i < materialNames.length; i++) {
                    String matName = (materialNames[i] != null) ? materialNames[i].trim() : "";
                    String qty = (quantities != null && i < quantities.length) ? quantities[i] : "";
                    String unit = (units != null && i < units.length) ? units[i] : "";
                    String unitPrice = (unitPrices != null && i < unitPrices.length) ? unitPrices[i] : "";

                    if (matName.isEmpty() || qty.isEmpty() || unit.isEmpty() || unitPrice.isEmpty()) {
                        System.out.println("==> Dòng " + (i+1) + " thiếu dữ liệu, bỏ qua. materialName='" + matName + "', quantity='" + qty + "', unit='" + unit + "', unitPrice='" + unitPrice + "'");
                        continue;
                    }
                    System.out.println("==> Dòng " + (i+1) + " có dữ liệu, sẽ insert");
                    PurchaseOrderDetail detail = new PurchaseOrderDetail();
                    detail.setPurchaseOrderId(order.getPurchaseOrderId());
                    detail.setMaterialName(matName);
                    detail.setQuantity(Integer.parseInt(qty));
                    detail.setUnit(unit);
                    detail.setUnitPrice(Double.parseDouble(unitPrice));
                    detail.setTotalPrice(detail.getQuantity() * detail.getUnitPrice());
                    String error = detailDAO.addPurchaseOrderDetail(detail, conn);
                    if (error != null) {
                        errorMsg.append(error).append("<br>");
                    } else {
                        insertedCount++;
                    }
                }
                System.out.println("=== DEBUG: Đã insert " + insertedCount + " dòng chi tiết ===");
                
                // KHÔNG thêm vào inventory ngay khi tạo đơn mua
                // Inventory sẽ được cập nhật khi đơn mua được duyệt
            } finally {
                if (conn != null) try { conn.close(); } catch (Exception e) {}
            }
            if (errorMsg.length() > 0) {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<h2 style='color:red'>Lỗi khi lưu chi tiết vật tư:</h2>");
                response.getWriter().println("<div style='color:#b71c1c; background:#ffebee; padding:12px 18px; border-radius:8px; margin:12px 0; font-weight:bold;'>" + errorMsg.toString() + "</div>");
                return;
            }

            // Gửi thông báo cho giám đốc
            UserDAO userDAO = new UserDAO();
            Integer directorId = userDAO.getDirectorId();
            if (directorId != null) {
                NotificationDAO notificationDAO = new NotificationDAO();
                String message = "Có đơn mua mới #" + order.getPurchaseOrderId() + " từ " + user.getFullname();
                notificationDAO.addNotification(directorId, message, order.getPurchaseOrderId());
            }

            // Chuyển hướng thành công ngay lập tức
            response.sendRedirect("purchaseOrderList?success=true&orderId=" + order.getPurchaseOrderId());
            return;

        } catch (Exception e) {
            e.printStackTrace();
            
            // Kiểm tra xem response đã được commit chưa
            if (response.isCommitted()) {
                // Nếu response đã commit, chuyển hướng về trang lỗi
                response.sendRedirect("createPurchaseOrder?error=true&message=" + java.net.URLEncoder.encode("Lỗi khi tạo đơn mua: " + e.getMessage(), "UTF-8"));
                return;
            }
            
            request.setAttribute("errorMessage", "Lỗi khi tạo đơn mua: " + e.getMessage());
            
            // Set lại thông tin user và dữ liệu cần thiết
            User user = (User) request.getSession().getAttribute("Admin");
            if (user != null) {
                request.setAttribute("currentUser", user);
            }
            
            // Lấy lại danh sách nhà cung cấp và inventory
            try {
                SupplierDAO supplierDAO = new SupplierDAO();
                List<Supplier> suppliers = supplierDAO.getAllSuppliers();
                if (suppliers == null) suppliers = new ArrayList<>();
                request.setAttribute("suppliers", suppliers);
                
                InventoryDAO inventoryDAO = new InventoryDAO();
                List<Inventory> inventoryList = inventoryDAO.getInventoryWithMaterialInfo();
                request.setAttribute("inventoryList", inventoryList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            request.getRequestDispatcher("createPurchaseOrder.jsp").forward(request, response);
        }
    }
}
