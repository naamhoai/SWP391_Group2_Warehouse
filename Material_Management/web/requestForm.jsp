<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Tạo yêu cầu vật tư</title>
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/requestForm.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/awesomplete/1.1.5/awesomplete.min.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
        <script src="https://cdn.jsdelivr.net/npm/flatpickr/dist/l10n/vi.js"></script>
    </head>
    <body>
        <div class="main-layout">
            <div class="sidebar">
                <jsp:include page="sidebar.jsp" />
            </div>
            <div class="main-content">
                <div class="request-form-container">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>
                    <h2>Tạo yêu cầu xuất kho vật tư</h2>
                    <form action="createRequest" method="post" id="requestForm">
                        <div class="form-section section-request-info">
                            <h3><i class="fas fa-clipboard-list"></i> Thông tin yêu cầu</h3>
                            <div class="form-group">
                                <label class="form-label">Người làm đơn:</label>
                                <input type="text" class="input-text" value="${userName}" readonly>
                            </div>
                            <div class="form-group">
                                <label class="form-label">Lý do yêu cầu:</label>
                                <textarea name="reason" id="reason" class="input-textarea" required 
                                          pattern="[^<>\"']*"
                                          placeholder="Nhập lý do yêu cầu xuất vật tư...">${fn:escapeXml(param.reason)}</textarea>
                                <div class="error-message"></div>
                            </div>
                        </div>

                        <div class="form-section section-recipient-info">
                            <h3><i class="fas fa-user"></i> Thông tin người nhận</h3>
                            <div class="form-row">
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">Tên dự án:</label>
                                        <input type="text" name="recipientName" class="input-text" required 
                                               pattern="[^<>\"']*" 
                                               placeholder="Nhập tên dự án"
                                               value="${not empty recipientName ? fn:escapeXml(recipientName) : fn:escapeXml(param.recipientName)}" />
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">Địa chỉ giao hàng:</label>
                                        <textarea name="deliveryAddress" class="input-textarea" required 
                                                  pattern="[^<>\"']*"
                                                  placeholder="Nhập địa chỉ giao hàng chi tiết">${not empty deliveryAddress ? fn:escapeXml(deliveryAddress) : fn:escapeXml(param.deliveryAddress)}</textarea>
                                    </div>
                                </div>
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">Người liên hệ:</label>
                                        <input type="text" name="contactPerson" class="input-text" required 
                                               pattern="[^<>\"']*"
                                               placeholder="Nhập tên người liên hệ"
                                               value="${not empty contactPerson ? fn:escapeXml(contactPerson) : fn:escapeXml(param.contactPerson)}" />
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">SĐT người liên hệ:</label>
                                        <input type="tel" name="contactPhone" class="input-text" required 
                                               pattern="[0-9+\-\s()]{10,11}"
                                               placeholder="Nhập số điện thoại liên hệ"
                                               value="${not empty contactPhone ? fn:escapeXml(contactPhone) : fn:escapeXml(param.contactPhone)}" />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-section section-material-list">
                            <h3><i class="fas fa-boxes"></i> Danh sách vật tư</h3>
                            <table class="request-items-table">
                                <thead>
                                    <tr>
                                        <th class="col-material-name">Tên vật tư</th>
                                        <th class="col-quantity">Số lượng</th>
                                        <th class="col-unit">Đơn vị</th>
                                        <th class="col-condition">Tình trạng</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody id="itemsBody">
                                    <c:choose>
                                        <c:when test="${not empty materialNames}">
                                            <c:forEach var="name" items="${materialNames}" varStatus="loop">
                                                <tr>
                                                    <td>
                                                        <input name="materialName" class="input-text materialNameInput" autocomplete="off" required pattern="[^<>\"']*"
                                                               value="${fn:escapeXml(name)}" />
                                                        <div class="error-message"></div>
                                                    </td>
                                                    <td>
                                                        <input type="number" name="quantity" class="input-text" min="1" max="999999" required
                                                               value="${quantities[loop.index]}" />
                                                        <div class="error-message"></div>
                                                    </td>
                                                    <td>
                                                        <input name="unit" class="input-text unitInput" autocomplete="off" required pattern="[^<>\"']*" />
                                                        <div class="error-message"></div>
                                                    </td>
                                                    <td>
                                                        <select name="materialCondition" class="input-select" required title="Vui lòng chọn tình trạng vật tư">
                                                            <option value="Mới" ${materialConditions[loop.index] == 'Mới' ? 'selected' : ''}>Mới</option>
                                                            <option value="Cũ" ${materialConditions[loop.index] == 'Cũ' ? 'selected' : ''}>Cũ</option>
                                                            <option value="Hỏng" ${materialConditions[loop.index] == 'Hỏng' ? 'selected' : ''}>Hỏng</option>
                                                        </select>
                                                        <div class="error-message"></div>
                                                    </td>
                                                    <td class="col-action">
                                                        <button type="button" class="btn-remove" onclick="removeRow(this)">Xóa</button>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td>
                                                    <input name="materialName" class="input-text materialNameInput" autocomplete="off" required pattern="[^<>\"']*"
                                                           value="" />
                                                    <div class="error-message"></div>
                                                </td>
                                                <td>
                                                    <input type="number" name="quantity" class="input-text" min="1" max="999999" required >
                                                    <div class="error-message"></div>
                                                </td>
                                                <td>
                                                    <input name="unit" class="input-text unitInput" autocomplete="off" required pattern="[^<>\"']*" readonly/>
                                                    <div class="error-message"></div>
                                                </td>
                                                <td>
                                                    <select name="materialCondition" class="input-select" required title="Vui lòng chọn tình trạng vật tư">
                                                        <option value="Mới">Mới</option>
                                                        <option value="Cũ">Cũ</option>
                                                    </select>
                                                    <div class="error-message"></div>
                                                </td>
                                                <td>
                                                    <button type="button" class="btn-remove" onclick="removeRow(this)">Xóa</button>
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                            <button type="button" class="btn-add" onclick="addRow()">Thêm vật tư</button>
                        </div>
                        <div class="form-actions">
                            <input type="hidden" name="itemCount" id="itemCount" value="1">
                            <button type="submit" class="btn-submit">Gửi yêu cầu</button>
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
            console.log("materialNames:", window.materialNames);
            window.materialUnitMap = {
            <c:forEach var="m" items="${materialList}" varStatus="loop">
            "${fn:escapeXml(m.materialName)}": "${fn:escapeXml(m.unitName)}"<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            };
            console.log("materialUnitMap:", window.materialUnitMap);
            window.materialInventoryMap = {
            <c:forEach var="m" items="${materialList}" varStatus="loop">
            "${fn:escapeXml(m.materialName)}": ${m.quantityOnHand}<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            };
            console.log("materialInventoryMap:", window.materialInventoryMap);
        </script>
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