<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý đơn vị</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" href="./css/unitMmanagement.css">
    </head>
    <body>
        <div class="container">
            <h2 class="page-title">Conversion table</h2>
                
            <form action="unitConversionSeverlet" method="post">
            <div class="filter-section">
                <div class="search-filters">
                    <div class="filter-group">
                        <label>Device:</label>
                        <select name="choiceDevice">
                            <option value="all">All</option>
                            <c:forEach var="m" items="${requestScope.listcat}">
                            <option value="${m.name}">${m.name}</option>
                            </c:forEach>
                            
                        </select>
                    </div>

                    <div class="filter-group">
                        <label>Standard unit:</label>
                        <select name="choiceUnit">
                            <option value="all">All</option>
                            <c:forEach var="uni" items="${requestScope.listunit}">
                             <option value="${uni.baseunit}">${uni.baseunit}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="search-group">
                        <input type="text" name="search" placeholder="Tìm kiếm..." class="search-input">
                        <button class="search-btn" type="submit">
                            <i class="fas fa-search"></i>
                        </button>
                    </div>
                </div>

                <div class="add-button">
                    <button class="add-new">
                        <i class="fas fa-plus"></i> New Add 
                    </button>
                </div>
                <div class="add-button">
                        <i class="fas fa-plus"></i><a href="UnitEditseverlet"> Edit </a>
                </div>
                
            </div>
     </form>
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Thiết bị</th>
                            <th>Loại</th>
                            <th>Tên hiển thị</th>
                            <th>Đơn vị gốc</th>
                            <th>Đơn vị chuẩn</th>
                            <th>Tỷ lệ quy đổi</th>
                            <th>Ghi chú</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="v" items="${requestScope.list}">
                            <tr>
                                <td>${v.categorypar}</td>
                                <td>${v.category.name}</td>
                                <td>${v.material.name}</td>
                                <td>${v.baseunit}</td>
                                <td>${v.convertedunit}</td>
                                <td>${v.conversionfactor}</td>
                                <td>${v.note}</td>
                                <td class="actions">
                                  
                                    <button class="delete-btn"><i class="fas fa-trash"></i></button>
                                </td>

                            </tr>
                        </c:forEach>

                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html> 