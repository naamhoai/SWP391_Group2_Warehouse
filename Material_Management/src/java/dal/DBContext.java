package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBContext {

    public Connection connection;

    public DBContext() {
        try {
            String url = "jdbc:mysql://localhost:3306/material_system_2";
            String user = "root";
            String password = "123456";
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("ket noi thanh cong ");

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Lỗi khi khởi tạo kết nối cơ sở dữ liệu" + e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Kết nối CSDL đã được đóng.");
            } catch (SQLException e) {

                System.err.println("Lỗi khi đóng kết nối CSDL: " + e.getMessage());
            }
        }
    }

    private void createTables() {
        try (Connection conn = getConnection()) {
            // Create permission_logs table
            String createPermissionLogsTable = "CREATE TABLE IF NOT EXISTS permission_logs ("
                    + "log_id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "role_id INT NOT NULL,"
                    + "admin_id INT NOT NULL,"
                    + "action VARCHAR(50) NOT NULL,"
                    + "permission_name VARCHAR(100) NOT NULL,"
                    + "old_value BOOLEAN,"
                    + "new_value BOOLEAN,"
                    + "log_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY (role_id) REFERENCES roles(role_id),"
                    + "FOREIGN KEY (admin_id) REFERENCES users(user_id)"
                    + ")";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createPermissionLogsTable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DBContext n = new DBContext();
        
    }
}