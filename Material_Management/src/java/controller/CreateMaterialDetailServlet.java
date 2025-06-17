package controller;

import dao.MaterialDAO;
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
import java.sql.SQLException;
import java.util.List;

@WebServlet("/CreateMaterialDetail")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class CreateMaterialDetailServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        MaterialDAO materialDAO = new MaterialDAO();
        String uploadPath = getServletContext().getRealPath("" + File.separator + "img"); // Đường dẫn lưu ảnh
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try {
            // Lấy các tham số từ form
            String materialIdStr = request.getParameter("materialId");
            String name = request.getParameter("name");
            String categoryIdStr = request.getParameter("category");
            String supplierIdStr = request.getParameter("supplier");
            String priceStr = request.getParameter("price");
            String unitConversionIdStr = request.getParameter("unitConversion");
            String quantityStr = request.getParameter("quantity");
            String description = request.getParameter("description");

            // Chuyển đổi kiểu dữ liệu
            int materialId = Integer.parseInt(materialIdStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            int supplierId = Integer.parseInt(supplierIdStr);
            BigDecimal price = new BigDecimal(priceStr);
            int unitConversionId = Integer.parseInt(unitConversionIdStr);
            int quantity = Integer.parseInt(quantityStr);

            // Xử lý upload ảnh
            String imagePath = null;
            Part filePart = request.getPart("imageUpload"); // Tên input type="file" là "imageUpload"
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = filePart.getSubmittedFileName();
                imagePath = "img/" + fileName; // Lưu đường dẫn tương đối để dễ hiển thị trên web
                filePart.write(uploadPath + File.separator + fileName);
            }

            // Tạo đối tượng Material
            Material newMaterial = new Material();
            newMaterial.setMaterialId(materialId);
            newMaterial.setName(name);
            newMaterial.setCategoryId(categoryId);
            newMaterial.setSupplierId(supplierId);
            newMaterial.setPrice(price);
            newMaterial.setConversionId(unitConversionId);
            newMaterial.setQuantity(quantity);
            newMaterial.setDescription(description);
            newMaterial.setImageUrl(imagePath);

            // Thêm vật tư vào database
            boolean success = materialDAO.addMaterial(newMaterial);

            if (success) {
                response.sendRedirect("materialDetailList.jsp?status=addSuccess"); // Chuyển hướng về trang danh sách
            } else {
                request.setAttribute("errorMessage", "Thêm vật tư thất bại, ID vật tư có thể đã tồn tại hoặc có lỗi xảy ra.");
                // Load lại dữ liệu cho dropdown nếu thất bại để người dùng không phải nhập lại
                List<Category> categories = materialDAO.getAllCategoriesForDropdown();
                List<Supplier> suppliers = materialDAO.getAllSuppliersForDropdown();
                List<UnitConversion> units = materialDAO.getAllUnitConversions();
                request.setAttribute("categories", categories);
                request.setAttribute("suppliers", suppliers);
                request.setAttribute("units", units);
                request.setAttribute("material", newMaterial); // Giữ lại dữ liệu người dùng đã nhập
                request.getRequestDispatcher("/createMaterialDetail.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu nhập vào không hợp lệ. Vui lòng kiểm tra lại ID, giá, số lượng.");
            request.getRequestDispatcher("/createMaterialDetail.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/createMaterialDetail.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            request.getRequestDispatcher("/createMaterialDetail.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Load dữ liệu cần thiết cho dropdown khi người dùng truy cập trang tạo mới
        MaterialDAO materialDAO = new MaterialDAO();
        try {
            List<Category> categories = materialDAO.getAllCategoriesForDropdown();
            List<Supplier> suppliers = materialDAO.getAllSuppliersForDropdown();
            List<UnitConversion> units = materialDAO.getAllUnitConversions();
            request.setAttribute("categories", categories);
            request.setAttribute("suppliers", suppliers);
            request.setAttribute("units", units);
            request.getRequestDispatcher("/createMaterialDetail.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Không thể tải dữ liệu danh mục, nhà cung cấp, đơn vị tính.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
} 