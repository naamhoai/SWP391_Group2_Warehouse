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
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class UpdateMaterialServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            // Lấy dữ liệu từ các trường của form
            int materialId = Integer.parseInt(request.getParameter("materialId"));
            String name = request.getParameter("name");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            String description = request.getParameter("description");

            // Lấy URL ảnh hiện tại (nếu có) để giữ lại nếu không có ảnh mới được upload
            // Có thể cần lấy từ DB hoặc từ một trường ẩn trong form nếu không thay đổi
            // Hiện tại, chúng ta sẽ bỏ qua trường hợp này và chỉ xử lý khi có upload mới.
            String imageUrl = null; // Mặc định là null, sẽ được cập nhật nếu có file mới

            // Lấy conversionId hiện tại từ DB để giữ nguyên đơn vị
            MaterialDAO dao = new MaterialDAO();
            Material existingMaterial = dao.getMaterialById(materialId);
            int conversionId = 0;
            if (existingMaterial != null) {
                conversionId = existingMaterial.getConversionId();
            }

            // Xử lý upload ảnh mới
            Part filePart = request.getPart("imageUpload"); // Tên input trong JSP
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

            // Tạo đối tượng Material mới với dữ liệu cập nhật
            Material material = new Material();
            material.setMaterialId(materialId);
            material.setName(name);
            material.setCategoryId(categoryId);
            material.setSupplierId(supplierId);
            material.setPrice(price);
            material.setDescription(description);
            material.setImageUrl(imageUrl);
            material.setConversionId(conversionId);

            // Gọi DAO để cập nhật vào DB
            boolean success = dao.updateMaterial(material);

            // Chuyển hướng người dùng về trang danh sách với thông báo
            if (success) {
                response.sendRedirect("MaterialListServlet");
            } else {
                request.setAttribute("errorMessage", "Cập nhật vật tư thất bại, vui lòng thử lại.");
                // Cần load lại dữ liệu cho form nếu thất bại
                request.setAttribute("material", material); // Giữ lại dữ liệu người dùng đã nhập
                // Lấy lại các dropdown data nếu cần
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