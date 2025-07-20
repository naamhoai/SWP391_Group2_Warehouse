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
                case "hide":
                    hideCategory(request, response);
                    break;
                case "unhide":
                    unhideCategory(request, response);
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
        String pageStr = request.getParameter("page");
        String hiddenFilter = request.getParameter("hiddenFilter");

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

        // Xử lý phân trang
        int currentPage = 1;
        final int PAGE_SIZE = 10; // Số items mỗi trang

        try {
            if (pageStr != null) {
                currentPage = Math.max(1, Integer.parseInt(pageStr));
            }
        } catch (NumberFormatException e) {
            currentPage = 1;
        }

        // Lấy tổng số trang (chỉ đếm các danh mục con)
        int totalPages = categoryDAO.getTotalPages(keyword, parentId, PAGE_SIZE);
        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        }

        // Tính tổng số danh mục
        int totalCategories = categoryDAO.getTotalCategories(keyword, parentId, hiddenFilter);

        // Lấy danh sách category cho trang hiện tại (chỉ lấy danh mục con)
        List<Category> categories = categoryDAO.getFilteredCategoriesWithPaging(keyword, parentId, sortBy, hiddenFilter, currentPage, PAGE_SIZE);
        
        // Lấy danh sách danh mục vật tư cho dropdown filter
        List<Category> parentCategories = categoryDAO.getVisibleParentCategories();
        
        // Tạo map chứa tên danh mục vật tư
        java.util.Map<Integer, String> parentNames = new java.util.HashMap<>();
        for (Category category : categories) {
            if (category.getParentId() != null) {
                parentNames.put(category.getParentId(), categoryDAO.getCategoryNameById(category.getParentId()));
            }
        }

        // Tính toán phạm vi trang hiển thị
        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);

        // Set all attributes
        request.setAttribute("categories", categories);
        request.setAttribute("parentCategories", parentCategories);
        request.setAttribute("parentNames", parentNames);
        request.setAttribute("keyword", keyword);
        request.setAttribute("parentId", parentIdParam);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("hiddenFilter", hiddenFilter);
        request.setAttribute("totalCategories", totalCategories);

        request.getRequestDispatcher("listCategory.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> parentCategories = categoryDAO.getAvailableParentCategories(null);
        request.setAttribute("parentCategories", parentCategories);
        // Lấy thông báo thành công từ session nếu có
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("success") != null) {
            request.setAttribute("success", session.getAttribute("success"));
            session.removeAttribute("success");
        }
        request.getRequestDispatcher("addCategory.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Category category = categoryDAO.getCategoryById(id);

            if (category == null) {
                request.setAttribute("error", "Không tìm thấy danh mục với ID: " + id);
                List<Category> parentCategories = categoryDAO.getAvailableParentCategories(null);
                request.setAttribute("parentCategories", parentCategories);
                request.getRequestDispatcher("editCategory.jsp").forward(request, response);
                return;
            }

            List<Category> parentCategories = categoryDAO.getAvailableParentCategories(id);
            request.setAttribute("category", category);
            request.setAttribute("parentCategories", parentCategories);
            request.getRequestDispatcher("editCategory.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải trang sửa: " + e.getMessage());
            List<Category> parentCategories = categoryDAO.getAvailableParentCategories(null);
            request.setAttribute("parentCategories", parentCategories);
            request.getRequestDispatcher("editCategory.jsp").forward(request, response);
        }
    }

    private void addCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String name = request.getParameter("name");
        String parentIdParam = request.getParameter("parentId");
        java.sql.Timestamp createdAt = null;
        
        // Tạo timestamp hiện tại thay vì parse từ form
        createdAt = new java.sql.Timestamp(System.currentTimeMillis());

        // Validate tên danh mục
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Tên danh mục không được để trống");
            request.setAttribute("name", name);
            request.setAttribute("parentId", parentIdParam);
            showAddForm(request, response);
            return;
        }
        if (name.trim().length() < 2) {
            request.setAttribute("error", "Tên danh mục phải có ít nhất 2 ký tự");
            request.setAttribute("name", name);
            request.setAttribute("parentId", parentIdParam);
            showAddForm(request, response);
            return;
        }
        if (name.trim().replaceAll(" ", "").isEmpty()) {
            request.setAttribute("error", "Tên danh mục không được chỉ chứa khoảng trắng");
            request.setAttribute("name", name);
            request.setAttribute("parentId", parentIdParam);
            showAddForm(request, response);
            return;
        }
        if (!name.trim().matches("^[\\p{L}0-9 ]+$")) {
            request.setAttribute("error", "Tên danh mục chỉ được chứa chữ, số và khoảng trắng");
            request.setAttribute("name", name);
            request.setAttribute("parentId", parentIdParam);
            showAddForm(request, response);
            return;
        }
        // Kiểm tra tên trùng (bỏ qua khoảng trắng thừa giữa các từ)
        String normalizedName = name.trim().replaceAll("\\s+", " ");
        boolean isDuplicate = categoryDAO.getAllCategories().stream()
            .anyMatch(c -> c.getName().trim().replaceAll("\\s+", " ").equalsIgnoreCase(normalizedName));
        if (isDuplicate) {
            request.setAttribute("error", "Tên danh mục đã tồn tại. Vui lòng nhập tên khác.");
            request.setAttribute("name", name);
            request.setAttribute("parentId", parentIdParam);
            showAddForm(request, response);
            return;
        }

        Integer parentId = null;
        if (parentIdParam != null && !parentIdParam.trim().isEmpty()) {
            try {
                parentId = Integer.parseInt(parentIdParam);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID danh mục cha không hợp lệ");
                request.setAttribute("name", name);
                request.setAttribute("parentId", parentIdParam);
                showAddForm(request, response);
                return;
            }
        }

        String error = categoryDAO.addCategory(name.trim(), parentId, createdAt);
        if (error == null) {
            request.getSession().setAttribute("message", "Thêm danh mục thành công!");
            response.sendRedirect("categories");
        } else {
            request.setAttribute("error", error);
            request.setAttribute("name", name);
            request.setAttribute("parentId", parentIdParam);
            showAddForm(request, response);
        }
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String parentIdParam = request.getParameter("parentId");
            
            // Set current date for display
            java.sql.Timestamp currentTime = new java.sql.Timestamp(System.currentTimeMillis());
            request.setAttribute("updatedAt", currentTime.toString());

            // Validate tên danh mục
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("error", "Tên danh mục không được để trống");
                request.setAttribute("category", categoryDAO.getCategoryById(id));
                request.setAttribute("parentId", parentIdParam);
                showEditForm(request, response);
                return;
            }
            if (name.trim().length() < 2) {
                request.setAttribute("error", "Tên danh mục phải có ít nhất 2 ký tự");
                request.setAttribute("category", categoryDAO.getCategoryById(id));
                request.setAttribute("parentId", parentIdParam);
                showEditForm(request, response);
                return;
            }
            if (name.trim().replaceAll(" ", "").isEmpty()) {
                request.setAttribute("error", "Tên danh mục không được chỉ chứa khoảng trắng");
                request.setAttribute("category", categoryDAO.getCategoryById(id));
                request.setAttribute("parentId", parentIdParam);
                showEditForm(request, response);
                return;
            }
            if (!name.trim().matches("^[\\p{L}0-9 ]+$")) {
                request.setAttribute("error", "Tên danh mục chỉ được chứa chữ, số và khoảng trắng");
                request.setAttribute("category", categoryDAO.getCategoryById(id));
                request.setAttribute("parentId", parentIdParam);
                showEditForm(request, response);
                return;
            }
            // Kiểm tra tên trùng (trừ chính nó, bỏ qua khoảng trắng thừa giữa các từ)
            String normalizedName = name.trim().replaceAll("\\s+", " ");
            boolean isDuplicate = categoryDAO.getAllCategories().stream()
                .anyMatch(c -> c.getName().trim().replaceAll("\\s+", " ").equalsIgnoreCase(normalizedName) && c.getCategoryId() != id);
            if (isDuplicate) {
                request.setAttribute("error", "Tên danh mục đã tồn tại. Vui lòng nhập tên khác.");
                request.setAttribute("category", categoryDAO.getCategoryById(id));
                request.setAttribute("parentId", parentIdParam);
                showEditForm(request, response);
                return;
            }

            Integer parentId = null;
            if (parentIdParam != null && !parentIdParam.trim().isEmpty()) {
                try {
                    parentId = Integer.parseInt(parentIdParam);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "ID danh mục cha không hợp lệ");
                    request.setAttribute("category", categoryDAO.getCategoryById(id));
                    request.setAttribute("parentId", parentIdParam);
                    showEditForm(request, response);
                    return;
                }
                // Không tự chọn chính nó làm cha
                if (parentId == id) {
                    request.setAttribute("error", "Không thể chọn chính nó làm danh mục cha");
                    request.setAttribute("category", categoryDAO.getCategoryById(id));
                    request.setAttribute("parentId", parentIdParam);
                    showEditForm(request, response);
                    return;
                }
            }
            // Không chuyển cha thành con nếu đang có con
            boolean isCurrentlyParent = categoryDAO.getAllCategories().stream().anyMatch(c -> c.getParentId() != null && c.getParentId() == id);
            if (isCurrentlyParent && parentId != null) {
                request.setAttribute("error", "Không thể chuyển danh mục cha thành danh mục con khi đã có danh mục con");
                request.setAttribute("category", categoryDAO.getCategoryById(id));
                request.setAttribute("parentId", parentIdParam);
                showEditForm(request, response);
                return;
            }

            boolean success = categoryDAO.updateCategory(id, name.trim(), parentId);
            if (success) {
                request.getSession().setAttribute("message", "Cập nhật danh mục thành công");
                response.sendRedirect("categories");
            } else {
                request.setAttribute("error", "Không thể cập nhật danh mục. Vui lòng kiểm tra lại danh mục cha hoặc xem danh mục này có chứa danh mục con không.");
                request.setAttribute("category", categoryDAO.getCategoryById(id));
                request.setAttribute("parentId", parentIdParam);
                showEditForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            showEditForm(request, response);
        }
    }

    private void hideCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = categoryDAO.hideCategory(id);
            if (success) {
                request.getSession().setAttribute("message", "Ẩn danh mục thành công");
            } else {
                request.getSession().setAttribute("error", "Không thể ẩn danh mục. Vui lòng kiểm tra xem danh mục có chứa danh mục con không.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Có lỗi xảy ra khi ẩn danh mục: " + e.getMessage());
        }
        response.sendRedirect("categories");
    }

    private void unhideCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = categoryDAO.unhideCategory(id);
            if (success) {
                request.getSession().setAttribute("message", "Hiện lại danh mục thành công");
            } else {
                request.getSession().setAttribute("error", "Không thể hiện lại danh mục.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Có lỗi xảy ra khi hiện lại danh mục: " + e.getMessage());
        }
        response.sendRedirect("categories");
    }
} 