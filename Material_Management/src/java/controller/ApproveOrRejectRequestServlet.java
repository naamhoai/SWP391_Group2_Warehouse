package controller;

import dao.NotificationDAO;
import dao.RequestDAO;
import dao.RequestHistoryDAO;
import dao.RequestDetailDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import model.RequestHistory;
import model.RequestDetail;
import model.RequestHistoryDetail;
import java.util.List;
import java.util.stream.Collectors;

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

            RequestDAO requestDAO = new RequestDAO();
            NotificationDAO notificationDAO = new NotificationDAO();

            if ("approve".equals(action)) {
                requestDAO.updateStatusAndNote(requestId, "Đã duyệt", directorNote);
                int warehouseStaffId = requestDAO.getWarehouseStaffId();
                if (warehouseStaffId != -1) {
                    notificationDAO.addNotification(
                            warehouseStaffId,
                            "Yêu cầu vật tư #" + requestId + " đã được phê duyệt. Vui lòng tiến hành xuất kho.",
                            requestId
                    );
                }

                int directorId = (Integer) request.getSession().getAttribute("userId");
                RequestHistoryDAO historyDAO = new RequestHistoryDAO();
                String lastEmployeeReason = historyDAO.getLastEmployeeChangeReason(requestId, 2);
                RequestHistory history = new RequestHistory();
                history.setRequestId(requestId);
                history.setChangedBy(directorId);
                history.setOldStatus("Chờ duyệt");
                history.setNewStatus("Đã duyệt");
                history.setAction("Duyệt yêu cầu");
                history.setChangeReason(lastEmployeeReason);
                history.setDirectorNote(directorNote);

                RequestDetailDAO detailDAO = new RequestDetailDAO();
                List<RequestDetail> requestDetails = detailDAO.getRequestDetailsByRequestId(requestId);
                List<RequestHistoryDetail> historyDetails = requestDetails.stream().map(d -> {
                    RequestHistoryDetail hd = new RequestHistoryDetail();
                    hd.setMaterialId(d.getMaterialId() != null ? d.getMaterialId() : 0);
                    hd.setMaterialName(d.getMaterialName());
                    hd.setQuantity(d.getQuantity());
                    hd.setWarehouseUnitId(d.getWarehouseUnitId());
                    hd.setMaterialCondition(d.getMaterialCondition());
                    return hd;
                }).collect(Collectors.toList());
                history.setHistoryDetails(historyDetails);
                historyDAO.addRequestHistory(history);

                String successMsg = "Yêu cầu #" + requestId + " đã được duyệt thành công!";
                String encodedMsg = URLEncoder.encode(successMsg, StandardCharsets.UTF_8.toString());
                response.sendRedirect(request.getContextPath() + "/RequestListServlet?success=" + encodedMsg);
                return;

            } else if ("reject".equals(action)) {
                requestDAO.updateStatusAndNote(requestId, "Từ chối", directorNote);
                int staffUserId = requestDAO.getRequestCreatorId(requestId);
                notificationDAO.addNotification(
                        staffUserId,
                        "Yêu cầu vật tư #" + requestId + " đã bị từ chối. Lý do: " + directorNote + " Vui lòng chỉnh sửa và gửi lại.",
                        requestId
                );

                int directorId = (Integer) request.getSession().getAttribute("userId");
                RequestHistoryDAO historyDAO = new RequestHistoryDAO();
                String lastEmployeeReason = historyDAO.getLastEmployeeChangeReason(requestId, 2);
                RequestHistory history = new RequestHistory();
                history.setRequestId(requestId);
                history.setChangedBy(directorId);
                history.setOldStatus("Chờ duyệt");
                history.setNewStatus("Từ chối");
                history.setAction("Từ chối yêu cầu");
                history.setChangeReason(lastEmployeeReason);
                history.setDirectorNote(directorNote);
                RequestDetailDAO detailDAO = new RequestDetailDAO();
                List<RequestDetail> requestDetails = detailDAO.getRequestDetailsByRequestId(requestId);
                List<RequestHistoryDetail> historyDetails = requestDetails.stream().map(d -> {
                    RequestHistoryDetail hd = new RequestHistoryDetail();
                    hd.setMaterialId(d.getMaterialId() != null ? d.getMaterialId() : 0);
                    hd.setMaterialName(d.getMaterialName());
                    hd.setQuantity(d.getQuantity());
                    hd.setWarehouseUnitId(d.getWarehouseUnitId());
                    hd.setMaterialCondition(d.getMaterialCondition());
                    return hd;
                }).collect(Collectors.toList());
                history.setHistoryDetails(historyDetails);
                historyDAO.addRequestHistory(history);
                String rejectMsg = "Yêu cầu #" + requestId + " đã được duyệt thành công!";
                String encodedRejectMsg = URLEncoder.encode(rejectMsg, StandardCharsets.UTF_8.toString());
                response.sendRedirect(request.getContextPath() + "/RequestListServlet?success=" + encodedRejectMsg);
                return;
            } else {
                request.setAttribute("error", "Hành động không hợp lệ.");
                request.getRequestDispatcher("viewRequestDetail.jsp?requestId=" + requestId).forward(request, response);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Lỗi khi xử lý yêu cầu: " + e.getMessage());
        }
    }
}
