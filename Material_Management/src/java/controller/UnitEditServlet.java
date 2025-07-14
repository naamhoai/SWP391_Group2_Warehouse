/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UnitConversionDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.*;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kien3
 */
@WebServlet(name = "unitEditseverlet", urlPatterns = {"/unitEditseverlet"})
public class UnitEditServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet unitEditseverlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet unitEditseverlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UnitConversionDao dao = new UnitConversionDao();

        List<Unit> supplierUnits = dao.getSupplierUnits();
        Map<Integer, List<UnitConversion>> mapBaseUnits = new HashMap<>();

        for (Unit supplier : supplierUnits) {
            List<UnitConversion> baseUnits = dao.getBaseUnitsBySupplier(supplier.getUnit_id());
            System.out.println("supplier.getUnit_id()" + supplier.getUnit_id());
            mapBaseUnits.put(supplier.getUnit_id(), baseUnits);
        }

        request.setAttribute("supplierUnits", supplierUnits);
        request.setAttribute("mapBaseUnits", mapBaseUnits);

        request.getRequestDispatcher("editUnit.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UnitConversionDao dao = new UnitConversionDao();
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User nameandid = (User) session.getAttribute("Admin");  // đảm bảo đã login và lưu "Admin" là Users

        String username = (nameandid != null) ? nameandid.getFullname() : "Rỗng";
        String role = (nameandid != null && nameandid.getRole() != null) ? nameandid.getRole().getRolename() : "Rỗng";
        

        Map<String, String[]> paramMap = request.getParameterMap();

        for (String paramName : paramMap.keySet()) {
            if (paramName.startsWith("conversion_")) {
                try {
                    String unitIdStr = paramName.substring("conversion_".length());
                    int supplierUnitId = Integer.parseInt(unitIdStr);

                    String conversionStr = request.getParameter("conversion_" + unitIdStr);
                    if (conversionStr == null || conversionStr.trim().isEmpty()) {
                        continue;
                    }

                    String warehouseStr = request.getParameter("warehouse_" + unitIdStr);
                    if (warehouseStr == null || warehouseStr.trim().isEmpty()) {
                        continue;
                    }
                    int warehouseUnitId = Integer.parseInt(warehouseStr);

                    // Lấy giá trị cũ từ DB
                    String oldValue = dao.getOldConversionValue(warehouseUnitId, supplierUnitId);

                    // Nếu giá trị mới khác với cũ → cập nhật + ghi lịch sử
                    if (!conversionStr.equals(oldValue)) {
                        // Cập nhật giá trị mới
                        dao.Update(conversionStr, supplierUnitId, warehouseUnitId);

                        // Lấy tên đơn vị động từ DB
                        String unitName = dao.getUnitNameById(supplierUnitId);
                        String unitName2 = dao.getUnitNameById(warehouseUnitId);
                        // Ghi lịch sử
                        UnitChangeHistory history = new UnitChangeHistory();
                        history.setUnitId(supplierUnitId);
                        history.setUnitName(unitName);
                        history.setActionType("Cập nhật tỉ lệ.");
                        history.setOldValue(oldValue);
                        history.setNewValue(conversionStr);
                        history.setChangedBy(username); // Có thể lấy từ session
                        history.setRole(role);
                        history.setNote("1 " + unitName + " = " + conversionStr +" "+ unitName2 );
                        history.setChangedAt(new Timestamp(System.currentTimeMillis()));

                        dao.insertHistory(history);
                    }

                } catch (SQLException | NumberFormatException ex) {
                    Logger.getLogger(UnitEditServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        
        response.sendRedirect("unitConversionSeverlet");
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý chỉnh sửa đơn vị và lưu lịch sử thay đổi.";
    }
}
