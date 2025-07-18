/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.SidebarMenu;

/**
 * @author nam
 */
public class SidebarMenuDAO {

    private final Connection conn;

    public SidebarMenuDAO(Connection conn) {
        this.conn = conn;
    }
    
    public List<SidebarMenu> getSidebarMenuByUser(int userId, int roleId) throws SQLException {
        String sql;
        PreparedStatement ps;

        if (roleId == 1) {
            sql = "SELECT * FROM sidebar_menu WHERE is_enabled = 1 ORDER BY menu_order";
            ps = conn.prepareStatement(sql);
        } 
        else {
            sql = "SELECT DISTINCT sm.* FROM sidebar_menu sm "
                    + "JOIN permissions p ON sm.permission_id = p.permission_id "
                    + "JOIN role_permissions rp ON rp.permission_id = p.permission_id "
                    + "JOIN users u ON u.role_id = rp.role_id "
                    + "WHERE u.user_id = ? AND sm.is_enabled = 1 ORDER BY sm.menu_order";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
        }

        ResultSet rs = ps.executeQuery();
        List<SidebarMenu> menuList = new ArrayList<>();
        
        while (rs.next()) {
            SidebarMenu menu = new SidebarMenu();
            menu.setMenuId(rs.getInt("menu_id"));
            menu.setMenuName(rs.getString("menu_name"));
            menu.setUrl(rs.getString("url"));
            menu.setIcon(rs.getString("icon"));
            Object parentIdObj = rs.getObject("parent_id");
            menu.setParentId(parentIdObj != null ? rs.getInt("parent_id") : null);
            Object permissionIdObj = rs.getObject("permission_id");
            menu.setPermissionId(permissionIdObj != null ? rs.getInt("permission_id") : null);
            menuList.add(menu);
        }
        
        return menuList;
    }
}
