package controller;

import dao.SupplierDAO;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Supplier;
import java.util.HashMap;
import dao.PurchaseOrderDAO;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import dao.MaterialDAO;

@WebServlet(name = "SupplierListServlet", urlPatterns = {"/suppliers"})
public class SupplierListServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SupplierListServlet.class.getName());
    private final SupplierDAO supplierDAO = new SupplierDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        LOGGER.log(Level.INFO, "Processing request with action: {0}", action);

        try {
            switch (action) {
                case "list":
                    listSuppliers(request, response);
                    break;
                case "add":
                    if ("POST".equalsIgnoreCase(request.getMethod())) {
                        addSupplier(request, response);
                    } else {
                        request.getRequestDispatcher("/addSupplier.jsp").forward(request, response);
                    }
                    break;
                case "edit":
                    if ("POST".equalsIgnoreCase(request.getMethod())) {
                        updateSupplier(request, response);
                    } else {
                        showEditForm(request, response);
                    }
                    break;
                case "view":
                    viewSupplier(request, response);
                    break;
                default:
                    listSuppliers(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace(); // log ra console
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void listSuppliers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String status = request.getParameter("status");
        String keyword = request.getParameter("keyword");
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");
        String pageStr = request.getParameter("page");
        String itemsPerPageStr = request.getParameter("itemsPerPage");
        int currentPage = 1;
        int itemsPerPage = 5;
        if (pageStr != null) {
            try { currentPage = Math.max(1, Integer.parseInt(pageStr)); } catch (Exception ignored) {}
        }
        if (itemsPerPageStr != null) {
            try { itemsPerPage = Math.max(1, Integer.parseInt(itemsPerPageStr)); } catch (Exception ignored) {}
        }

        // Đếm tổng số supplier sau filter/search
        int totalSuppliers = supplierDAO.countSuppliers(keyword, status);
        int totalPages = (int) Math.ceil((double) totalSuppliers / itemsPerPage);
        if (totalPages == 0) totalPages = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        // Lấy danh sách supplier cho trang hiện tại
        List<Supplier> suppliers = supplierDAO.getSuppliersWithPaging(keyword, status, sortBy, sortOrder, currentPage, itemsPerPage);

        // Map supplierId to hasMaterialsInStock
        HashMap<Integer, Boolean> supplierMaterialStatusMap = new HashMap<>();
        for (Supplier s : suppliers) {
            supplierMaterialStatusMap.put(s.getSupplierId(), supplierDAO.hasMaterialsInStock(s.getSupplierId()));
        }

        // Tính startPage, endPage cho hiển thị phân trang (5 số)
        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);

        // Truyền các biến cho phân trang và hiển thị tổng số supplier
        List<Integer> itemsPerPageOptions = Arrays.asList(5, 10, 20, 50);
        request.setAttribute("itemsPerPageOptions", itemsPerPageOptions);
        request.setAttribute("itemsPerPage", itemsPerPage);
        request.setAttribute("totalSuppliers", totalSuppliers);

        request.setAttribute("suppliers", suppliers);
        request.setAttribute("supplierMaterialStatusMap", supplierMaterialStatusMap);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);

        request.getRequestDispatcher("/listSupplier.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Ưu tiên lấy supplierId từ attribute (do validate lỗi), nếu không có thì lấy từ parameter
            Integer id = null;
            Object attr = request.getAttribute("supplierId");
            if (attr != null) {
                if (attr instanceof Integer) {
                    id = (Integer) attr;
                } else if (attr instanceof String) {
                    id = Integer.parseInt((String) attr);
                }
            } else if (request.getParameter("id") != null) {
                id = Integer.parseInt(request.getParameter("id"));
            } else if (request.getParameter("supplierId") != null) {
                id = Integer.parseInt(request.getParameter("supplierId"));
            }
            if (id == null) throw new NumberFormatException("Missing supplierId");

            Supplier supplier = supplierDAO.getSupplierById(id);
            if (supplier != null) {
                request.setAttribute("supplier", supplier);
                request.setAttribute("statusReason", supplier.getStatusReason());
                request.getRequestDispatcher("/editSupplier.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("error", "Supplier not found");
                response.sendRedirect(request.getContextPath() + "/suppliers");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid supplier ID format: {0}", e.getMessage());
            request.getSession().setAttribute("error", "Invalid supplier ID");
            response.sendRedirect(request.getContextPath() + "/suppliers");
        }
    }

    private void addSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String supplierName = request.getParameter("supplierName");
            String contactPerson = request.getParameter("contactPerson");
            String supplierPhone = request.getParameter("supplierPhone");
            String address = request.getParameter("address");
            String status = request.getParameter("status");

            // Validate Tên nhà cung cấp
            if (supplierName == null || supplierName.trim().isEmpty()) {
                request.setAttribute("error", "Tên nhà cung cấp không được để trống");
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.getRequestDispatcher("/addSupplier.jsp").forward(request, response);
                return;
            }
            if (supplierName.trim().length() < 2) {
                request.setAttribute("error", "Tên nhà cung cấp phải có ít nhất 2 ký tự");
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.getRequestDispatcher("/addSupplier.jsp").forward(request, response);
                return;
            }
            if (supplierName.trim().replaceAll(" ", "").isEmpty()) {
                request.setAttribute("error", "Tên nhà cung cấp không được chỉ chứa khoảng trắng");
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.getRequestDispatcher("/addSupplier.jsp").forward(request, response);
                return;
            }
            if (!supplierName.trim().matches("^[\\p{L}0-9 ]+$")) {
                request.setAttribute("error", "Tên nhà cung cấp chỉ được chứa chữ, số và khoảng trắng");
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.getRequestDispatcher("/addSupplier.jsp").forward(request, response);
                return;
            }
            // Kiểm tra tên trùng
            if (supplierDAO.getAllSuppliers().stream().anyMatch(s -> s.getSupplierName().equalsIgnoreCase(supplierName.trim()))) {
                request.setAttribute("error", "Tên nhà cung cấp đã tồn tại. Vui lòng nhập tên khác.");
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.getRequestDispatcher("/addSupplier.jsp").forward(request, response);
                return;
            }

            // Validate Tên người liên hệ
            if (contactPerson == null || contactPerson.trim().isEmpty()) {
                request.setAttribute("error", "Tên người liên hệ không được để trống");
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.getRequestDispatcher("/addSupplier.jsp").forward(request, response);
                return;
            }
            if (contactPerson.trim().length() < 2) {
                request.setAttribute("error", "Tên người liên hệ phải có ít nhất 2 ký tự");
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.getRequestDispatcher("/addSupplier.jsp").forward(request, response);
                return;
            }
            if (!contactPerson.trim().matches("^[\\p{L}0-9 ]+$")) {
                request.setAttribute("error", "Tên người liên hệ chỉ được chứa chữ, số và khoảng trắng");
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.getRequestDispatcher("/addSupplier.jsp").forward(request, response);
                return;
            }
            // Nếu trùng người liên hệ thì chỉ báo lỗi nếu số điện thoại cũng trùng
            boolean duplicateContact = supplierDAO.getAllSuppliers().stream()
                .anyMatch(s -> s.getContactPerson().equalsIgnoreCase(contactPerson.trim()) && s.getSupplierPhone().equals(supplierPhone));
            if (duplicateContact) {
                request.setAttribute("error", "Người liên hệ và số điện thoại đã tồn tại");
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.getRequestDispatcher("/addSupplier.jsp").forward(request, response);
                return;
            }

            // Validate số điện thoại
            if (supplierPhone == null || !supplierPhone.matches("^[0-9]{10,11}$")) {
                request.setAttribute("error", "Số điện thoại phải là số và có 10-11 chữ số");
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.getRequestDispatcher("/addSupplier.jsp").forward(request, response);
                return;
            }

            // Validate địa chỉ
            if (address == null || address.trim().length() < 5) {
                request.setAttribute("error", "Địa chỉ phải có ít nhất 5 ký tự");
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.getRequestDispatcher("/addSupplier.jsp").forward(request, response);
                return;
            }

            // Validate trạng thái
            if (status == null || status.trim().isEmpty()) {
                status = "chưa hợp tác";
            }

            Supplier supplier = new Supplier();
            supplier.setSupplierName(supplierName.trim());
            supplier.setContactPerson(contactPerson.trim());
            supplier.setSupplierPhone(supplierPhone.trim());
            supplier.setAddress(address.trim());
            supplier.setStatus(status);

            if (supplierDAO.addSupplier(supplier)) {
                request.getSession().setAttribute("message", "Thêm nhà cung cấp thành công");
            } else {
                request.getSession().setAttribute("error", "Không thể thêm nhà cung cấp");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in addSupplier: {0}", e.getMessage());
            request.getSession().setAttribute("error", "Lỗi khi thêm nhà cung cấp: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/suppliers");
    }

    private void updateSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get parameters
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            String supplierName = request.getParameter("supplierName");
            String contactPerson = request.getParameter("contactPerson");
            String supplierPhone = request.getParameter("supplierPhone");
            String address = request.getParameter("address");
            String status = request.getParameter("status");
            String statusReason = request.getParameter("statusReason");

            LOGGER.log(Level.INFO, "Nhận được request cập nhật supplier - ID: {0}, Name: {1}, Status: {2}",
                new Object[]{supplierId, supplierName, status});

            // Validate tên nhà cung cấp
            if (supplierName == null || supplierName.trim().isEmpty()) {
                request.setAttribute("error", "Tên nhà cung cấp không được để trống");
                request.setAttribute("supplierId", supplierId);
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.setAttribute("statusReason", statusReason);
                showEditForm(request, response);
                return;
            }
            if (supplierName.trim().length() < 2) {
                request.setAttribute("error", "Tên nhà cung cấp phải có ít nhất 2 ký tự");
                request.setAttribute("supplierId", supplierId);
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.setAttribute("statusReason", statusReason);
                showEditForm(request, response);
                return;
            }
            if (supplierName.trim().replaceAll(" ", "").isEmpty()) {
                request.setAttribute("error", "Tên nhà cung cấp không được chỉ chứa khoảng trắng");
                request.setAttribute("supplierId", supplierId);
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.setAttribute("statusReason", statusReason);
                showEditForm(request, response);
                return;
            }
            if (!supplierName.trim().matches("^[\\p{L}0-9 ]+$")) {
                request.setAttribute("error", "Tên nhà cung cấp chỉ được chứa chữ, số và khoảng trắng");
                request.setAttribute("supplierId", supplierId);
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.setAttribute("statusReason", statusReason);
                showEditForm(request, response);
                return;
            }
            // Kiểm tra tên trùng (trừ chính nó)
            if (supplierDAO.getAllSuppliers().stream().anyMatch(s -> s.getSupplierName().equalsIgnoreCase(supplierName.trim()) && s.getSupplierId() != supplierId)) {
                request.setAttribute("error", "Tên nhà cung cấp đã tồn tại. Vui lòng nhập tên khác.");
                request.setAttribute("supplierId", supplierId);
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.setAttribute("statusReason", statusReason);
                showEditForm(request, response);
                return;
            }

            // Validate tên người liên hệ
            if (contactPerson == null || contactPerson.trim().isEmpty()) {
                request.setAttribute("error", "Tên người liên hệ không được để trống");
                request.setAttribute("supplierId", supplierId);
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.setAttribute("statusReason", statusReason);
                showEditForm(request, response);
                return;
            }
            if (contactPerson.trim().length() < 2) {
                request.setAttribute("error", "Tên người liên hệ phải có ít nhất 2 ký tự");
                request.setAttribute("supplierId", supplierId);
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.setAttribute("statusReason", statusReason);
                showEditForm(request, response);
                return;
            }
            if (!contactPerson.trim().matches("^[\\p{L}0-9 ]+$")) {
                request.setAttribute("error", "Tên người liên hệ chỉ được chứa chữ, số và khoảng trắng");
                request.setAttribute("supplierId", supplierId);
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.setAttribute("statusReason", statusReason);
                showEditForm(request, response);
                return;
            }
            // Nếu trùng người liên hệ thì chỉ báo lỗi nếu số điện thoại cũng trùng (trừ chính nó)
            boolean duplicateContact = supplierDAO.getAllSuppliers().stream()
                .anyMatch(s -> s.getContactPerson().equalsIgnoreCase(contactPerson.trim()) && s.getSupplierPhone().equals(supplierPhone) && s.getSupplierId() != supplierId);
            if (duplicateContact) {
                request.setAttribute("error", "Người liên hệ và số điện thoại đã tồn tại");
                request.setAttribute("supplierId", supplierId);
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.setAttribute("statusReason", statusReason);
                showEditForm(request, response);
                return;
            }

            // Validate số điện thoại
            if (supplierPhone == null || !supplierPhone.matches("^[0-9]{10,11}$")) {
                request.setAttribute("error", "Số điện thoại phải là số và có 10-11 chữ số");
                request.setAttribute("supplierId", supplierId);
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.setAttribute("statusReason", statusReason);
                showEditForm(request, response);
                return;
            }

            // Validate địa chỉ
            if (address == null || address.trim().length() < 5) {
                request.setAttribute("error", "Địa chỉ phải có ít nhất 5 ký tự");
                request.setAttribute("supplierId", supplierId);
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.setAttribute("statusReason", statusReason);
                showEditForm(request, response);
                return;
            }

            // Validate trạng thái
            if (status == null || (!status.equals("active") && !status.equals("inactive") && !status.equals("terminated"))) {
                request.setAttribute("error", "Trạng thái không hợp lệ");
                request.setAttribute("supplierId", supplierId);
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("contactPerson", contactPerson);
                request.setAttribute("supplierPhone", supplierPhone);
                request.setAttribute("address", address);
                request.setAttribute("status", status);
                request.setAttribute("statusReason", statusReason);
                showEditForm(request, response);
                return;
            }

            // Validate lý do khi chuyển trạng thái
            Supplier oldSupplier = supplierDAO.getSupplierById(supplierId);
            String oldStatus = oldSupplier != null ? oldSupplier.getStatus() : null;
            System.out.println("DEBUG: oldStatus=" + oldStatus + ", newStatus=" + status + ", statusReason=" + statusReason);
            if (("inactive".equals(oldStatus) && "active".equals(status)) ||
                ("active".equals(oldStatus) && "terminated".equals(status))) {
                if (statusReason == null || statusReason.trim().length() < 5) {
                    request.setAttribute("error", "Lý do phải có ít nhất 5 ký tự khi chuyển trạng thái.");
                    request.setAttribute("supplierId", supplierId);
                    request.setAttribute("supplierName", supplierName);
                    request.setAttribute("contactPerson", contactPerson);
                    request.setAttribute("supplierPhone", supplierPhone);
                    request.setAttribute("address", address);
                    request.setAttribute("status", status);
                    request.setAttribute("statusReason", statusReason);
                    showEditForm(request, response);
                    return;
                }
            }

            // Create supplier object
            Supplier supplier = new Supplier();
            supplier.setSupplierId(supplierId);
            supplier.setSupplierName(supplierName.trim());
            supplier.setContactPerson(contactPerson.trim());
            supplier.setSupplierPhone(supplierPhone.trim());
            supplier.setAddress(address.trim());
            supplier.setStatus(status);
            supplier.setStatusReason(statusReason != null ? statusReason.trim() : null);

            LOGGER.log(Level.INFO, "Đang gửi request cập nhật đến DAO - ID: {0}", supplierId);

            // Update supplier
            if (supplierDAO.updateSupplier(supplier)) {
                // Cập nhật trạng thái vật tư theo trạng thái supplier và category
                MaterialDAO materialDAO = new MaterialDAO();
                try {
                    materialDAO.updateMaterialsStatusBySupplier(supplierId, status);
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật trạng thái vật tư theo supplier: {0}", ex.getMessage());
                }
                LOGGER.log(Level.INFO, "Cập nhật thành công supplier - ID: {0}", supplierId);
                request.getSession().setAttribute("message", "Cập nhật nhà cung cấp thành công");
                response.sendRedirect(request.getContextPath() + "/suppliers");
            } else {
                LOGGER.log(Level.WARNING, "Không thể cập nhật supplier - ID: {0}", supplierId);
                request.getSession().setAttribute("error", "Không thể cập nhật nhà cung cấp");
                response.sendRedirect(request.getContextPath() + "/suppliers?action=edit&id=" + supplierId);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Lỗi định dạng ID nhà cung cấp: {0}", e.getMessage());
            request.getSession().setAttribute("error", "ID nhà cung cấp không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/suppliers");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật nhà cung cấp: {0}", e.getMessage());
            e.printStackTrace(); // In stack trace để debug
            request.getSession().setAttribute("error", "Có lỗi xảy ra khi cập nhật nhà cung cấp");
            response.sendRedirect(request.getContextPath() + "/suppliers");
        }
    }

    private void viewSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Supplier supplier = supplierDAO.getSupplierById(id);
            if (supplier != null) {
                // Fetch cooperation times
                PurchaseOrderDAO poDAO = new PurchaseOrderDAO();
                Timestamp firstPurchase = null;
                Timestamp latestPurchase = null;
                try {
                    //firstPurchase = poDAO.getFirstPurchaseDateBySupplier(id);
                    //latestPurchase = poDAO.getLatestPurchaseDateBySupplier(id);
                } catch (Exception e) { /* ignore */ }
                request.setAttribute("firstPurchaseDate", firstPurchase);
                request.setAttribute("latestPurchaseDate", latestPurchase);
                // Calculate cooperation duration
                Integer coopMonths = null;
                Integer coopYears = null;
                Integer coopRemainMonths = null;
                if (firstPurchase != null) {
                    LocalDate start = firstPurchase.toLocalDateTime().toLocalDate();
                    LocalDate now = LocalDate.now(ZoneId.systemDefault());
                    Period period = Period.between(start, now);
                    coopYears = period.getYears();
                    coopRemainMonths = period.getMonths();
                    coopMonths = coopYears * 12 + coopRemainMonths;
                }
                request.setAttribute("cooperationMonths", coopMonths);
                request.setAttribute("cooperationYears", coopYears);
                request.setAttribute("cooperationRemainMonths", coopRemainMonths);
                request.setAttribute("supplier", supplier);
                // Auto update status to 'terminated' if inactive and last activity > 1 year
                if (supplier != null && "inactive".equalsIgnoreCase(supplier.getStatus()) && latestPurchase != null) {
                    LocalDate lastActivity = latestPurchase.toLocalDateTime().toLocalDate();
                    LocalDate now = LocalDate.now(ZoneId.systemDefault());
                    if (Period.between(lastActivity, now).getYears() >= 1) {
                        supplier.setStatus("terminated");
                    }
                }
                // Truyền endCooperationDate cho cả inactive và terminated
                Timestamp endCooperationDate = null;
                if (supplier != null && ("terminated".equalsIgnoreCase(supplier.getStatus()) || "inactive".equalsIgnoreCase(supplier.getStatus()))) {
                    endCooperationDate = latestPurchase;
                }
                request.setAttribute("endCooperationDate", endCooperationDate);
                request.getRequestDispatcher("/viewSupplier.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("error", "Không tìm thấy nhà cung cấp");
                response.sendRedirect(request.getContextPath() + "/suppliers");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "ID nhà cung cấp không hợp lệ: {0}", e.getMessage());
            request.getSession().setAttribute("error", "ID nhà cung cấp không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/suppliers");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xem chi tiết nhà cung cấp: {0}", e.getMessage());
            request.getSession().setAttribute("error", "Có lỗi xảy ra khi xem chi tiết nhà cung cấp");
            response.sendRedirect(request.getContextPath() + "/suppliers");
        }
    }
} 