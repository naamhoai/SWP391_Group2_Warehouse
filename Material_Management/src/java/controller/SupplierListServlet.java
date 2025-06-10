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
                case "delete":
                    deleteSupplier(request, response);
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
            Supplier supplier = new Supplier();
            supplier.setSupplierName(request.getParameter("name"));
            supplier.setContactPerson(request.getParameter("contact"));
            supplier.setSupplierPhone(request.getParameter("phone"));
            supplier.setAddress(request.getParameter("address"));
            supplier.setStatus(request.getParameter("status"));

            if (supplierDAO.addSupplier(supplier)) {
                request.getSession().setAttribute("message", "Supplier added successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to add supplier");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in addSupplier: {0}", e.getMessage());
            request.getSession().setAttribute("error", "Error adding supplier");
        }
        response.sendRedirect(request.getContextPath() + "/suppliers");
    }

    private void updateSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Supplier supplier = new Supplier();
            supplier.setSupplierId(Integer.parseInt(request.getParameter("id")));
            supplier.setSupplierName(request.getParameter("name"));
            supplier.setContactPerson(request.getParameter("contact"));
            supplier.setSupplierPhone(request.getParameter("phone"));
            supplier.setAddress(request.getParameter("address"));
            supplier.setStatus(request.getParameter("status"));

            if (supplierDAO.updateSupplier(supplier)) {
                request.getSession().setAttribute("message", "Supplier updated successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to update supplier");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in updateSupplier: {0}", e.getMessage());
            request.getSession().setAttribute("error", "Error updating supplier");
        }
        response.sendRedirect(request.getContextPath() + "/suppliers");
    }

    private void deleteSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            if (supplierDAO.deleteSupplier(id)) {
                request.getSession().setAttribute("message", "Supplier deleted successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to delete supplier");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid supplier ID format: {0}", e.getMessage());
            request.getSession().setAttribute("error", "Invalid supplier ID");
        }
        response.sendRedirect(request.getContextPath() + "/suppliers");
    }
} 