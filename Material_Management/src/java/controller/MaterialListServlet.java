package controller;

import dao.MaterialDAO;
import dao.MaterialInfoDAO;
import java.util.List;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import model.*;

@WebServlet(name = "MaterialListServlet", urlPatterns = {"/MaterialListServlet"})
public class MaterialListServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet MaterialListServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MaterialListServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String searchQuery = request.getParameter("search");
        String categoryFilter = request.getParameter("category") == null ? "All" : request.getParameter("category");
        String supplierFilter = request.getParameter("supplier") == null ? "All" : request.getParameter("supplier");
        String sortField = request.getParameter("sort") == null ? "material_id" : request.getParameter("sort");
        String sortDir = request.getParameter("dir") == null ? "desc" : request.getParameter("dir");

        int currentPage = 1;
        int itemsPerPage = 10;
        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                currentPage = Integer.parseInt(pageStr);
            }
            String itemsPerPageStr = request.getParameter("itemsPerPage");
            if (itemsPerPageStr != null && !itemsPerPageStr.isEmpty()) {
                itemsPerPage = Integer.parseInt(itemsPerPageStr);
            }
        } catch (NumberFormatException e) {

        }

        MaterialDAO dao = new MaterialDAO();
        int totalItems = dao.getTotalMaterialsForAdmin(searchQuery, categoryFilter, supplierFilter);
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        }
        if (currentPage < 1) {
            currentPage = 1;
        }

        List<Material> materials = dao.getMaterialsForAdmin(searchQuery, categoryFilter, supplierFilter, currentPage, itemsPerPage, sortField, sortDir);

        MaterialInfoDAO infoDAO = new MaterialInfoDAO();
        List<String> categories = infoDAO.getAllCategories();
        List<String> suppliers = infoDAO.getAllSuppliers();
        List<Integer> itemsPerPageOptions = Arrays.asList(5, 10, 20, 50);

        request.setAttribute("materials", materials);
        request.setAttribute("categories", categories);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("itemsPerPageOptions", itemsPerPageOptions);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("itemsPerPage", itemsPerPage);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("categoryFilter", categoryFilter);
        request.setAttribute("supplierFilter", supplierFilter);
        request.setAttribute("sortField", sortField);
        request.setAttribute("sortDir", sortDir);

        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.getRequestDispatcher("materialDetailList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            String idStr = request.getParameter("materialId");
            if (idStr != null) {
                try {
                    int materialId = Integer.parseInt(idStr);
                    MaterialDAO dao = new MaterialDAO();
                    dao.deleteMaterial(materialId);
                } catch (Exception e) {
                }
            }
            response.sendRedirect("MaterialListServlet");
            return;
        }
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
