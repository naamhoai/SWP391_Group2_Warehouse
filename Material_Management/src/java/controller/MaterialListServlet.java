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
import java.util.ArrayList;
import dao.CategoryDAO;
import dao.MaterialDetailHistoryDAO;
import java.sql.Timestamp;

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
        Integer categoryId = null;
        if (request.getParameter("category") != null && !request.getParameter("category").isEmpty()) {
            try {
                categoryId = Integer.parseInt(request.getParameter("category"));
            } catch (NumberFormatException e) {
                categoryId = null;
            }
        }
        String categoryFilter = "All";
        if (categoryId != null) {
            categoryFilter = String.valueOf(categoryId);
        }
        Integer unitFilter = null;
        if (request.getParameter("unit") != null && !request.getParameter("unit").isEmpty() && !request.getParameter("unit").equals("All")) {
            try {
                unitFilter = Integer.parseInt(request.getParameter("unit"));
            } catch (NumberFormatException e) {
                unitFilter = null;
            }
        }
        String statusFilter = request.getParameter("status") == null ? "All" : request.getParameter("status");
        String sortField = request.getParameter("sort") == null ? "material_id" : request.getParameter("sort");
        String sortDir = request.getParameter("dir") == null ? "asc" : request.getParameter("dir");

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
        int totalItems = dao.getTotalMaterialsForAdmin(searchQuery, categoryId, unitFilter, statusFilter);
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        }
        if (currentPage < 1) {
            currentPage = 1;
        }

        List<Material> materials = dao.getMaterialsForAdmin(searchQuery, categoryId, unitFilter, statusFilter, currentPage, itemsPerPage, sortField, sortDir);

        CategoryDAO categoryDAO = new CategoryDAO();
        MaterialInfoDAO infoDAO = new MaterialInfoDAO();
        List<Category> categories = categoryDAO.getVisibleCategories();
        List<Unit> units;
        try {
            units = infoDAO.getAllWarehouseUnits();
        } catch (Exception e) {
            units = new ArrayList<>();
            e.printStackTrace();
        }
        List<Integer> itemsPerPageOptions = Arrays.asList(5, 10, 20, 50);

        request.setAttribute("materials", materials);
        request.setAttribute("categories", categories);
        request.setAttribute("units", units);
        request.setAttribute("itemsPerPageOptions", itemsPerPageOptions);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("itemsPerPage", itemsPerPage);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("categoryFilter", categoryFilter);
        request.setAttribute("unitFilter", unitFilter);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("sortField", sortField);
        request.setAttribute("sortDir", sortDir);
        request.setAttribute("categoryId", categoryId);

        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("totalMaterials", totalItems);
        request.getRequestDispatcher("materialDetailList.jsp").forward(request, response);
        categoryDAO.closeConnection();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("toggleStatus".equals(action)) {
            String[] idArr = request.getParameterValues("materialIds");
            List<String> errorMessages = new ArrayList<>();
            if (idArr != null && idArr.length > 0) {
                try {
                    MaterialDAO dao = new MaterialDAO();
                    User user = (User) request.getSession().getAttribute("Admin");
                    int userId = user != null ? user.getUser_id() : -1;
                    String roleName = (user != null && user.getRole() != null) ? user.getRole().getRolename() : "";
                    MaterialDetailHistoryDAO historyDAO = new MaterialDetailHistoryDAO();
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    for (String idStr : idArr) {
                        int materialId;
                        try {
                            materialId = Integer.parseInt(idStr);
                        } catch (NumberFormatException e) {
                            errorMessages.add("ID vật tư không hợp lệ: " + idStr);
                            continue;
                        }
                        Material material = null;
                        try {
                            material = dao.getMaterialById(materialId);
                        } catch (Exception e) {
                            errorMessages.add("Lỗi khi lấy vật tư ID: " + materialId + ", lỗi: " + e.getMessage());
                            continue;
                        }
                        if (material == null) {
                            errorMessages.add("Không tìm thấy vật tư ID: " + materialId);
                            continue;
                        }
                        String currentStatus = material.getStatus();
                        String newStatus = null;
                        if ("active".equalsIgnoreCase(currentStatus)) {
                            // Đang kinh doanh -> luôn cho chuyển sang ngừng kinh doanh
                            newStatus = "inactive";
                        } else {
                            // Ngừng kinh doanh -> chỉ cho chuyển nếu category không ẩn (bỏ kiểm tra supplierStatus)
                            if (!material.isCategoryHidden()) {
                                newStatus = "active";
                            } else {
                                errorMessages.add("Không thể chuyển vật tư ID: " + materialId + " sang Đang kinh doanh vì danh mục bị ẩn.");
                                continue;
                            }
                        }
                        try {
                            dao.updateMaterialStatus(materialId, newStatus);
                            // Không ghi lịch sử khi chuyển trạng thái hàng loạt
                        } catch (Exception e) {
                            errorMessages.add("Lỗi khi cập nhật vật tư ID: " + materialId + ", lỗi: " + e.getMessage());
                        }
                    }
                    if (errorMessages.isEmpty()) {
                        response.sendRedirect("MaterialListServlet?actionStatus=deleteSuccess");
                    } else {
                        request.setAttribute("errorMessages", errorMessages);
                        doGet(request, response);
                    }
                } catch (Exception e) {
                    errorMessages.add("Lỗi không xác định: " + e.getMessage());
                    request.setAttribute("errorMessages", errorMessages);
                    doGet(request, response);
                }
            } else {
                response.sendRedirect("MaterialListServlet");
            }
            return;
        }
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
