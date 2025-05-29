package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {

    public Connection connection;

    public DBContext() {
        try {
            String url = "jdbc:mysql://localhost:3306/warehouse_material_management";
            String user = "root";
            String password = "12345";
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

    public static void main(String[] args) {
        DBContext n = new DBContext();
        
    }
}
