<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Chỉnh sửa yêu cầu vật tư</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/footer.css">
        <link rel="stylesheet" href="css/requestForm.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/awesomplete/1.1.5/awesomplete.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
        <script src="https://cdn.jsdelivr.net/npm/flatpickr/dist/l10n/vi.js"></script>
    </head>

    <body>

        <c:set var="isEditable" value="${sessionScope.roleId == 4 && request.requestStatus == 'Từ chối'}" />

        <div class="main-layout">
            <div class="sidebar">
                <%@include file="sidebar.jsp" %>
            </div>
            <div class="main-content">
                <div class="request-form-container">
                    <c:if test="${!isEditable}">
                        <div class="info-message" style="background: #fdecea; border: 1px solid #f5c6cb; padding: 12px; color: #721c24; border-radius: 6px;">
                            <strong>Thông báo:</strong> Bạn không có quyền chỉnh sửa yêu cầu này.
                        </div>
                    </c:if>

                    <h2>Chỉnh sửa yêu cầu xuất kho vật tư</h2>
                    <form action="editRequest" method="post" id="requestForm">
                        <input type="hidden" name="requestId" value="${request.requestId}">
                        <!-- Lý do -->
                        <div class="form-section section-request-info">
                            <h3><i class="fas fa-clipboard-list"></i> Thông tin yêu cầu</h3>
                            <div class="form-group">
                                <label class="form-label">Người làm đơn:</label>
                                <input type="text" class="input-text" value="${userName}" readonly>
                            </div>
                            <div class="form-group">
                                <label class="form-label">Lý do yêu cầu:</label>
                                <textarea name="reason" id="reason" class="input-textarea" 
                                          ${!isEditable ? 'readonly' : ''} 
                                          required maxlength="500" pattern="[^<>\"']*"
                                          title="Không chứa ký tự < > &quot; '">${request.reason}</textarea>
                            </div>
                        </div>

                        <div class="form-section section-recipient-info">
                            <h3><i class="fas fa-user"></i> Thông tin người nhận</h3>
                            <div class="form-row">
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">Tên dự án:</label>
                                        <input type="text" name="recipientName" class="input-text" 
                                               value="${request.recipientName}" ${!isEditable ? 'readonly' : ''}
                                               required maxlength="255" pattern="[^<>\"']*">
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">Địa chỉ giao hàng:</label>
                                        <textarea name="deliveryAddress" class="input-textarea"
                                                  ${!isEditable ? 'readonly' : ''}
                                                  required maxlength="500" pattern="[^<>\"']*">${request.deliveryAddress}</textarea>
                                    </div>
                                </div>
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">Người liên hệ:</label>
                                        <input type="text" name="contactPerson" class="input-text"
                                               value="${request.contactPerson}" ${!isEditable ? 'readonly' : ''}
                                               required maxlength="255" pattern="[^<>\"']*">
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">SĐT người liên hệ:</label>
                                        <input type="tel" name="contactPhone" class="input-text"
                                               value="${request.contactPhone}" ${!isEditable ? 'readonly' : ''}
                                               pattern="[0-9+\-\s()]{10,15}" maxlength="15"
                                               required title="10-15 số, có thể dùng + - ( ) và khoảng trắng">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <c:if test="${not empty request.directorNote}">
                            <div class="form-section">
                                <h3><i class="fas fa-comment"></i> Ghi chú từ giám đốc</h3>
                                <textarea class="input-textarea" readonly>${request.directorNote}</textarea>
                            </div>
                        </c:if>

                        <div class="form-section section-material-list">
                            <h3><i class="fas fa-boxes"></i> Danh sách vật tư</h3>
                            <table class="request-items-table">
                                <thead>
                                    <tr>
                                        <th class="col-material-name">Tên vật tư</th>
                                        <th class="col-quantity">Số lượng</th>
                                        <th class="col-unit">Đơn vị</th>
                                        <th class="col-condition">Tình trạng</th>
                                        <th class="col-action">Hành động</th>
                                    </tr>
                                </thead>
                                <tbody id="itemsBody">
                                    <c:forEach var="d" items="${details}" varStatus="loop">
                                        <tr>
                                            <td>
                                                <input type="text" name="materialName" class="input-text materialNameInput"
                                                       value="${d.materialName}" ${!isEditable ? 'readonly' : ''}
                                                       autocomplete="off" required pattern="[^<>\"']*" maxlength="100">
                                            </td>
                                            <td>
                                                <input type="number" name="quantity" class="input-text"
                                                       value="${d.quantity}" min="1" max="999999" 
                                                       ${!isEditable ? 'readonly' : ''} required>
                                            </td>
                                            <td>
                                                <input type="text" name="unit" class="input-text unitInput"
                                                       value="${d.unitName}" ${!isEditable ? 'readonly' : ''} readonly>
                                            </td>
                                            <td>
                                                <select name="materialCondition" class="input-select" ${!isEditable ? 'disabled' : ''}>
                                                    <option value="Mới" ${d.materialCondition == 'Mới' ? 'selected' : ''}>Mới</option>
                                                    <option value="Cũ" ${d.materialCondition == 'Cũ' ? 'selected' : ''}>Cũ</option>
                                                </select>
                                            </td>
                                            <td>
                                                <button type="button" class="btn-remove" onclick="removeRow(this)" 
                                                        ${!isEditable ? 'disabled' : ''}>Xóa</button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <button type="button" class="btn-add" onclick="addRow()" ${!isEditable ? 'disabled' : ''}>Thêm vật tư</button>
                        </div>

                        <input type="hidden" name="itemCount" id="itemCount" value="${fn:length(details)}">

                        <div class="form-actions">
                            <button type="submit" class="btn-submit" ${!isEditable ? 'disabled' : ''}>Gửi lại yêu cầu</button>
                            <button type="button" class="btn-back" onclick="window.history.back()">Quay lại</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script>
            window.materialNames = [
            <c:forEach var="m" items="${materialList}" varStatus="loop">
            "${fn:escapeXml(m.materialName)}"<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            ];
            window.materialUnitMap = {
            <c:forEach var="m" items="${materialList}" varStatus="loop">
            "${fn:escapeXml(m.materialName)}": "${fn:escapeXml(m.unitName)}"<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            };
            window.materialInventoryMap = {
            <c:forEach var="m" items="${materialList}" varStatus="loop">
            "${fn:escapeXml(m.materialName)}": ${m.quantityOnHand}<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            };</script>

        <!-- Scripts -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/awesomplete/1.1.5/awesomplete.min.js"></script>
        <script src="js/requestForm.js"></script>
    </body>
</html>
