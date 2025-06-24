package controller;

import dao.MaterialDAO;
import dao.MaterialInfoDAO;
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
            String priceStr = request.getParameter("price");
            if (priceStr == null || priceStr.trim().isEmpty()) {
                throw new Exception("Giá vật tư không được để trống.");
            }
            if (!priceStr.matches("^\\d+$")) {
                throw new Exception("Giá vật tư phải là số nguyên dương (không nhập số lẻ hoặc ký tự đặc biệt).");
            }
            long price = Long.parseLong(priceStr);
            if (price <= 0) {
                throw new Exception("Giá vật tư phải lớn hơn 0.");
            }
            String description = request.getParameter("description");
            String imageUrl = null;
            MaterialDAO dao = new MaterialDAO();
            Material existingMaterial = dao.getMaterialById(materialId);
            int conversionId = 0;
            if (existingMaterial != null) {
                conversionId = existingMaterial.getConversionId();
            }

            Part filePart = request.getPart("imageUpload");
            String fileName = getFileName(filePart);

            if (fileName != null && !fileName.isEmpty()) {
                imageUrl = fileName;
                String applicationPath = getServletContext().getRealPath("");
                String uploadFilePath = applicationPath + File.separator + "image";

                File uploadDir = new File(uploadFilePath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                filePart.write(uploadFilePath + File.separator + fileName);
            } else {
                // Nếu không upload ảnh mới, giữ lại ảnh cũ
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
            if (price > 10000000000L) {
                throw new Exception("Giá vật tư không được vượt quá 10 tỷ VNĐ.");
            }

            Material material = new Material();
            material.setMaterialId(materialId);
            material.setName(name);
            material.setCategoryId(categoryId);
            material.setSupplierId(supplierId);
            material.setPrice(price);
            material.setDescription(description);
            material.setImageUrl(imageUrl);
            material.setConversionId(conversionId);

            boolean success = dao.updateMaterial(material);
            if (success) {
                response.sendRedirect("MaterialListServlet");
            } else {
                request.setAttribute("errorMessage", "Cập nhật vật tư thất bại, vui lòng thử lại.");
                request.setAttribute("material", material);
                MaterialInfoDAO infoDAO = new MaterialInfoDAO();
                request.setAttribute("categories", infoDAO.getAllCategoriesForDropdown());
                request.setAttribute("suppliers", infoDAO.getAllSuppliersForDropdown());
                request.setAttribute("units", infoDAO.getAllUnitConversions());
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