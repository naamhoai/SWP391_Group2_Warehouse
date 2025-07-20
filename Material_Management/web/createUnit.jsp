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
            <h1><i class="fas fa-plus-circle"></i> Thêm đơn vị quy đổi mới</h1>
            <p class="subtitle">Tạo các đơn vị mới và quy đổi về 1 trong 4 đơn vị gốc: cái, lít, kg, mét</p>
            <h2 style="color: red">${requestScope.mess}</h2>
            <form id="conversion-form" action="CreateUnitServlet" method="post">
                <table class="conversion-table">
                    <thead>
                        <tr>
                            <th>TÊN ĐƠN VỊ MỚI</th>
                            <th>MÔ TẢ</th>
                            <th>ĐƠN VỊ LƯU KHO</th>
                            <th>TỈ LỆ QUY ĐỔI</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody id="conversion-tbody">
                        <tr>
                            <td><input type="text" name="unitName" placeholder="VD: Thùng, Hộp..."
                                       required maxlength="10"
                                       pattern="^[\p{L}0-9\s]{1,10}$"
                                       title="Chỉ nhập chữ, số, không nhập kí tự đặc biệt, tối đa 10 ký tự."
                                       ></td>
                            <td><input type="text" name="unitDesc" placeholder="Mô tả"></td>
                            <td>
                                <select name="baseUnitID" required>
                                    <option value="">Chọn đơn vị lưu kho</option>
                                    <option value="1">Cái</option>
                                    <option value="2">Mét</option>
                                    <option value="3">Kg</option>
                                    <option value="4">Lít</option>


                                </select>
                            </td>
                            <td><input type="number" name="ratio" min="1" step="any" placeholder="Tỉ lệ" required></td>

                        </tr>
                    </tbody>
                </table>
                <div class="form-actions">
                    <button type="submit"  class="btn-add-row"><i class="fas fa-plus"></i>Tạo mới </button>
                    <button type="button"  class="btn-add-row"> <a href="unitConversionSeverlet" class="btn-add-row">
                            <i class="fas fa-plus"></i> Trở về
                        </a> </button>
                </div>

            </form>
        </div>
        <script>
            document.getElementById('conversion-form').addEventListener('submit', function (e) {
                var unitName = document.querySelector('input[name="unitName"]').value.trim().toLowerCase();
                var validUnits = ['thùng', 'cuộn', 'hộp', 'gói'];
                if (validUnits.includes(unitName)) {
                    if (!confirm('Bạn có chắc chắn muốn tạo đơn vị quy đổi mới?')) {
                        e.preventDefault();
                    }
                } else {
                    if (!confirm('Đơn vị không đúng, bạn có chắc chắn muốn tạo không?')) {
                        e.preventDefault();
                    }
                }
            });
        </script>
    </body>
</html> 