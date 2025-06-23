<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thêm mới đơn vị đơn giản</title>
        <link rel="stylesheet" href="./css/createUnit.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1 class="page-title">Thêm mới đơn vị</h1>
                <h2 style="color: red">${requestScope.mess}</h2>
                
            </div>

            <div class="card">
                <form action="CreateUnitServlet" method="post">
                    <div class="form-group">
                        <label class="form-label" for="materials">Tên vật tư:</label>
                        <input class="form-control" list="materials" id="materials-input" name="materialid" placeholder="Nhập tên vật tư..." required>
                        <datalist id="materials">
                            <c:forEach var="item" items="${list}">
                                <option value="${item.materialId}-${item.name}" />
                            </c:forEach>
                        </datalist>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="baseunit">Tên đơn vị nhập vào:</label>
                        <input type="text" class="form-control" id="baseunit" name="baseunit" placeholder="Nhập tên đơn vị..." required>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="ratio">Tỉ lệ quy đổi:</label>
                        <div class="ratio-input">
                            <input type="text" class="form-control" id="ratio" name="unit1" min="1" step="1" placeholder="Nhập tỷ lệ" required>
                            <span class="ratio-separator">:</span>
                            <input type="text" class="form-control" id="ratio2" name="unit2" min="1" step="1" placeholder="Nhập tỷ lệ" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="convertedunit">Đơn vị quy đổi ra:</label>
                        <input type="text" class="form-control" id="convertedunit" name="convertedunit" placeholder="Nhập đơn vị quy đổi..." required>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="note">Ghi chú:</label>
                        <textarea class="form-control" id="note" name="note" rows="3" placeholder="Nhập ghi chú..."></textarea>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn-primary">Lưu</button>
                        <a href="unitConversionSeverlet"><button type="button" class="btn-secondary">Hủy</button></a>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html> 
</html> 