package controller;

import dao.CategoryDAO;
import dao.RequestDAO;
import dao.RequestDetailDAO;
import dao.UserDAO;
import model.Request;
import model.RequestDetail;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.User;

@WebServlet(name = "ViewRequestDetailServlet", urlPatterns = {"/viewRequestDetail"})
public class ViewRequestDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            RequestDAO requestDAO = new RequestDAO();
            RequestDetailDAO detailDAO = new RequestDetailDAO();
            CategoryDAO categoryDAO = new CategoryDAO();
            UserDAO userDAO = new UserDAO(); // Thêm dòng này

            Request req = requestDAO.getRequestById(requestId);
            List<RequestDetail> details = detailDAO.getRequestDetailsByRequestId(requestId);

            // Lấy tên người yêu cầu
            String requesterName = "";
            if (req != null) {
                User user = userDAO.getUserById(req.getUserId());
                requesterName = (user != null) ? user.getFullname() : "Không xác định";
            }

            request.setAttribute("request", req);
            request.setAttribute("details", details);
            request.setAttribute("requesterName", requesterName); // Thêm dòng này
            request.getRequestDispatcher("viewRequestDetail.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải chi tiết yêu cầu: " + e.getMessage());
        }
    }
}
