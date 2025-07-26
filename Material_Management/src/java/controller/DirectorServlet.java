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

            // Tổng số đơn yêu cầu
            int totalRequests = 0;
            String sqlTotalRequests = "SELECT COUNT(*) FROM requests";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sqlTotalRequests)) {
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) totalRequests = rs.getInt(1);
                }
            } catch (Exception e) { e.printStackTrace(); }
            request.setAttribute("totalRequests", totalRequests);

            // Yêu cầu lớn chờ duyệt (ví dụ: tổng số lượng vật tư trong 1 request >= 1000)
            int bigRequestCount = 0;
            String sqlBigRequest = "SELECT COUNT(*) FROM requests r WHERE r.request_status = 'Chờ duyệt' AND r.request_id IN (SELECT request_id FROM request_details GROUP BY request_id HAVING SUM(quantity) >= 1000)";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sqlBigRequest)) {
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) bigRequestCount = rs.getInt(1);
                }
            } catch (Exception e) { e.printStackTrace(); }
            request.setAttribute("bigRequestCount", bigRequestCount);

            // Báo cáo tổng hợp (ví dụ: số lượng các request đã duyệt)
            int reportCount = 0;
            String sqlReportCount = "SELECT COUNT(*) FROM requests WHERE request_status = 'Đã duyệt'";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sqlReportCount)) {
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) reportCount = rs.getInt(1);
                }
            } catch (Exception e) { e.printStackTrace(); }
            request.setAttribute("reportCount", reportCount);

            // Tổng vật tư
            int totalItems = 0;
            String sqlTotalItems = "SELECT COUNT(*) FROM materials";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sqlTotalItems)) {
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) totalItems = rs.getInt(1);
                }
            } catch (Exception e) { e.printStackTrace(); }
            request.setAttribute("totalItems", totalItems);

            // Tổng giá trị đã mua vật tư (tổng tất cả các đơn đã duyệt, không lọc theo tháng)
            long totalPurchaseValue = 0;
            String sqlTotalPurchase = "SELECT SUM(total_amount) FROM purchase_orders WHERE approval_status = 'Approved'";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sqlTotalPurchase)) {
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) totalPurchaseValue = rs.getLong(1);
                }
            } catch (Exception e) { e.printStackTrace(); }
            request.setAttribute("totalPurchaseValue", totalPurchaseValue);

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

            // --- Lấy tham số lọc cho biểu đồ ---
            String startMonth = request.getParameter("startMonth");
            String startYear = request.getParameter("startYear");
            String endMonth = request.getParameter("endMonth");
            String endYear = request.getParameter("endYear");

            // --- Xử lý biểu đồ xuất kho ---
            java.util.Map<String, Integer> exportByMonth = new java.util.LinkedHashMap<>();
            String exportWhere = "";
            java.util.List<String> exportParams = new java.util.ArrayList<>();
            if (startMonth != null && startYear != null && endMonth != null && endYear != null) {
                String start = startYear + "-" + startMonth;
                String end = endYear + "-" + endMonth;
                exportWhere = " AND DATE_FORMAT(export_date, '%Y-%m') >= ? AND DATE_FORMAT(export_date, '%Y-%m') <= ? ";
                exportParams.add(start);
                exportParams.add(end);
            }
            String sqlExportByMonth = "SELECT DATE_FORMAT(export_date, '%Y-%m') as month, COUNT(*) as total FROM export_forms WHERE status = 'Hoàn thành'" + exportWhere + " GROUP BY month ORDER BY month ASC";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sqlExportByMonth)) {
                for (int i = 0; i < exportParams.size(); i++) {
                    ps.setString(i + 1, exportParams.get(i));
                }
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        exportByMonth.put(rs.getString("month"), rs.getInt("total"));
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
            java.util.List<String> exportMonthLabels = new java.util.ArrayList<>(exportByMonth.keySet());
            java.util.List<Integer> exportMonthValues = new java.util.ArrayList<>(exportByMonth.values());
            request.setAttribute("exportMonthLabels", exportMonthLabels);
            request.setAttribute("exportMonthValues", exportMonthValues);

            // --- Xử lý biểu đồ mua hàng ---
            java.util.Map<String, Long> purchaseByMonth = new java.util.LinkedHashMap<>();
            String purchaseWhere = "";
            java.util.List<String> purchaseParams = new java.util.ArrayList<>();
            if (startMonth != null && startYear != null && endMonth != null && endYear != null) {
                String start = startYear + "-" + startMonth;
                String end = endYear + "-" + endMonth;
                purchaseWhere = " AND DATE_FORMAT(order_date, '%Y-%m') >= ? AND DATE_FORMAT(order_date, '%Y-%m') <= ? ";
                purchaseParams.add(start);
                purchaseParams.add(end);
            }
            String sqlPurchaseByMonth = "SELECT DATE_FORMAT(order_date, '%Y-%m') as month, SUM(total_amount) as total FROM purchase_orders WHERE approval_status = 'Approved'" + purchaseWhere + " GROUP BY month ORDER BY month ASC";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sqlPurchaseByMonth)) {
                for (int i = 0; i < purchaseParams.size(); i++) {
                    ps.setString(i + 1, purchaseParams.get(i));
                }
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        purchaseByMonth.put(rs.getString("month"), rs.getLong("total"));
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
            java.util.List<String> purchaseMonthLabels = new java.util.ArrayList<>(purchaseByMonth.keySet());
            java.util.List<Long> purchaseMonthValues = new java.util.ArrayList<>(purchaseByMonth.values());
            request.setAttribute("purchaseMonthLabels", purchaseMonthLabels);
            request.setAttribute("purchaseMonthValues", purchaseMonthValues);

            // Lấy dữ liệu cho 2 bảng thống kê
            List<model.MonthStat> requestCountByMonth = requestDAO.getRequestCountByMonth();
            List<model.MonthStat> purchaseValueStatsByMonth = requestDAO.getPurchaseValueByMonth();
            request.setAttribute("requestCountByMonth", requestCountByMonth);
            request.setAttribute("purchaseValueByMonth", purchaseValueStatsByMonth);

            // Tổng số đơn mua đã duyệt
            int totalApprovedPurchases = 0;
            String sqlApprovedPurchases = "SELECT COUNT(*) FROM purchase_orders WHERE approval_status = 'Approved'";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sqlApprovedPurchases)) {
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) totalApprovedPurchases = rs.getInt(1);
                }
            } catch (Exception e) { e.printStackTrace(); }

            // Tổng số đơn xuất kho (phiếu xuất kho hoàn thành)
            int totalExportForms = 0;
            String sqlExportForms = "SELECT COUNT(*) FROM export_forms WHERE status = 'Hoàn thành'";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sqlExportForms)) {
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) totalExportForms = rs.getInt(1);
                }
            } catch (Exception e) { e.printStackTrace(); }
            request.setAttribute("totalExportForms", totalExportForms);

            // Tổng số đơn mua hàng (đã duyệt)
            int totalPurchaseOrders = 0;
            String sqlPurchaseOrders = "SELECT COUNT(*) FROM purchase_orders WHERE approval_status = 'Approved'";
            try (java.sql.Connection conn = new dal.DBContext().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sqlPurchaseOrders)) {
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) totalPurchaseOrders = rs.getInt(1);
                }
            } catch (Exception e) { e.printStackTrace(); }
            request.setAttribute("totalPurchaseOrders", totalPurchaseOrders);

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
