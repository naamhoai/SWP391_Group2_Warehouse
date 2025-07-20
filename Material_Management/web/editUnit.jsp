<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Chỉnh sửa tỉ lệ đơn vị</title>
        <link rel="stylesheet" href="./css/editUnit.css">
        <script>
            function updateConversion(selectElement, supplierId) {
                const dataElement = document.getElementById("data_" + supplierId);
                if (!dataElement)
                    return;
                const conversion = JSON.parse(dataElement.textContent);
                const selected = selectElement.value;
                const input = document.getElementById("conv_" + supplierId);
                if (input) {
                    input.value = conversion[selected] || "";
                }
            }
        </script>
    </head>
    <body>
        <div class="unit-card">
            <h1>Chỉnh sửa tỉ lệ quy đổi</h1>
            <form action="unitEditseverlet" method="post" id="unitForm">
                <table>
                    <thead>
                        <tr>
                            <th>Đơn vị nhập vào</th>
                            <th>Đơn vị chuyển đổi vào kho</th>
                            <th>Tỉ lệ</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="sup" items="${supplierUnits}">
                            <tr>
                                <td>${sup.unit_name}</td>
                                <td>
                                    <select name="warehouse_${sup.unit_id}" onchange="updateConversion(this, '${sup.unit_id}')">
                                        <c:forEach var="conv" items="${mapBaseUnits[sup.unit_id]}">
                                            <option value="${conv.warehouseunitid}">
                                                ${conv.units.unit_name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    <input type="number" id="conv_${sup.unit_id}" name="conversion_${sup.unit_id}" value="" min="1" max="1000"  required/>
                                    <script type="application/json" id="data_${sup.unit_id}">
                                        {
                                        <c:forEach var="conv" items="${mapBaseUnits[sup.unit_id]}" varStatus="loop">
                                            "${conv.warehouseunitid}": "${conv.conversionfactor}"<c:if test="${!loop.last}">,</c:if>
                                        </c:forEach>
                                        }
                                    </script>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <button type="submit">Lưu</button>
                <div style="display: flex; justify-content: flex-start; margin-top: 24px;">
                    <button type="button"><a href="unitConversionSeverlet" class="btn-reset"><i class="fas fa-arrow-left"></i>về</a></button>
                </div>
            </form>

        </div>


        <script>
            window.onload = function () {
                document.querySelectorAll("select[name^='warehouse_']").forEach(select => {
                    select.dispatchEvent(new Event('change'));
                });
            };
             document.addEventListener("DOMContentLoaded", function () {
        const form = document.getElementById("unitForm");

        form.addEventListener("submit", function (e) {
            const confirmSave = confirm("Bạn có chắc chắn muốn lưu các thay đổi tỉ lệ không?");
            if (!confirmSave) {
                e.preventDefault(); 
            }
        });
    });
        </script>
    </body>
</html>
