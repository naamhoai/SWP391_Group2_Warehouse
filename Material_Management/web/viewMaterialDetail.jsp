<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <title>Chi Tiết Vật Tư</title>
    <link rel="stylesheet" href="css/sidebar.css" />
    <link rel="stylesheet" href="css/viewMaterialDetail.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
    <script
      src="https://kit.fontawesome.com/4b7b2b6e8b.js"
      crossorigin="anonymous"
    ></script>
    <script src="js/sidebar.js"></script>
  </head>
  <body>
    <jsp:include page="sidebar.jsp" />

    <div class="main-content">
      <c:if test="${not empty material}">
        <div class="header-section">
          <h1 class="page-title">Chi Tiết Vật Tư: ${material.name}</h1>
          <div class="header-buttons">
            <a href="javascript:history.back()" class="btn btn-secondary"
              ><i class="fas fa-arrow-left"></i> Quay Lại</a
            >
          </div>
        </div>

        <div class="content-card">
          <div class="detail-container">
            <div class="image-panel">
              <h3>Hình ảnh</h3>
              <div class="image-wrapper">
                <img
                  src="image/${not empty material.imageUrl ? material.imageUrl : 'default.png'}"
                  alt="${material.name}"
                  class="material-image"
                />
              </div>
            </div>
            <div class="info-panel">
              <h3>Thông tin chi tiết</h3>
              <div class="info-grid">
                <div class="info-item">
                  <label>Mã VT:</label>
                  <span class="value">#${material.materialId}</span>
                </div>
                <div class="info-item">
                  <label>Tên vật tư:</label>
                  <span class="value">${material.name}</span>
                </div>
                <div class="info-item">
                  <label>Danh mục:</label>
                  <span class="value">${material.categoryName}</span>
                </div>
                <div class="info-item">
                  <label>Nhà cung cấp:</label>
                  <span class="value">${material.supplierName}</span>
                </div>
                <div class="info-item">
                  <label>Đơn vị tính:</label>
                  <span class="value">${material.unitName}</span>
                </div>
                <div class="info-item">
                  <label>Giá:</label>
                  <span class="value">
                    <fmt:setLocale value="vi_VN" />
                    <fmt:formatNumber
                      value="${material.price}"
                      type="currency"
                    />
                  </span>
                </div>
                <div class="info-item">
                  <label>Trạng thái:</label>
                  <span class="value status-badge ${material.status eq 'active' ? 'status-active' : 'status-inactive'}">
                    ${material.status eq 'active' ? 'Đang kinh doanh' : 'Ngừng kinh doanh'}
                  </span>
                </div>
                <div class="info-item">
                  <label>Mô tả:</label>
                  <span class="value">${material.description}</span>
                </div>
              </div>
              <div class="button-container">
                <a
                  href="editMaterialDetail?id=${material.materialId}"
                  class="btn btn-primary"
                  ><i class="fas fa-pen"></i> Sửa Thông Tin</a
                >
              </div>
            </div>
          </div>
        </div>
      </c:if>
      <c:if test="${empty material}">
        <div class="content-card">
          <p>Không tìm thấy thông tin vật tư.</p>
          <a href="javascript:history.back()" class="btn btn-secondary"
            >Quay Lại</a
          >
        </div>
      </c:if>
    </div>
  </body>
</html>
