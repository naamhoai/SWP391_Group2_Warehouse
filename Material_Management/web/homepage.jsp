<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Material Management System</title>
    <link rel="stylesheet" href="css/homepage.css">
    <link rel="stylesheet" href="css/footer.css">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar">
        <div class="nav-container">
            <a href="homepage.jsp" class="nav-logo">
                <i class="fas fa-warehouse"></i> Material Management
            </a>
            <div class="nav-links">
                <a href="homepage.jsp" class="nav-link">Home</a>
                <a href="#features" class="nav-link">Features</a>
                <a href="login.jsp" class="btn btn-primary">Log In</a>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero-section">
        <div class="container">
            <h1 class="hero-title">Transform Your Material Management</h1>
            <p class="hero-subtitle">Streamline your inventory, optimize resources, and boost efficiency with our comprehensive system</p>
            <a href="login.jsp" class="btn-cta">Start Your Journey</a>
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
                    <h3 class="feature-title">Smart Inventory Management</h3>
                    <p class="feature-description">
                        Track materials in real-time, set automatic reorder points, and maintain optimal stock levels effortlessly.
                    </p>
                </div>

                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fas fa-chart-line"></i>
                    </div>
                    <h3 class="feature-title">Advanced Analytics</h3>
                    <p class="feature-description">
                        Make data-driven decisions with comprehensive reports, usage patterns, and predictive analytics.
                    </p>
                </div>

                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fas fa-warehouse"></i>
                    </div>
                    <h3 class="feature-title">Supplier Management</h3>
                    <p class="feature-description">
                        Manage suppliers, automate ordering processes, and maintain strong relationships with vendors.
                    </p>
                </div>

                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fas fa-toolbox"></i>
                    </div>
                    <h3 class="feature-title">Product Management</h3>
                    <p class="feature-description">
                        Access your inventory data anywhere, anytime with our mobile-responsive platform.
                    </p>
                </div>
            </div>
        </div>
    </section>

    <!-- Include Footer -->
    <jsp:include page="footer.jsp" />
</body>
</html> 