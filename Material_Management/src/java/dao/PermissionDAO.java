/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 *
 * @author nam
 */
public class PermissionDAO {
    
    private Connection conn;
    public PermissionDAO(Connection conn) { this.conn = conn; }

    public boolean createPermission(String name, String description) throws SQLException {
        String sql = "INSERT INTO permissions (permission_name) VALUES (?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, description);
        int rows = ps.executeUpdate();
        return rows > 0;
    }
    
}
