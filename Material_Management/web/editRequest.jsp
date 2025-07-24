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
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
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

                        <!-- Thông tin yêu cầu -->
                        <div class="form-section section-request-info">
                            <h3><i class="fas fa-clipboard-list"></i> Thông tin yêu cầu</h3>
                            <div class="form-group">
                                <label class="form-label">Người làm đơn:</label>
                                <input type="text" class="input-text" value="${userName}" readonly>
                            </div>
                            <div class="form-group">
                                <label class="form-label">Lý do yêu cầu:</label>
                                <textarea name="reason" class="input-textarea" ${!isEditable ? 'readonly' : ''} required>${request.reason}</textarea>
                            </div>
                        </div>

                        <!-- Thông tin người nhận -->
                        <div class="form-section section-recipient-info">
                            <h3><i class="fas fa-user"></i> Thông tin người nhận</h3>
                            <div class="form-row">
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">Tên dự án:</label>
                                        <input type="text" name="recipientName" class="input-text" value="${request.recipientName}" ${!isEditable ? 'readonly' : ''} required>
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">Địa chỉ giao hàng:</label>
                                        <textarea name="deliveryAddress" class="input-textarea" ${!isEditable ? 'readonly' : ''} required>${request.deliveryAddress}</textarea>
                                    </div>
                                </div>
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">Người liên hệ:</label>
                                        <input type="text" name="contactPerson" class="input-text" value="${request.contactPerson}" ${!isEditable ? 'readonly' : ''} required>
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">SĐT người liên hệ:</label>
                                        <input type="text" name="contactPhone" class="input-text" value="${request.contactPhone}" ${!isEditable ? 'readonly' : ''} required pattern="0[0-9]{9,10}" maxlength="11">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Ghi chú từ giám đốc -->
                        <c:if test="${not empty request.directorNote}">
                            <div class="form-section">
                                <h3><i class="fas fa-comment"></i> Ghi chú từ giám đốc</h3>
                                <textarea class="input-textarea" readonly>${request.directorNote}</textarea>
                            </div>
                        </c:if>

                        <!-- Danh sách vật tư -->
                        <div class="form-section section-material-list">
                            <h3><i class="fas fa-boxes"></i> Danh sách vật tư</h3>
                            <table class="request-items-table">
                                <thead>
                                   <tr>
                                        <th class="col-material-name">Tên vật tư</th>
                                        <th class="col-quantity">Số lượng</th>
                                        <th class="col-unit">Tồn kho</th>
                                        <th class="col-unit">Đơn vị</th>
                                        <th class="col-condition">Tình trạng</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody id="itemsBody">
                                    <c:forEach var="d" items="${details}" varStatus="loop">
                                        <tr>
                                            <td>
                                                <input type="text" name="materialName" class="input-text materialNameInput" value="${d.materialName}" ${!isEditable ? 'readonly' : ''} autocomplete="off" required>
                                                <div class="error-message"></div>
                                            </td>
                                            <td>
                                                <input type="number" name="quantity" class="input-text" value="${d.quantity}" min="1" max="999999" ${!isEditable ? 'readonly' : ''} required>
                                                <div class="error-message"></div>
                                            </td>
                                            <td>
                                                <input type="text" class="input-text inventoryInput" readonly />
                                            </td>
                                            <td>
                                                <input type="text" class="input-text unitInput" value="${d.unitName}" readonly>
                                            </td>
                                            <td>
                                                <select name="materialCondition" class="input-select" ${!isEditable ? 'disabled' : ''}>
                                                    <option value="Mới" ${d.materialCondition == 'Mới' ? 'selected' : ''}>Mới</option>
                                                    <option value="Cũ" ${d.materialCondition == 'Cũ' ? 'selected' : ''}>Cũ</option>
                                                </select>
                                            </td>
                                            <td>
                                                <button type="button" class="btn-remove"
                                                        onclick="${isEditable ? 'removeRow(this)' : ''}"
                                                        style="${!isEditable ? 'pointer-events:none;opacity:0.6;cursor:not-allowed;' : ''}">
                                                    Xóa
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <button type="button" class="btn-add"
                                    onclick="${isEditable ? 'addRow()' : ''}"
                                    style="${!isEditable ? 'pointer-events:none;opacity:0.6;cursor:not-allowed;' : ''}">
                                Thêm vật tư
                            </button>
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
            window.materialInventoryMap = (function() {
            const map = {};
            <c:forEach var="m" items="${materialList}">
            (function() {
            const name = "${fn:escapeXml(m.materialName)}";
            const condition = "${fn:escapeXml(m.materialCondition)}".toLowerCase();
            const qty = ${m.quantityOnHand};
            if (!map[name]) map[name] = {};
            map[name][condition] = qty;
            })();
            </c:forEach>
            return map;
            })();
            console.log("materialNames:", window.materialNames);
            console.log("materialUnitMap:", window.materialUnitMap);
            console.log("materialInventoryMap:", window.materialInventoryMap);</script>

        <script src="js/sidebar.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/awesomplete/1.1.5/awesomplete.min.js"></script>
        <script src="js/requestForm.js"></script>
        <script>
            document.addEventListener("DOMContentLoaded", function() {
            setupAwesompleteInputs();
            });
        </script>
    </body>
</html>