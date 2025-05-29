package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import model.Category;
import dal.CategoryDAO;

import java.io.IOException;
import java.util.List;

@WebServlet("/categories")
public class CategoryController extends HttpServlet {
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
                case "edit": // sửa lại đúng với editCategory.jsp
                    updateCategory(request, response);
                    break;
                default:
                    response.sendRedirect("categories");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    private void listCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String parentIdParam = request.getParameter("parentId");
        String sortBy = request.getParameter("sortBy");

        if (keyword != null) {
            keyword = keyword.trim().toLowerCase();
        }

        Integer parentId = null;
        if (parentIdParam != null && !parentIdParam.trim().isEmpty()) {
            try {
                parentId = Integer.parseInt(parentIdParam);
            } catch (NumberFormatException ignored) {}
        }

        List<Category> categories = categoryDAO.getFilteredCategories(keyword, parentId, sortBy);
        List<Category> parentCategories = categoryDAO.getParentCategories();

        request.setAttribute("categories", categories);
        request.setAttribute("parentCategories", parentCategories);
        request.setAttribute("keyword", keyword);
        request.setAttribute("parentId", parentId);
        request.setAttribute("sortBy", sortBy);

        request.getRequestDispatcher("listCategory.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> parentCategories = categoryDAO.getParentCategories();
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

            List<Category> parentCategories = categoryDAO.getParentCategories();
            request.setAttribute("category", category);
            request.setAttribute("parentCategories", parentCategories);
            request.getRequestDispatcher("editCategory.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("categories");
        }
    }

    private void addCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String name = request.getParameter("name");
        String parentIdParam = request.getParameter("parentId");

        if (name == null || name.trim().isEmpty()) {
            response.sendRedirect("categories");
            return;
        }

        Integer parentId = null;
        if (parentIdParam != null && !parentIdParam.trim().isEmpty()) {
            try {
                parentId = Integer.parseInt(parentIdParam);
            } catch (NumberFormatException ignored) {}
        }

        categoryDAO.addCategory(name.trim(), parentId);
        response.sendRedirect("categories");
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String parentIdParam = request.getParameter("parentId");

            if (name == null || name.trim().isEmpty()) {
                response.sendRedirect("categories");
                return;
            }

            Integer parentId = null;
            if (parentIdParam != null && !parentIdParam.trim().isEmpty()) {
                try {
                    parentId = Integer.parseInt(parentIdParam);
                } catch (NumberFormatException ignored) {}
            }

            categoryDAO.updateCategory(id, name.trim(), parentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("categories");
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            categoryDAO.deleteCategory(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("categories");
    }
}
