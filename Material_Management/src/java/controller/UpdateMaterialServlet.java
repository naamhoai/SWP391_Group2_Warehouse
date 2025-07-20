package controller;

import dao.MaterialDAO;
import dao.MaterialInfoDAO;
import dao.UnitDAO;
import model.Material;

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
import java.sql.SQLException;

@WebServlet("/updateMaterialServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 15
)
public class UpdateMaterialServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            int materialId = Integer.parseInt(request.getParameter("materialId"));
            String name = request.getParameter("name");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            int unitId = Integer.parseInt(request.getParameter("unit"));
            String description = request.getParameter("description");
            String status = request.getParameter("status");
            String imageUrl = null;
            MaterialDAO dao = new MaterialDAO();
            Material existingMaterial = dao.getMaterialById(materialId);

            Part filePart = request.getPart("imageUpload");
            String fileName = getFileName(filePart);
            if (filePart != null && filePart.getSize() > 0) {
                if (fileName == null) fileName = java.nio.file.Path.of(filePart.getSubmittedFileName()).getFileName().toString();
                if (!fileName.matches("(?i)^.+\\.(jpg|jpeg|png|gif)$")) {
                    request.setAttribute("errorMessage", "File ảnh không hợp lệ. Chỉ cho phép jpg, jpeg, png, gif.");
                    request.setAttribute("material", existingMaterial);
                    MaterialInfoDAO infoDAO = new MaterialInfoDAO();
                    request.setAttribute("categories", infoDAO.getAllCategoriesForDropdown());
                    request.setAttribute("suppliers", infoDAO.getAllSuppliersForDropdown());
                    UnitDAO unitDAO = new UnitDAO();
                    request.setAttribute("units", unitDAO.getWarehouseUnits());
                    request.getRequestDispatcher("/updateMaterialDetail.jsp").forward(request, response);
                    return;
                }
                String imagePath = fileName; // chỉ lưu tên file
                String buildImageDir = request.getServletContext().getRealPath("/image");
                java.io.File uploadDirFile = new java.io.File(buildImageDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }
                filePart.write(buildImageDir + java.io.File.separator + fileName);
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
            } else {
                if (existingMaterial != null) {
                    imageUrl = existingMaterial.getImageUrl();
                }
            }

            if (name == null || name.trim().isEmpty()) {
                throw new Exception("Tên vật tư không được để trống.");
            }
            if (name.length() > 50) {
                throw new Exception("Tên vật tư không được vượt quá 50 ký tự.");
            }
            if (description != null && description.length() > 255) {
                throw new Exception("Mô tả không được vượt quá 255 ký tự.");
            }
            // BỎ KIỂM TRA GIÁ
            
            if (status == null || status.trim().isEmpty()) {
                throw new Exception("Vui lòng chọn trạng thái vật tư.");
            }

            Material material = new Material();
            material.setMaterialId(materialId);
            material.setName(name);
            material.setCategoryId(categoryId);
            material.setSupplierId(supplierId);
            material.setUnitId(unitId);
            // material.setPrice(price); // KHÔNG SET GIÁ
            material.setDescription(description);
            material.setImageUrl(imageUrl);
            material.setStatus(status);

            boolean success = dao.updateMaterial(material);
            if (success) {
                response.sendRedirect("MaterialListServlet");
            } else {
                request.setAttribute("errorMessage", "Cập nhật vật tư thất bại, vui lòng thử lại.");
                request.setAttribute("material", material);
                MaterialInfoDAO infoDAO = new MaterialInfoDAO();
                request.setAttribute("categories", infoDAO.getAllCategoriesForDropdown());
                request.setAttribute("suppliers", infoDAO.getAllSuppliersForDropdown());
                UnitDAO unitDAO = new UnitDAO();
                request.setAttribute("units", unitDAO.getWarehouseUnits());
                request.getRequestDispatcher("/updateMaterialDetail.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu nhập vào không hợp lệ. Vui lòng kiểm tra lại ID, giá, số lượng.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp != null) {
            for (String token : contentDisp.split(";")) {
                if (token.trim().startsWith("filename")) {
                    return new File(token.substring(token.indexOf("=") + 2, token.length() - 1)).getName();
                }
            }
        }
        return "";
    }
} 