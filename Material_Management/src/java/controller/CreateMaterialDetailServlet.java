package controller;

import dao.CategoryDAO;
import dao.MaterialDAO;
import dao.SupplierDAO;
import dao.UnitConversionDao;
import model.Category;
import model.Material;
import model.Supplier;
import model.UnitConversion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "CreateMaterialDetailServlet", urlPatterns = {"/CreateMaterialDetail"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class CreateMaterialDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Load categories
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categories = categoryDAO.getAllCategories();
        request.setAttribute("categories", categories);

        // Load suppliers
        SupplierDAO supplierDAO = new SupplierDAO();
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        request.setAttribute("suppliers", suppliers);

        // Load units
        UnitConversionDao unitDao = new UnitConversionDao();
        List<UnitConversion> units = unitDao.getAll();
        request.setAttribute("units", units);

        request.getRequestDispatcher("createMaterialDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String materialIdStr = request.getParameter("materialId");
        String name = request.getParameter("name");
        String categoryIdStr = request.getParameter("category");
        String supplierIdStr = request.getParameter("supplier");
        String priceStr = request.getParameter("price");
        String description = request.getParameter("description");
        Part imagePart = request.getPart("imageUpload");

        String errorMessage = null;
        try {
            // Validate required fields
            if (materialIdStr == null || materialIdStr.isEmpty() ||
                name == null || name.trim().isEmpty() ||
                categoryIdStr == null || categoryIdStr.isEmpty() ||
                supplierIdStr == null || supplierIdStr.isEmpty() ||
                priceStr == null || priceStr.isEmpty()) {
                throw new Exception("Vui lòng nhập đầy đủ thông tin bắt buộc.");
            }

            int materialId = Integer.parseInt(materialIdStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            int supplierId = Integer.parseInt(supplierIdStr);
            BigDecimal price = new BigDecimal(priceStr);

            // Check duplicate ID
            MaterialDAO materialDAO = new MaterialDAO();
            if (materialDAO.getMaterialById(materialId) != null) {
                throw new Exception("ID vật tư đã tồn tại. Vui lòng chọn ID khác.");
            }

            // Handle image upload
            String imageUrl = null;
            if (imagePart != null && imagePart.getSize() > 0) {
                String fileName = new File(imagePart.getSubmittedFileName()).getName();
                String uploadPath = getServletContext().getRealPath("/image");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();
                imagePart.write(uploadPath + File.separator + fileName);
                imageUrl = fileName;
            }

            // Create Material object
            Material material = new Material();
            material.setMaterialId(materialId);
            material.setName(name);
            material.setCategoryId(categoryId);
            material.setSupplierId(supplierId);
            material.setPrice(price);
            material.setImageUrl(imageUrl);
            material.setDescription(description);

            // Save to DB
            boolean success = materialDAO.addMaterialWithId(material);
            if (success) {
                response.sendRedirect("MaterialListServlet");
                return;
            } else {
                errorMessage = "Không thể tạo mới vật tư. Vui lòng thử lại.";
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        // Reload dropdowns and show error
        CategoryDAO categoryDAO = new CategoryDAO();
        request.setAttribute("categories", categoryDAO.getAllCategories());
        SupplierDAO supplierDAO = new SupplierDAO();
        request.setAttribute("suppliers", supplierDAO.getAllSuppliers());
        UnitConversionDao unitDao = new UnitConversionDao();
        request.setAttribute("units", unitDao.getAll());
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("CreateMaterialDetail").forward(request, response);
    }
} 