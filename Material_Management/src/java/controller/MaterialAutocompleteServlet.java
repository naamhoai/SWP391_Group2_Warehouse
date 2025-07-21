package controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import dao.MaterialDAO;
import model.Material;

@WebServlet("/MaterialAutocompleteServlet")
public class MaterialAutocompleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        List<Material> result = new java.util.ArrayList<>();
        if (query != null && !query.trim().isEmpty()) {
            MaterialDAO dao = new MaterialDAO();
            result = dao.searchMaterialsByName(query.trim());
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(result));
    }
} 