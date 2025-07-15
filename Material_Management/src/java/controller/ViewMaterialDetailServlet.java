package controller;

import dao.MaterialDAO;
import model.Material;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet(name = "ViewMaterialDetailServlet", urlPatterns = {"/viewMaterialDetail"})
public class ViewMaterialDetailServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int materialId = Integer.parseInt(idStr);
                MaterialDAO dao = new MaterialDAO();
                Material material = dao.getMaterialById(materialId);
                
                if (material != null) {
                    request.setAttribute("material", material);
                    request.getRequestDispatcher("viewMaterialDetail.jsp").forward(request, response);
                } else {
                    response.sendRedirect("error.jsp");
                }
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp");
            }
        } else {
            response.sendRedirect("error.jsp");
        }
    }
} 