<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thêm mới đơn vị đơn giản</title>
        <link rel="stylesheet" href="./css/createUnit.css">
        <style>
            .form-actions {
                display: flex;
                align-items: center;
                justify-content: space-between;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1 class="page-title">Thêm mới đơn vị</h1>
                <h2 style="color: red">${requestScope.mess}</h2>
                <a href="pure-management-unit.html" class="back-button">

                </a>
            </div>

            <div class="card">
                <form action="CreateUnitServlet" method="post">
                    <input list="materials" name="materialid" placeholder="Nhập tên vật tư..." required>
                    <datalist id="materials">
                        <c:forEach var="item" items="${list}">
                            <option value="${item.materialId}-${item.name}" />
                        </c:forEach>
                    </datalist>
            </div>

            <div class="form-group">
                <label class="form-label">Tên đơn vị nhập vào:</label>
                <input type="text" class="form-control" name="baseunit" required>
            </div>

            <div class="form-group ratio-input">
                <label class="form-label">Tỉ lệ quy đổi:</label>
                <input type="text" id="ratio" name="unit1" min="1" step="1" required>
                <span class="ratio-separator">:</span>
                <input type="text" id="ratio2" name="unit2" min="1" step="1"  required >
            </div>

            <div class="form-group ratio-input">
                <label class="form-label">Đơn vị quy đổi ra:</label>
                <input type="text" class="form-control" name="convertedunit" required >
            </div>


            <div class="form-group">
                <label class="form-label">Ghi chú</label>
                <textarea class="form-control" name="note" rows="3"></textarea>
            </div>
            <div class="form-actions">
                <button type="submit">Lưu</button>
                <a href="unitConversionSeverlet"><button type="button">Hủy</button></a>
            </div>
        </form>



    </div>
</body>
</html> 