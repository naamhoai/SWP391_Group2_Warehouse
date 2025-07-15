package controller;

import dao.ExportFormDAO;
import dao.ExportMaterialDAO;
import dao.DeliveryDAO;
import model.ExportForm;
import model.ExportMaterial;
import model.Delivery;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ExportFormDetailServlet", urlPatterns = {"/exportFormDetail"})
public class ExportFormDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int exportFormId = Integer.parseInt(request.getParameter("exportId"));
            ExportFormDAO exportFormDAO = new ExportFormDAO();
            ExportMaterialDAO exportMaterialDAO = new ExportMaterialDAO();
            DeliveryDAO deliveryDAO = new DeliveryDAO();

            ExportForm exportForm = exportFormDAO.getExportFormById(exportFormId);
            List<ExportMaterial> exportMaterials = exportMaterialDAO.getExportMaterialsByExportId(exportFormId);
            Delivery delivery = deliveryDAO.getDeliveryByExportId(exportFormId);

            request.setAttribute("exportForm", exportForm);
            request.setAttribute("exportMaterials", exportMaterials);
            request.setAttribute("delivery", delivery);
            request.getRequestDispatcher("exportFormDetail.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi lấy chi tiết phiếu xuất kho: " + e.getMessage());
            request.getRequestDispatcher("exportFormDetail.jsp").forward(request, response);
        }
    }
} 