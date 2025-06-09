package controller;

import dao.SupplierDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Supplier;

@WebServlet(name = "SupplierListServlet", urlPatterns = {"/supplier-list"})
public class SupplierListServlet extends HttpServlet {
    private SupplierDAO supplierDAO;

    @Override
    public void init() throws ServletException {
        supplierDAO = new SupplierDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                listSuppliers(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteSupplier(request, response);
                break;
            default:
                listSuppliers(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "add":
                addSupplier(request, response);
                break;
            case "edit":
                updateSupplier(request, response);
                break;
            default:
                listSuppliers(request, response);
        }
    }

    private void listSuppliers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        request.setAttribute("listSupplier", suppliers);
        request.getRequestDispatcher("/listSupplier.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/supplier-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Supplier supplier = supplierDAO.getSupplierById(id);
        request.setAttribute("supplier", supplier);
        request.getRequestDispatcher("/supplier-form.jsp").forward(request, response);
    }

    private void addSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String supplierName = request.getParameter("supplierName");
        String contactPerson = request.getParameter("contactPerson");
        String supplierPhone = request.getParameter("supplierPhone");
        String address = request.getParameter("address");
        String status = request.getParameter("status");

        Supplier supplier = new Supplier();
        supplier.setSupplierName(supplierName);
        supplier.setContactPerson(contactPerson);
        supplier.setSupplierPhone(supplierPhone);
        supplier.setAddress(address);
        supplier.setStatus(status);

        if (supplierDAO.addSupplier(supplier)) {
            response.sendRedirect(request.getContextPath() + "/supplier-list");
        } else {
            request.setAttribute("error", "Không thể thêm nhà cung cấp");
            request.getRequestDispatcher("/supplier-form.jsp").forward(request, response);
        }
    }

    private void updateSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String supplierName = request.getParameter("supplierName");
        String contactPerson = request.getParameter("contactPerson");
        String supplierPhone = request.getParameter("supplierPhone");
        String address = request.getParameter("address");
        String status = request.getParameter("status");

        Supplier supplier = new Supplier();
        supplier.setSupplierId(id);
        supplier.setSupplierName(supplierName);
        supplier.setContactPerson(contactPerson);
        supplier.setSupplierPhone(supplierPhone);
        supplier.setAddress(address);
        supplier.setStatus(status);

        if (supplierDAO.updateSupplier(supplier)) {
            response.sendRedirect(request.getContextPath() + "/supplier-list");
        } else {
            request.setAttribute("error", "Không thể cập nhật nhà cung cấp");
            request.setAttribute("supplier", supplier);
            request.getRequestDispatcher("/supplier-form.jsp").forward(request, response);
        }
    }

    private void deleteSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (supplierDAO.deleteSupplier(id)) {
            response.sendRedirect(request.getContextPath() + "/supplier-list");
        } else {
            request.setAttribute("error", "Không thể xóa nhà cung cấp");
            listSuppliers(request, response);
        }
    }
} 