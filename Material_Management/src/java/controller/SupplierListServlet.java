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
            LOGGER.log(Level.SEVERE, "Error processing request: {0}", e.getMessage());
            request.getSession().setAttribute("error", "An error occurred: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/suppliers");
        }
    }

    private void listSuppliers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String status = request.getParameter("status");
            String keyword = request.getParameter("keyword");
            String sortBy = request.getParameter("sortBy");

            List<Supplier> suppliers;
            if (keyword != null && !keyword.trim().isEmpty()) {
                suppliers = supplierDAO.searchSuppliers(keyword);
            } else {
                suppliers = supplierDAO.getAllSuppliers();
            }

            if (status != null && !status.isEmpty()) {
                suppliers = suppliers.stream()
                    .filter(s -> status.equals(s.getStatus()))
                    .toList();
            }

            if (sortBy != null) {
                switch (sortBy) {
                    case "name":
                        suppliers.sort((s1, s2) -> s1.getSupplierName().compareTo(s2.getSupplierName()));
                        break;
                    case "id":
                        suppliers.sort((s1, s2) -> Integer.compare(s1.getSupplierId(), s2.getSupplierId()));
                        break;
                }
            }

            request.setAttribute("suppliers", suppliers);
            request.getRequestDispatcher("/listSupplier.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in listSuppliers: {0}", e.getMessage());
            request.getSession().setAttribute("error", "Error loading supplier list");
            response.sendRedirect(request.getContextPath() + "/suppliers");
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Supplier supplier = supplierDAO.getSupplierById(id);
            if (supplier != null) {
                request.setAttribute("supplier", supplier);
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

            // Validate input
            if (supplierName == null || supplierName.trim().length() < 2) {
                request.getSession().setAttribute("error", "Tên nhà cung cấp phải có ít nhất 2 ký tự");
                response.sendRedirect(request.getContextPath() + "/suppliers?action=add");
                return;
            }

            if (contactPerson == null || contactPerson.trim().length() < 2) {
                request.getSession().setAttribute("error", "Tên người liên hệ phải có ít nhất 2 ký tự");
                response.sendRedirect(request.getContextPath() + "/suppliers?action=add");
                return;
            }

            if (supplierPhone == null || !supplierPhone.matches("^[0-9]{10,11}$")) {
                request.getSession().setAttribute("error", "Số điện thoại phải có 10-11 chữ số");
                response.sendRedirect(request.getContextPath() + "/suppliers?action=add");
                return;
            }

            if (address == null || address.trim().length() < 5) {
                request.getSession().setAttribute("error", "Địa chỉ phải có ít nhất 5 ký tự");
                response.sendRedirect(request.getContextPath() + "/suppliers?action=add");
                return;
            }

            if (status == null || (!status.equals("active") && !status.equals("inactive"))) {
                request.getSession().setAttribute("error", "Trạng thái không hợp lệ");
                response.sendRedirect(request.getContextPath() + "/suppliers?action=add");
                return;
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

            LOGGER.log(Level.INFO, "Nhận được request cập nhật supplier - ID: {0}, Name: {1}, Status: {2}",
                new Object[]{supplierId, supplierName, status});

            // Validate input
            if (supplierName == null || supplierName.trim().length() < 2) {
                request.getSession().setAttribute("error", "Tên nhà cung cấp phải có ít nhất 2 ký tự");
                response.sendRedirect(request.getContextPath() + "/suppliers?action=edit&id=" + supplierId);
                return;
            }

            if (contactPerson == null || contactPerson.trim().length() < 2) {
                request.getSession().setAttribute("error", "Tên người liên hệ phải có ít nhất 2 ký tự");
                response.sendRedirect(request.getContextPath() + "/suppliers?action=edit&id=" + supplierId);
                return;
            }

            if (supplierPhone == null || !supplierPhone.matches("^[0-9]{10,11}$")) {
                request.getSession().setAttribute("error", "Số điện thoại phải có 10-11 chữ số");
                response.sendRedirect(request.getContextPath() + "/suppliers?action=edit&id=" + supplierId);
                return;
            }

            if (address == null || address.trim().length() < 5) {
                request.getSession().setAttribute("error", "Địa chỉ phải có ít nhất 5 ký tự");
                response.sendRedirect(request.getContextPath() + "/suppliers?action=edit&id=" + supplierId);
                return;
            }

            if (status == null || (!status.equals("active") && !status.equals("inactive"))) {
                request.getSession().setAttribute("error", "Trạng thái không hợp lệ");
                response.sendRedirect(request.getContextPath() + "/suppliers?action=edit&id=" + supplierId);
                return;
            }

            // Create supplier object
            Supplier supplier = new Supplier();
            supplier.setSupplierId(supplierId);
            supplier.setSupplierName(supplierName.trim());
            supplier.setContactPerson(contactPerson.trim());
            supplier.setSupplierPhone(supplierPhone.trim());
            supplier.setAddress(address.trim());
            supplier.setStatus(status);

            LOGGER.log(Level.INFO, "Đang gửi request cập nhật đến DAO - ID: {0}", supplierId);

            // Update supplier
            if (supplierDAO.updateSupplier(supplier)) {
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
                request.setAttribute("supplier", supplier);
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