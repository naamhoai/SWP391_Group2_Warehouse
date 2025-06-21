package controller;

import dao.NotificationDAO;
import dao.RequestDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ApproveOrRejectRequestServlet", urlPatterns = {"/approveOrRejectRequest"})
public class ApproveOrRejectRequestServlet extends HttpServlet {

    private boolean isInvalid(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String requestIdRaw = request.getParameter("requestId");
            String directorNote = request.getParameter("directorNote");
            String action = request.getParameter("action");

            if (isInvalid(requestIdRaw) || isInvalid(action)) {
                request.setAttribute("error", "Thiếu thông tin bắt buộc.");
                request.getRequestDispatcher("viewRequestDetail.jsp?requestId=" + requestIdRaw).forward(request, response);
                return;
            }

            int requestId;
            try {
                requestId = Integer.parseInt(requestIdRaw);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Mã yêu cầu không hợp lệ.");
                request.getRequestDispatcher("viewRequestDetail.jsp?requestId=" + requestIdRaw).forward(request, response);
                return;
            }

            if (isInvalid(directorNote)) {
                request.setAttribute("error", "Lý do không được để trống.");
                request.getRequestDispatcher("viewRequestDetail.jsp?requestId=" + requestId).forward(request, response);
                return;
            }

            if (directorNote.length() > 500) {
                request.setAttribute("error", "Lý do không được vượt quá 500 ký tự.");
                request.getRequestDispatcher("viewRequestDetail.jsp?requestId=" + requestId).forward(request, response);
                return;
            }

            if (!directorNote.matches("^[a-zA-Z0-9À-ỹ\\s\\-_\\(\\)\\.,!?;:]+$")) {
                request.setAttribute("error", "Lý do chứa ký tự không hợp lệ. Chỉ cho phép chữ cái, số, khoảng trắng, dấu câu và một số ký tự đặc biệt.");
                request.getRequestDispatcher("viewRequestDetail.jsp?requestId=" + requestId).forward(request, response);
                return;
            }

            RequestDAO requestDAO = new RequestDAO();
            NotificationDAO notificationDAO = new NotificationDAO();

            if ("approve".equals(action)) {
                requestDAO.updateStatusAndNote(requestId, "Approved", directorNote);
                int warehouseStaffId = requestDAO.getWarehouseStaffId();
                if (warehouseStaffId != -1) {
                    notificationDAO.addNotification(
                            warehouseStaffId,
                            "Yêu cầu vật tư #" + requestId + " đã được phê duyệt. Vui lòng tiến hành nhập hàng.",
                            requestId
                    );
                }
            } else if ("reject".equals(action)) {
                requestDAO.updateStatusAndNote(requestId, "Rejected", directorNote);
                int staffUserId = requestDAO.getRequestCreatorId(requestId);
                notificationDAO.addNotification(
                        staffUserId,
                        "Yêu cầu vật tư #" + requestId + " đã bị từ chối. Lý do: " + directorNote + " Vui lòng chỉnh sửa và gửi lại.",
                        requestId
                );
            } else {
                request.setAttribute("error", "Hành động không hợp lệ.");
                request.getRequestDispatcher("viewRequestDetail.jsp?requestId=" + requestId).forward(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/director");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Lỗi khi xử lý yêu cầu: " + e.getMessage());
        }
    }
}
