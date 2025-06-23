package controller;

import dao.MaterialSupplierDAO;
import model.MaterialSupplier;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "MaterialSupplierServlet", urlPatterns = {"/material-suppliers"})
public class MaterialSupplierServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String supplierIdStr = request.getParameter("supplier_id");
        String keyword = request.getParameter("keyword");
        int supplierId = 0;
        try {
            supplierId = Integer.parseInt(supplierIdStr);
        } catch (Exception e) {
        }
        int pageSize = 10;
        try { pageSize = Integer.parseInt(request.getParameter("pageSize")); } catch(Exception e) {}
        int currentPage = 1;
        try { currentPage = Integer.parseInt(request.getParameter("page")); } catch(Exception e) {}

        MaterialSupplierDAO dao = new MaterialSupplierDAO();
        List<MaterialSupplier> list = dao.getMaterialsBySupplierId(supplierId);
        List<MaterialSupplier> filtered = new ArrayList<>();
        if (list != null) {
            if (keyword != null && !keyword.trim().isEmpty()) {
                for (MaterialSupplier ms : list) {
                    if (ms.getMaterialName() != null && ms.getMaterialName().toLowerCase().contains(keyword.toLowerCase())) {
                        filtered.add(ms);
                    }
                }
            } else {
                filtered = list;
            }
        }
        int total = filtered != null ? filtered.size() : 0;
        int totalPages = (int)Math.ceil((double)total / pageSize);
        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;
        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        List<MaterialSupplier> pageList = new ArrayList<>();
        if (filtered != null && !filtered.isEmpty() && start < end) {
            pageList = filtered.subList(start, end);
        }
        request.setAttribute("materialSupplierList", pageList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("total", total);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("keyword", keyword);
        request.setAttribute("supplier_id", supplierIdStr);
        request.getRequestDispatcher("materialSupplierList.jsp").forward(request, response);
    }
} 