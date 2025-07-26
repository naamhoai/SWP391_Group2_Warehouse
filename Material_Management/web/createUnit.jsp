<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thêm đơn vị quy đổi</title>
        <link rel="stylesheet" href="./css/createUnit.css">
        <link rel="stylesheet" href="add-unit-conversion.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    </head>
    <body>
        <div class="container">
            <h1><i class="fas fa-plus-circle"></i> Thêm đơn vị mới</h1>
            <h2 style="color: red">${requestScope.mess}</h2>
            <form id="create-unit-form" action="CreateUnitServlet" method="post">
                <table class="conversion-table">
                    <thead>
                        <tr>
                            <th>TÊN ĐƠN VỊ MỚI</th>
                            <th>TRẠNG THÁI</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><input type="text" name="unitName" placeholder="VD: Mét, Kg, Cái..." required maxlength="20" pattern="^[\p{L}0-9\s]{1,20}$" title="Chỉ nhập chữ, số, không nhập kí tự đặc biệt, tối đa 20 ký tự."></td>
                            <td>
                                <select name="status" required>
                                    <option value="Hoạt động">Hoạt động</option>
                                    <option value="Không hoạt động">Không hoạt động</option>
                                </select>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div class="form-actions">
                    <button type="submit" class="btn-add-row"><i class="fas fa-plus"></i> Tạo mới </button>
                    <button type="button" class="btn-add-row"> <a href="unitConversionSeverlet" class="btn-add-row">
                            <i class="fas fa-arrow-left"></i> Trở về
                        </a> </button>
                </div>
            </form>
        </div>
        <script>
            document.getElementById('create-unit-form').addEventListener('submit', function (e) {
                var unitName = document.querySelector('input[name="unitName"]').value.trim();
                if (!window.confirm('Bạn có chắc chắn muốn tạo đơn vị mới: ' + unitName + ' ?')) {
                    e.preventDefault();
                }
            });
        </script>
    </body>
</html> 