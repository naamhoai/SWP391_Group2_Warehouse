package controller;

import dao.CategoryDAO;
import dao.RequestDAO;
import dao.RequestDetailDAO;
import model.Request;
import model.RequestDetail;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewRequestDetailServlet", urlPatterns = {"/viewRequestDetail"})
public class ViewRequestDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            RequestDAO requestDAO = new RequestDAO();
            RequestDetailDAO detailDAO = new RequestDetailDAO();
            CategoryDAO categoryDAO = new CategoryDAO();

            Request req = requestDAO.getRequestById(requestId);
            List<RequestDetail> details = detailDAO.getRequestDetailsByRequestId(requestId);

            // Set tên danh mục cha và con cho từng detail
            for (RequestDetail d : details) {
                d.setParentCategoryName(categoryDAO.getCategoryNameById(d.getParentCategoryId()));
                d.setCategoryName(categoryDAO.getCategoryNameById(d.getCategoryId()));
            }

            request.setAttribute("request", req);
            request.setAttribute("details", details);
            request.getRequestDispatcher("viewRequestDetail.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải chi tiết yêu cầu: " + e.getMessage());
        }
    }
}
