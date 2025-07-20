package controller;

import dao.CategoryDAO;
import dao.MaterialDAO;
import dao.SupplierDAO;
import dao.UnitDAO;
import model.Category;
import model.Material;
import model.Supplier;
import model.Unit;

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
        UnitDAO unitDao = new UnitDAO();
        List<Unit> units = null;
        try {
            units = unitDao.getWarehouseUnits();
        } catch (Exception e) {
            units = List.of();
        }
        request.setAttribute("units", units);
        MaterialDAO materialDAO = new MaterialDAO();
        int nextMaterialId = materialDAO.getNextMaterialId();
        request.setAttribute("materialId", nextMaterialId);
        materialDAO.closeConnection();
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
        String categoryIdStr = request.getParameter("categoryId");
        String supplierIdStr = request.getParameter("supplierId");
        String unitIdStr = request.getParameter("unit");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        Part imagePart = request.getPart("imageUpload");

        String errorMessage = null;
        try {
            if (name == null || name.trim().isEmpty() ||
                categoryIdStr == null || categoryIdStr.isEmpty() ||
                supplierIdStr == null || supplierIdStr.isEmpty() ||
                unitIdStr == null || unitIdStr.isEmpty()) {
                throw new Exception("Vui lòng nhập đầy đủ thông tin bắt buộc.");
            }

            int materialId = Integer.parseInt(materialIdStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            int supplierId = Integer.parseInt(supplierIdStr);
            int unitId = Integer.parseInt(unitIdStr);

            if (name.length() > 50) {
                throw new Exception("Tên vật tư không được vượt quá 50 ký tự.");
            }
            
            if (status == null || status.trim().isEmpty()) {
                throw new Exception("Vui lòng chọn trạng thái vật tư.");
            }
            
            if (description != null && description.length() > 255) {
                throw new Exception("Mô tả không được vượt quá 255 ký tự.");
            }

            String imageUrl = null;
            if (imagePart != null && imagePart.getSize() > 0) {
                String fileName = java.nio.file.Path.of(imagePart.getSubmittedFileName()).getFileName().toString();
                if (!fileName.matches("(?i)^.+\\.(jpg|jpeg|png|gif)$")) {
                    request.setAttribute("errorMessage", "File ảnh không hợp lệ. Chỉ cho phép jpg, jpeg, png, gif.");
                    request.setAttribute("materialId", materialIdStr);
                    request.setAttribute("name", name);
                    request.setAttribute("category", categoryIdStr);
                    request.setAttribute("supplier", supplierIdStr);
                    request.setAttribute("unit", unitIdStr);
                    request.setAttribute("description", description);
                    CategoryDAO categoryDAO2 = new CategoryDAO();
                    SupplierDAO supplierDAO2 = new SupplierDAO();
                    UnitDAO unitDao2 = new UnitDAO();
                    try {
                        request.setAttribute("categories", categoryDAO2.getAllCategories());
                        request.setAttribute("suppliers", supplierDAO2.getAllSuppliers());
                        try {
                            request.setAttribute("units", unitDao2.getWarehouseUnits());
                        } catch (Exception ex) {
                            request.setAttribute("units", java.util.List.of());
                        }
                        request.getRequestDispatcher("createMaterialDetail.jsp").forward(request, response);
                    } finally {
                        categoryDAO2.closeConnection();
                        supplierDAO2.closeConnection();
                        unitDao2.closeConnection();
                    }
                    return;
                }
                String imagePath = fileName; // chỉ lưu tên file
                String buildImageDir = request.getServletContext().getRealPath("/image");
                java.io.File uploadDirFile = new java.io.File(buildImageDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }
                imagePart.write(buildImageDir + java.io.File.separator + fileName);
                // Copy sang source (web/image)
                try {
                    java.io.File buildImageDirFile = new java.io.File(buildImageDir);
                    java.io.File projectRoot = buildImageDirFile.getParentFile().getParentFile().getParentFile();
                    java.io.File sourceImageDirFile = new java.io.File(projectRoot, "web/image");
                    if (!sourceImageDirFile.exists()) {
                        sourceImageDirFile.mkdirs();
                    }
                    java.nio.file.Path source = java.nio.file.Paths.get(buildImageDir, fileName);
                    java.nio.file.Path target = java.nio.file.Paths.get(sourceImageDirFile.getAbsolutePath(), fileName);
                    java.nio.file.Files.copy(source, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ex) {
                    ex.printStackTrace(); // log lỗi copy
                }
                imageUrl = imagePath; // chỉ lưu tên file
            }

            Material material = new Material();
            material.setMaterialId(materialId);
            material.setName(name);
            material.setCategoryId(categoryId);
            material.setSupplierId(supplierId);
            material.setUnitId(unitId);
            material.setImageUrl(imageUrl);
            material.setDescription(description);
            material.setStatus(status);

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
        if (errorMessage != null) {
            if (!errorMessage.contains("ID vật tư")) {
                request.setAttribute("materialId", materialIdStr);
            }
            if (!errorMessage.contains("Tên vật tư")) {
                request.setAttribute("name", name);
            }
            request.setAttribute("category", categoryIdStr);
            request.setAttribute("supplier", supplierIdStr);
            request.setAttribute("unit", unitIdStr);
            request.setAttribute("description", description);
        }

        CategoryDAO categoryDAO = new CategoryDAO();
        SupplierDAO supplierDAO = new SupplierDAO();
        UnitDAO unitDao = new UnitDAO();

        try {
            request.setAttribute("categories", categoryDAO.getAllCategories());
            request.setAttribute("suppliers", supplierDAO.getAllSuppliers());
            try {
                request.setAttribute("units", unitDao.getWarehouseUnits());
            } catch (Exception ex) {
                request.setAttribute("units", List.of());
            }
            request.setAttribute("errorMessage", errorMessage);

            request.getRequestDispatcher("createMaterialDetail.jsp").forward(request, response);
        } finally {
            categoryDAO.closeConnection();
            supplierDAO.closeConnection();
            unitDao.closeConnection();
        }
    }
    
    
} 
