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
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50)
public class CreateMaterialDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categories = categoryDAO.getAllCategories();
        request.setAttribute("categories", categories);
        SupplierDAO supplierDAO = new SupplierDAO();
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        request.setAttribute("suppliers", suppliers);
        UnitConversionDao unitDao = new UnitConversionDao();
        List<UnitConversion> units = unitDao.getAll(1);
        request.setAttribute("units", units);

        request.getRequestDispatcher("createMaterialDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();
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
            if (priceStr == null || priceStr.trim().isEmpty()) {
                throw new Exception("Vui lòng nhập giá vật tư.");
            }
            if (!priceStr.matches("^\\d+$")) {
                throw new Exception("Giá vật tư phải là số nguyên dương (không nhập số lẻ hoặc ký tự đặc biệt).");
            }
            long price = Long.parseLong(priceStr);
            if (price <= 0) {
                throw new Exception("Giá vật tư phải lớn hơn 0.");
            }

            if (materialDAO.getMaterialById(materialId) != null) {
                throw new Exception("ID vật tư đã tồn tại. Vui lòng chọn ID khác.");
            }
            String imageUrl = null;
            if (imagePart != null && imagePart.getSize() > 0) {
                String fileName = new File(imagePart.getSubmittedFileName()).getName();
                String uploadPath = getServletContext().getRealPath("/image");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();
                imagePart.write(uploadPath + File.separator + fileName);
                imageUrl = fileName;
            }

            Material material = new Material();
            material.setMaterialId(materialId);
            material.setName(name);
            material.setCategoryId(categoryId);
            material.setSupplierId(supplierId);
            material.setPrice(price);
            material.setImageUrl(imageUrl);
            material.setDescription(description);

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
        finally {
            materialDAO.closeConnection();
        }

        CategoryDAO categoryDAO = new CategoryDAO();
        SupplierDAO supplierDAO = new SupplierDAO();
        UnitConversionDao unitDao = new UnitConversionDao();

        try {
            request.setAttribute("categories", categoryDAO.getAllCategories());
            request.setAttribute("suppliers", supplierDAO.getAllSuppliers());
            request.setAttribute("units", unitDao.getAll(1));
            request.setAttribute("errorMessage", errorMessage);

            request.getRequestDispatcher("createMaterialDetail.jsp").forward(request, response);
        } finally {
            categoryDAO.closeConnection();
            supplierDAO.closeConnection();
            unitDao.closeConnection();
        }
    }
    
    
} 
