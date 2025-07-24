package controller;

import dao.NotificationDAO;
import dao.RequestDAO;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Notification;
import model.Request;
import model.User;

@WebServlet(name = "DirectorServlet", urlPatterns = {"/director/*"})
public class DirectorServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");

            if (userId != null) {
                UserDAO userDAO = new UserDAO();
                User user = userDAO.getUserById(userId);

                if (user != null) {
                    request.setAttribute("user", user);

                    NotificationDAO notificationDAO = new NotificationDAO();
                    List<Notification> notifications = notificationDAO.getDirectorNotifications(userId);
                    request.setAttribute("notifications", notifications);
                }
            } else {
                System.out.println("Warning: userId from session is null.");
            }

            // Lấy danh sách yêu cầu chờ duyệt
            RequestDAO requestDAO = new RequestDAO();
            List<Request> requests = requestDAO.getPendingRequests();
            request.setAttribute("requests", requests);

            // Lấy tổng số vật tư trong kho
            dao.InventoryDAO inventoryDAO = new dao.InventoryDAO();
            java.util.List<model.Inventory> inventories = inventoryDAO.getInventoryWithMaterialInfo();
            java.util.Set<Integer> materialIds = new java.util.HashSet<>();
            long totalInventoryValue = 0;
            for (model.Inventory inv : inventories) {
                materialIds.add(inv.getMaterialId());
                int price = Math.max(0, inv.getPrice());
                int qty = Math.max(0, inv.getQuantityOnHand());
                totalInventoryValue += (long) qty * price;
            }
            int totalMaterialTypes = materialIds.size();
            request.setAttribute("totalItems", totalMaterialTypes);
            request.setAttribute("totalInventoryValue", totalInventoryValue);

            // Tổng giá trị mua trong tháng hiện tại
            java.time.YearMonth now = java.time.YearMonth.now();
            String monthStr = now.toString(); // yyyy-MM
            long totalPurchaseValueThisMonth = 0;
            String sql = "SELECT SUM(pod.quantity * pod.price) as total FROM purchase_orders po JOIN purchase_order_details pod ON po.purchase_order_id = pod.purchase_order_id WHERE DATE_FORMAT(po.order_date, '%Y-%m') = ?";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, monthStr);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        totalPurchaseValueThisMonth = rs.getLong("total");
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
            request.setAttribute("totalPurchaseValueThisMonth", totalPurchaseValueThisMonth);

            // Số đơn yêu cầu mua trong tháng
            int totalPurchaseRequestsThisMonth = 0;
            String sql2 = "SELECT COUNT(*) FROM requests WHERE request_status = 'Chờ duyệt' AND DATE_FORMAT(created_at, '%Y-%m') = ?";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sql2)) {
                ps.setString(1, monthStr);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        totalPurchaseRequestsThisMonth = rs.getInt(1);
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
            request.setAttribute("totalPurchaseRequestsThisMonth", totalPurchaseRequestsThisMonth);

            // Số đơn đã duyệt trong tháng
            int totalApprovedRequestsThisMonth = 0;
            String sql3 = "SELECT COUNT(*) FROM requests WHERE request_status = 'Đã duyệt' AND DATE_FORMAT(created_at, '%Y-%m') = ?";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sql3)) {
                ps.setString(1, monthStr);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        totalApprovedRequestsThisMonth = rs.getInt(1);
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
            request.setAttribute("totalApprovedRequestsThisMonth", totalApprovedRequestsThisMonth);

            // Biểu đồ số lượng yêu cầu mua theo từng tháng (12 tháng gần nhất)
            java.util.Map<String, Integer> requestByMonth = new java.util.LinkedHashMap<>();
            String sql4 = "SELECT DATE_FORMAT(created_at, '%Y-%m') as month, COUNT(*) as total FROM requests WHERE request_status = 'Chờ duyệt' OR request_status = 'Đã duyệt' GROUP BY month ORDER BY month DESC LIMIT 12";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sql4)) {
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        requestByMonth.put(rs.getString("month"), rs.getInt("total"));
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
            java.util.List<String> requestMonthLabels = new java.util.ArrayList<>(requestByMonth.keySet());
            java.util.List<Integer> requestMonthValues = new java.util.ArrayList<>(requestByMonth.values());
            java.util.Collections.reverse(requestMonthLabels);
            java.util.Collections.reverse(requestMonthValues);
            request.setAttribute("requestMonthLabels", requestMonthLabels);
            request.setAttribute("requestMonthValues", requestMonthValues);

            // Biểu đồ giá trị mua hàng theo từng tháng (12 tháng gần nhất)
            java.util.Map<String, Long> purchaseValueByMonth = new java.util.LinkedHashMap<>();
            String sql5 = "SELECT DATE_FORMAT(po.order_date, '%Y-%m') as month, SUM(pod.quantity * pod.price) as total FROM purchase_orders po JOIN purchase_order_details pod ON po.purchase_order_id = pod.purchase_order_id GROUP BY month ORDER BY month DESC LIMIT 12";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sql5)) {
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        purchaseValueByMonth.put(rs.getString("month"), rs.getLong("total"));
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
            java.util.List<String> purchaseMonthLabels = new java.util.ArrayList<>(purchaseValueByMonth.keySet());
            java.util.List<Long> purchaseMonthValues = new java.util.ArrayList<>(purchaseValueByMonth.values());
            java.util.Collections.reverse(purchaseMonthLabels);
            java.util.Collections.reverse(purchaseMonthValues);
            request.setAttribute("purchaseMonthLabels", purchaseMonthLabels);
            request.setAttribute("purchaseMonthValues", purchaseMonthValues);

            // Lấy dữ liệu cho 2 bảng thống kê
            List<model.MonthStat> requestCountByMonth = requestDAO.getRequestCountByMonth();
            List<model.MonthStat> purchaseValueStatsByMonth = requestDAO.getPurchaseValueByMonth();
            request.setAttribute("requestCountByMonth", requestCountByMonth);
            request.setAttribute("purchaseValueByMonth", purchaseValueStatsByMonth);

            request.getRequestDispatcher("directorDashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Lỗi khi tải dashboard: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
    }
}
