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
import java.util.List;
import model.*;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

/**
 *
 * @author kien3
 */
@WebServlet(name = "ImportWarehouseServlet", urlPatterns = {"/ImportWarehouseServlet"})
public class ImportWarehouseServlet extends HttpServlet {

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
            out.println("<title>Servlet ImportWarehouseServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ImportWarehouseServlet at " + request.getContextPath() + "</h1>");
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
        User user = (User) request.getSession().getAttribute("Admin");

        String username = user.getFullname();

        response.setCharacterEncoding("UTF-8");
        UnitConversionDao dao = new UnitConversionDao();
        String action = request.getParameter("action");
        if ("getMaterialsByProject".equals(action)) {
            String project = request.getParameter("project");
            List<Material> list = dao.getMaterialsByProject(project);
            request.setAttribute("list", list);
            String json = new com.google.gson.Gson().toJson(list);
            response.setContentType("application/json");
            response.getWriter().write(json);
            return;
        }

        List<String> projectList = dao.getExportedProjects();
        request.setAttribute("projectList", projectList);
        request.setAttribute("username", username);
        request.getRequestDispatcher("importWarehouse.jsp").forward(request, response);
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
        request.setCharacterEncoding("UTF-8");
        UnitConversionDao dao = new UnitConversionDao();
        User user = (User) request.getSession().getAttribute("Admin");

        String[] namevts = request.getParameterValues("namevt[]");
        String[] numbers = request.getParameterValues("number[]");
        String[] statuses = request.getParameterValues("status[]");
        String reason = request.getParameter("reason");
        String nguoiGiao = request.getParameter("nguoiGiao");
        String nguoiNhan = request.getParameter("nguoiNhan");
        String soDienThoaiNguoiGiao = request.getParameter("soDienThoaiNguoiGiao");
        String project = request.getParameter("project");
        String roles = user != null && user.getRole() != null ? user.getRole().getRolename() : "";

        try {
            for (int i = 0; i < namevts.length; i++) {
                if (namevts[i] == null || namevts[i].trim().isEmpty()
                        || numbers[i] == null || numbers[i].trim().isEmpty()
                        || statuses[i] == null || statuses[i].trim().isEmpty()) {
                    continue;
                }
                int materialId = Integer.parseInt(namevts[i]);
                int quantity = Integer.parseInt(numbers[i]);
                String condition = statuses[i];

                int remainingQuantity = dao.getRemainingImportQuantity(project, materialId);
                if (quantity > remainingQuantity) {
                    String username = user.getFullname();
                    int exportedQuantity = dao.getExportedQuantityByProjectAndMaterial(project, materialId);
                    int importedQuantity = dao.getImportedQuantityByProjectAndMaterial(project, materialId);
                    request.setAttribute("mess", "Số lượng nhập lại (" + quantity + ") vượt quá số lượng còn lại có thể nhập (" + remainingQuantity + "). Đã xuất: " + exportedQuantity + ", Đã nhập lại: " + importedQuantity + " cho dự án này!");
                    request.setAttribute("username", username);
                    List<String> projectList = dao.getExportedProjects();
                    request.setAttribute("projectList", projectList);
                    doGet(request, response);
                    return;
                }

                Boolean update = dao.updateQuantity(materialId, condition, quantity);
                if (!update) {
                    String username = user.getFullname();
                    request.setAttribute("mess", "Nhập lại vật tư thất bại");
                    request.setAttribute("username", username);
                    List<String> projectList = dao.getExportedProjects();
                    request.setAttribute("projectList", projectList);
                    request.getRequestDispatcher("importWarehouse.jsp").forward(request, response);
                    return;
                }

                Material material = dao.getMaterialById(materialId);
                String materialName = material != null ? material.getName() : "";
                String unit = material != null ? material.getUnitName() : "";

                ImportHistory history = new ImportHistory(roles, reason, nguoiGiao, nguoiNhan, soDienThoaiNguoiGiao, project, materialName, quantity, unit, condition);
                dao.insertImportHistory(history);
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        request.setAttribute("mess", "Nhập lại vật tư thành công");
        List<String> projectList = dao.getExportedProjects();
        request.setAttribute("projectList", projectList);
        request.getRequestDispatcher("/importHistoryList").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
