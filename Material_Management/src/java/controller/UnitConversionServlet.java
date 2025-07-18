package controller;

import dao.UnitConversionDao;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;
import java.sql.Timestamp;

@WebServlet(name = "unitConversionSeverlet", urlPatterns = {"/unitConversionSeverlet"})
public class UnitConversionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UnitConversionDao dao = new UnitConversionDao();

        String cvid = request.getParameter("cvid");
        String action = request.getParameter("action");
        String search = request.getParameter("search");
//        int vtid = Integer.parseInt(request.getParameter("vtid"));
        HttpSession session = request.getSession();
        User nameandid = (User) session.getAttribute("Admin");

        String username = (nameandid != null) ? nameandid.getFullname() : "";
        String role = (nameandid != null && nameandid.getRole() != null) ? nameandid.getRole().getRolename() : "";

        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception ignored) {
        }

        if (cvid != null && action != null) {
            try {
                int unitId = Integer.parseInt(cvid);
                String oldStatus = dao.getOldStatus(unitId);
                if ("Hoạt động".equalsIgnoreCase(action) || "Không hoạt động".equalsIgnoreCase(action)) {
                    dao.updateStUnit(action, unitId);
                    request.setAttribute("messUpdate", "Trạng thái đã được cập nhật.");
                    
                    String unitName = dao.getUnitNameById(unitId);
                    UnitChangeHistory history = new UnitChangeHistory();
                    history.setUnitId(unitId);
                    history.setUnitName(unitName);
                    history.setActionType("Đổi trạng thái");
                    history.setOldValue(oldStatus);
                    history.setNewValue(action);
                    history.setChangedBy(username);
                    history.setRole(role);
                    history.setNote(" Trạng thái từ '" + oldStatus + " -> " + action + "");
                    history.setChangedAt(new Timestamp(System.currentTimeMillis()));

                    dao.insertHistory(history);

                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID không hợp lệ.");
            } catch (SQLException ex) {
                Logger.getLogger(UnitConversionServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int totalPages = dao.getcountPage();

        List<UnitConversion> list;
        if (search != null && !search.trim().isEmpty()) {
            list = dao.searchUnit(search, page);
            if (list.isEmpty()) {
                request.setAttribute("messUpdate", "Đơn vị không tồn tại!");
            }
        } else {
            list = dao.getAllUnit(page);
        }

        request.setAttribute("list", list);
        request.setAttribute("search", search);
        request.setAttribute("pages", totalPages);
        request.setAttribute("currentPage", page);

        request.getRequestDispatcher("unitManagements.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Unit management servlet";
    }
}
