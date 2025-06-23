<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chỉnh sửa đơn vị</title>
        <link rel="stylesheet" href="./css/h.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    </head>
    <body>
        <div class="container">
            <div class="card">
                <div class="header">
                    <h2 class="page-title">Chỉnh sửa đơn vị</h2>
                    <h3 style="color: red">${requestScope.messss}</h3>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label" for="type">Tên vật tư</label>
                        <input class="form-control" type="text" id="type" name="name" value="${requestScope.materialname}" readonly>
                    </div>
                </div>

                <form class="edit-form" action="unitEditseverlet" method="post">
                    <div class="form-row" style="display: flex; gap: 2rem;">
                        <div class="form-group" style="flex: 1;">
                            <label class="form-label" for="originalUnit">Đơn vị gốc</label>
                            <select class="form-control" id="originalUnit" name="baseunit">
                                <c:forEach var="l" items="${requestScope.listbase}">
                                    <option value="${l.baseunit}">${l.baseunit}</option>
                                </c:forEach> 
                            </select>
                        </div>

                        <div class="form-group" style="flex: 1;">
                            <label class="form-label" for="standardUnit">Đơn vị chuyển đổi</label>
                            <select class="form-control" id="standardUnit" name="convertedunit">
                                <c:forEach var="v" items="${requestScope.listconverted}">
                                    <option value="${v.convertedunit}">${v.convertedunit}</option>
                                </c:forEach> 
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="ratio">Tỷ lệ quy đổi</label>
                        <div class="ratio-input">
                            <input class="form-control" type="text" id="ratio" required min="1" step="1" name="unit1" placeholder="Nhập tỷ lệ">
                            <span class="ratio-separator">:</span>
                            <input class="form-control" type="text" id="ratio2" required min="1" step="1" name="unit2" placeholder="Nhập tỷ lệ">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="note">Ghi chú</label>
                        <textarea class="form-control" id="note" rows="3" placeholder="Nhập ghi chú..." name="note"></textarea>
                    </div>

                    <input type="hidden" name="baseunitid" value="${requestScope.baseunitid}"/>
                    <input type="hidden" name="materialid" value="${requestScope.materialid}"/>
                    <input type="hidden" name="materialname" value="${requestScope.materialname}"/>

                    <div class="form-actions">
                        <a href="unitConversionSeverlet"><button type="button">BACK</button></a>
                        <button type="submit">SUBMIT</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html> 