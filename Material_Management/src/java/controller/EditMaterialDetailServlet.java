package controller;

import dao.MaterialDAO;
import dao.MaterialInfoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import model.Material;
import model.Supplier;
import model.UnitConversion;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/editMaterialDetail")
public class EditMaterialDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int materialId = Integer.parseInt(request.getParameter("id"));
            MaterialDAO materialDAO = new MaterialDAO();
            MaterialInfoDAO infoDAO = new MaterialInfoDAO();

            // Lấy thông tin vật tư cần chỉnh sửa
            Material material = materialDAO.getMaterialById(materialId);

            // Lấy danh sách các danh mục, nhà cung cấp, đơn vị tính cho dropdown
            List<Category> categories = infoDAO.getAllCategoriesForDropdown();
            List<Supplier> suppliers = infoDAO.getAllSuppliersForDropdown();
            List<UnitConversion> units = infoDAO.getAllUnitConversions();

            // Đặt các đối tượng vào request scope
            request.setAttribute("material", material);
            request.setAttribute("categories", categories);
            request.setAttribute("suppliers", suppliers);

            // Chuyển tiếp đến trang JSP chỉnh sửa
            request.getRequestDispatcher("/updateMaterialDetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID vật tư không hợp lệ.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
} 