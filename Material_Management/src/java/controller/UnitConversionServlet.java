package controller;

import dao.UnitConversionDao;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Unit;

@WebServlet(name = "unitConversionSeverlet", urlPatterns = {"/unitConversionSeverlet"})
public class UnitConversionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UnitConversionDao dao = new UnitConversionDao();
        String search = request.getParameter("search");
        int page = 1;
        int pageSize = 5;
        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        List<Unit> list;
        int totalUnits;
        if (search != null && !search.trim().isEmpty()) {
            list = dao.searchUnitsByName(search.trim(), page, pageSize);
            totalUnits = dao.countUnitsByName(search.trim());
        } else {
            list = dao.getAllUnits(page, pageSize);
            totalUnits = dao.countAllUnits();
        }
        int totalPages = (int) Math.ceil((double) totalUnits / pageSize);
        request.setAttribute("list", list);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.getRequestDispatcher("unitManagements.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        UnitConversionDao dao = new UnitConversionDao();

        if ("add".equals(action)) {
            String unitName = request.getParameter("unitName");
            if (unitName != null && !unitName.trim().isEmpty()) {
                dao.addUnit(unitName.trim());
                request.setAttribute("messUpdate", "Thêm đơn vị thành công!");
            }
        } else if ("toggleStatus".equals(action)) {
            int unitId = Integer.parseInt(request.getParameter("unitId"));
            dao.toggleUnitStatus(unitId);
        } 
        response.sendRedirect("unitConversionSeverlet");
    }
}
