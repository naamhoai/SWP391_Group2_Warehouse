<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thêm mới đơn vị đơn giản</title>
        <link rel="stylesheet" href="./css/createUnit.css">
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1 class="page-title">Thêm mới đơn vị</h1>
                <h2 style="color: red">${requestScope.messs}</h2>
                <a href="pure-management-unit.html" class="back-button">

                </a>
            </div>

            <div class="card">
                <form action="CreateUnitServlet" method="post">


                    <div class="form-group">
                        <label class="form-label">tên vật tư </label>
                        <select class="form-control"  name="materialid">
                            <option value="All">-- Chọn loại thiết bị --</option>
                            <c:forEach var="list" items="${list}">
                                <option value="${list.materialId}">${list.name}</option>
                            </c:forEach>

                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Tên đơn vị</label>
                        <input type="text" class="form-control" name="baseunit" required>
                    </div>

                    <div class="form-group ratio-input">
                        <label class="form-label">Tỉ lệ quy đổi</label>
                        <input type="text" id="ratio" name="unit1" min="1" step="1" required>
                        <span class="ratio-separator">:</span>
                        <input type="text" id="ratio2" name="unit2" min="1" step="1"  required >
                    </div>

                    <div class="form-group ratio-input">
                        <label class="form-label">Đơn vị quy đổi</label>
                        <input type="text" class="form-control" name="convertedunit" required >
                    </div>


                    <div class="form-group">
                        <label class="form-label">Ghi chú</label>
                        <textarea class="form-control" name="note" rows="3"></textarea>
                    </div>
                     <button type="submit" class="btn btn-save">Lưu</button>
                </form>
                
                <a href="unitConversionSeverlet"><button class="btn btn-back">Hủy</button></a>
                
            </div>
    </body>
</html> 