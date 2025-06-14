<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Hệ Thống Quản Lý Vật Tư</title>
        <link rel="stylesheet" href="css/homepage.css">
        <link rel="stylesheet" href="css/footer.css">
    </head>
    
    <body>
        <!-- Navigation -->
        <nav class="navbar">
            <div class="nav-container">
                <a href="homepage.jsp" class="nav-logo">
                    <i class="fas fa-warehouse"></i> Quản Lý Vật Tư
                </a>
                <div class="nav-links">
                    <a href="homepage.jsp" class="nav-link">Trang Chủ</a>
                    <a href="login.jsp" class="btn btn-primary">Đăng Nhập</a>
                </div>
            </div>
        </nav>

        <!-- Hero Section -->
        <section class="hero-section">
            <div class="container">
                <h1 class="hero-title">Quản Lý Vật Tư Của Bạn</h1>
                <p class="hero-subtitle">Tối ưu hóa kho hàng, quản lý nguồn lực và nâng cao hiệu quả với hệ thống toàn diện của chúng tôi</p>
                <a href="login.jsp" class="btn-cta">Bắt Đầu Ngay</a>
            </div>
        </section>

        <!-- Features Section -->
        <section id="features" class="features-section">
            <div class="container">
                <div class="features-grid">
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-boxes"></i>
                        </div>
                        <h3 class="feature-title">Quản Lý Kho Thông Minh</h3>
                        <p class="feature-description">
                            Theo dõi vật tư theo thời gian thực, thiết lập điểm đặt hàng tự động và duy trì mức tồn kho tối ưu một cách dễ dàng.
                        </p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-chart-line"></i>
                        </div>
                        <h3 class="feature-title">Phân Tích Nâng Cao</h3>
                        <p class="feature-description">
                            Đưa ra quyết định dựa trên dữ liệu với báo cáo toàn diện, mẫu sử dụng và phân tích dự đoán.
                        </p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-warehouse"></i>
                        </div>
                        <h3 class="feature-title">Quản Lý Nhà Cung Cấp</h3>
                        <p class="feature-description">
                            Quản lý nhà cung cấp, tự động hóa quy trình đặt hàng và duy trì mối quan hệ tốt với đối tác.
                        </p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-toolbox"></i>
                        </div>
                        <h3 class="feature-title">Quản Lý Sản Phẩm</h3>
                        <p class="feature-description">
                            Truy cập dữ liệu kho hàng của bạn ở bất kỳ đâu, bất kỳ lúc nào với nền tảng thích ứng di động của chúng tôi.
                        </p>
                    </div>
                </div>
            </div>
        </section>

        <!-- Include Footer -->
        <jsp:include page="footer.jsp" />

    </body>
</html> 