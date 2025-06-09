<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chỉnh sửa đơn vị</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" href="./css/editUnit.css">
    </head>
    <body>
        <div class="container">
            <div class="edit-card">
                <div class="card-header">
                    <h2>Chỉnh sửa đơn vị</h2>
                    <p class="unit-info">Thiết bị: <b></b></p>
                </div>
                <select name="caterpp">
                    <c:forEach var="c" items="${requestScope.listcat}">
                        <option value="${c.name}">${c.name}</option>
                    </c:forEach>
                </select>
                <form class="edit-form">

                    <div class="form-row">
                        <div class="form-group">
                            <label for="type">Loại</label>

                            <select id="type" name="type">
                                <c:forEach var="ls" items="${requestScope.list}">
                                    <option value="${ls.category.name}">${ls.category.name}</option>
                                </c:forEach> 
                            </select>

                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="originalUnit">Đơn vị gốc</label>
                            <select name="baseunit">
                                <c:forEach var="l" items="${requestScope.listunit}">
                                    <option value="${l.baseunit}">${l.baseunit}</option>
                                </c:forEach> 
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="standardUnit">Đơn vị chuyển đổi</label>
                            <select id="standardUnit" name="convertedunit">
                                <c:forEach var="v" items="${requestScope.list}">
                                    <option value="${v.convertedunit}">${v.convertedunit}</option>
                                </c:forEach> 
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="ratio">Tỷ lệ quy đổi</label>
                        <div class="ratio-input">
                            <input type="text" id="ratio" required min="1" step="1" name="unit1">
                            <span class="ratio-separator">:</span>
                            <input type="text" id="ratio2" required min="1" step="1" name="unit2">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="note">Ghi chú</label>
                        <textarea id="note" rows="3" placeholder="Nhập ghi chú..." name="note"></textarea>
                    </div>
                       
                    <div>
                        <a href="unitConversionSeverlet">BACK</a>
                        <button type="submit">
                            Sumbit
                        </button>

                    </div>
                </form>
            </div>
        </div>
    </body>
</html> 