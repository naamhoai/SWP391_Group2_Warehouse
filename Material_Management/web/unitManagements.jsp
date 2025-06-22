<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý đơn vị</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" href="./css/unitMmanagement.css">
        <link rel="stylesheet" href="./css/unitmanga.css">
       
    </head>
    <body>
        
    
        <div class="container">
            <h2 class="page-title">Quản lý đơn vị</h2>

            <form action="unitConversionSeverlet" method="get">
                <div class="filter-section">
                    <div class="search-filters">
                        <div class="filter-group">
                            <label>Đơn vị gốc:</label>
                            <select name="baseunit">
                                <option value="all">All</option>
                                <c:forEach var="m" items="${requestScope.listbase}">
                                    <option value="${m.baseunit}">${m.baseunit}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="filter-group">
                            <label>Đơn vị chuẩn:</label>
                            <select name="convertedunit">
                                <option value="all">All</option>
                                <c:forEach var="uni" items="${requestScope.listconverted}">
                                    <option value="${uni.convertedunit}">${uni.convertedunit}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="filter-group">
                            <label>Trạng thái:</label>
                            <select name="status">
                                <option value="all">All</option>
                                <option value="Active">Active</option>
                                <option value="Inactive">Inactive</option>
                            </select>
                        </div>

                        <div class="search-group">
                            <input type="text" name="search" placeholder="Tìm kiếm..." class="search-input">
                            <button class="search-btn" type="submit">
                                <i class="fas fa-search"></i>
                            </button>
                        </div>
                    </div>

                </div>
            </form>

            <div class="action-buttons">
                <div class="add-button">
                    <a href="CreateUnitServlet">Thêm vật tư</a>
                </div>
                <div class="add-button">
                    <a href="#">Lịch sử thay đổi đơn vị</a>
                </div>
            </div>
          
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Tên hiển thị</th>
                            <th>Đơn vị gốc</th>
                            <th>Đơn vị chuẩn</th>
                            <th>Tỷ lệ quy đổi</th>
                            <th>Trạng thái</th>
                            <th>Ghi chú</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="v" items="${requestScope.list}">
                            <tr>

                                <td>${v.material.name}</td>
                                <td>${v.baseunit}</td>
                                <td>${v.convertedunit}</td>
                                <td>${v.conversionfactor}</td>
                                <td>${v.status}</td>
                                <td>${v.note}</td>

                                <td>

                                    <a href="unitConversionSeverlet?cvid=${v.conversionid}&action=Active"><button>Active</button></a>
                                    <a href="unitConversionSeverlet?cvid=${v.conversionid}&action=Inactive"><button>Inactive</button></a>
                                    
                                    <a href="unitEditseverlet?baseunitid=${v.conversionid}&materialid=${v.material.materialId}&materialname=${v.material.name}">
                                      chỉnh sửa
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div class="pagination-container">
                    <c:forEach begin="1" end="${pages}" var="p">
                        <form action="unitConversionSeverlet" method="get">
                            <input type="hidden" name="baseunit" value="${baseunit}">
                            <input type="hidden" name="convertedunit" value="${convertedunit}">
                            <input type="hidden" name="status" value="${status}">
                            <input type="hidden" name="search" value="${search}">
                            <input type="hidden" name="page" value="${p}">
                            <button type="submit" ${p == currentPage ? 'disabled' : ''}>${p}</button>
                        </form>
                       
                    </c:forEach>
                    
                </div>
               
            </div>
        </div>
          <a hidden="unitConversionSeverlet">Trở lại</a>
    </body>
</html>
