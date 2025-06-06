package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import model.Category;
import dao.CategoryDAO;

import java.io.IOException;
import java.util.List;

@WebServlet(name="CategoryServlet", urlPatterns={"/categories"})
public class CategoryServlet extends HttpServlet {
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        try {
            categoryDAO = new CategoryDAO();
        } catch (Exception e) {
            throw new ServletException("Không thể khởi tạo CategoryDAO: " + e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteCategory(request, response);
                    break;
                default:
                    listCategories(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("categories");
            return;
        }

        try {
            switch (action) {
                case "add":
                    addCategory(request, response);
                    break;
                case "edit":
                    updateCategory(request, response);
                    break;
                default:
                    response.sendRedirect("categories");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Error: " + e.getMessage());
        }
    }

    private void listCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String parentIdParam = request.getParameter("parentId");
        String sortBy = request.getParameter("sortBy");

        // Clean up keyword
        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) keyword = null;
        }

        // Parse parentId
        Integer parentId = null;
        if (parentIdParam != null && !parentIdParam.trim().isEmpty()) {
            try {
                parentId = Integer.parseInt(parentIdParam);
            } catch (NumberFormatException ignored) {}
        }

        List<Category> categories = categoryDAO.getFilteredCategories(keyword, parentId, sortBy);
        List<Category> parentCategories = categoryDAO.getParentCategories();

        // Set all attributes
        request.setAttribute("categories", categories);
        request.setAttribute("parentCategories", parentCategories);
        request.setAttribute("keyword", keyword);
        request.setAttribute("parentId", parentIdParam);
        request.setAttribute("sortBy", sortBy);

        request.getRequestDispatcher("listCategory.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> parentCategories = categoryDAO.getAvailableParentCategories(null);
        request.setAttribute("parentCategories", parentCategories);
        request.getRequestDispatcher("addCategory.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Category category = categoryDAO.getCategoryById(id);

            if (category == null) {
                response.sendRedirect("categories");
                return;
            }

            List<Category> parentCategories = categoryDAO.getAvailableParentCategories(id);
            request.setAttribute("category", category);
            request.setAttribute("parentCategories", parentCategories);
            request.getRequestDispatcher("editCategory.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("categories");
        }
    }

    private void addCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String name = request.getParameter("name");
        String parentIdParam = request.getParameter("parentId");

        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Tên danh mục không được để trống");
            showAddForm(request, response);
            return;
        }

        Integer parentId = null;
        if (parentIdParam != null && !parentIdParam.trim().isEmpty()) {
            try {
                parentId = Integer.parseInt(parentIdParam);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID danh mục cha không hợp lệ");
                showAddForm(request, response);
                return;
            }
        }

        boolean success = categoryDAO.addCategory(name.trim(), parentId);
        
        if (success) {
            request.getSession().setAttribute("message", "Thêm danh mục thành công");
            response.sendRedirect("categories");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi thêm danh mục. Vui lòng kiểm tra lại danh mục cha.");
            showAddForm(request, response);
        }
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String parentIdParam = request.getParameter("parentId");

            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("error", "Tên danh mục không được để trống");
                showEditForm(request, response);
                return;
            }

            Integer parentId = null;
            if (parentIdParam != null && !parentIdParam.trim().isEmpty()) {
                try {
                    parentId = Integer.parseInt(parentIdParam);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "ID danh mục cha không hợp lệ");
                    showEditForm(request, response);
                    return;
                }
            }

            boolean success = categoryDAO.updateCategory(id, name.trim(), parentId);
            if (success) {
                request.getSession().setAttribute("message", "Cập nhật danh mục thành công");
                response.sendRedirect("categories");
            } else {
                request.setAttribute("error", "Không thể cập nhật danh mục. Vui lòng kiểm tra lại danh mục cha hoặc xem danh mục này có chứa danh mục con không.");
                showEditForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            showEditForm(request, response);
        }
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = categoryDAO.deleteCategory(id);
            
            if (success) {
                request.getSession().setAttribute("message", "Xóa danh mục thành công");
            } else {
                request.getSession().setAttribute("error", "Không thể xóa danh mục. Vui lòng kiểm tra xem danh mục có chứa danh mục con không.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Có lỗi xảy ra khi xóa danh mục: " + e.getMessage());
        }
        response.sendRedirect("categories");
    }
} 