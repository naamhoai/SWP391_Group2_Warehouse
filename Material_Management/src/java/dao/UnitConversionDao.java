/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.*;

/**
 *
 * @author kien3
 */
public class UnitConversionDao extends dal.DBContext {

    public List<UnitConversion> getAll() {
        List<UnitConversion> list = new ArrayList<>();
        String sql = "SELECT \n"
                + "    u.category_id,\n"
                + "    c.name AS category_name,\n"
                + "    parent.name AS parent_category_name,\n"
                + "    m.material_id,\n"
                + "    m.name AS material_name,\n"
                + "    u.base_unit,\n"
                + "    u.converted_unit,\n"
                + "    u.conversion_factor,\n"
                + "    u.note\n"
                + "FROM \n"
                + "    unit_conversion u\n"
                + "JOIN \n"
                + "    categories c ON u.category_id = c.category_id\n"
                + "LEFT JOIN \n"
                + "    categories parent ON c.parent_id = parent.category_id\n"
                + "LEFT JOIN \n"
                + "    materials m ON m.category_id = c.category_id;";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet sm = st.executeQuery();
            while (sm.next()) {
                Category cate = new Category();

                Material mate = new Material();
                UnitConversion uni = new UnitConversion();

                cate.setName(sm.getString("category_name"));
                mate.setName(sm.getString("material_name"));
                uni.setCategorypar(sm.getString("parent_category_name"));
                uni.setCategory(cate);
                uni.setMaterial(mate);
                uni.setBaseunit(sm.getString("base_unit"));
                uni.setConvertedunit(sm.getString("converted_unit"));
                uni.setConversionfactor(sm.getString("conversion_factor"));
                uni.setNote(sm.getString("note"));
                list.add(uni);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;

    }

    public List<Category> getAllpre() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE parent_id IS NULL";
        try {
            PreparedStatement ca = connection.prepareStatement(sql);
            ResultSet st = ca.executeQuery();
            while (st.next()) {
                Category cate = new Category();
                cate.setName(st.getString("name"));

                list.add(cate);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;

    }

    public List<UnitConversion> getAllunit() {
        List<UnitConversion> list = new ArrayList<>();
        String sql = "SELECT DISTINCT base_unit FROM unit_conversion;";
        try {
            PreparedStatement ca = connection.prepareStatement(sql);
            ResultSet st = ca.executeQuery();
            while (st.next()) {
                UnitConversion uni = new UnitConversion();
                uni.setBaseunit(st.getString("base_unit"));

                list.add(uni);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public List<UnitConversion> getFilter(String device, String stunit, String name) {
        List<UnitConversion> list = new ArrayList<>();
        List<Object> param = new ArrayList<>();
        String sql = "SELECT \n"
                + "    u.category_id,\n"
                + "    c.name AS category_name,\n"
                + "    parent.name AS parent_category_name,\n"
                + "    m.material_id,\n"
                + "    m.name AS material_name,\n"
                + "    u.base_unit,\n"
                + "    u.converted_unit,\n"
                + "    u.conversion_factor,\n"
                + "    u.note\n"
                + "FROM \n"
                + "    unit_conversion u\n"
                + "JOIN \n"
                + "    categories c ON u.category_id = c.category_id\n"
                + "LEFT JOIN \n"
                + "    categories parent ON c.parent_id = parent.category_id\n"
                + "LEFT JOIN \n"
                + "    materials m ON m.category_id = c.category_id\n"
                + "where 1 = 1 ";

        if (device != null && !device.equalsIgnoreCase("all")) {
            
            sql += "and  parent.name  = ?";
            param.add(device.trim());
        }
        if (stunit != null && !stunit.equalsIgnoreCase("all")) {
            sql += " and u.base_unit = ?";
            param.add(stunit.trim());
        }

        if (name != null && !name.isEmpty()) {
            sql += " and  m.name like ?";
            param.add("%"+name.trim()+"%");
        }

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            for (int i = 0; i < param.size(); i++) {
                st.setObject(i + 1, param.get(i));
            }
            ResultSet sm = st.executeQuery();
            while (sm.next()) {
               Category cate = new Category();

                Material mate = new Material();
                UnitConversion uni = new UnitConversion();

                cate.setName(sm.getString("category_name"));
                mate.setName(sm.getString("material_name"));
                uni.setCategorypar(sm.getString("parent_category_name"));
                uni.setCategory(cate);
                uni.setMaterial(mate);
                uni.setBaseunit(sm.getString("base_unit"));
                uni.setConvertedunit(sm.getString("converted_unit"));
                uni.setConversionfactor(sm.getString("conversion_factor"));
                uni.setNote(sm.getString("note"));
                list.add(uni);
                
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;

    }

    public static void main(String[] args) {
        UnitConversionDao n = new UnitConversionDao();
        String a ="Thiết bị điện";
        String uni ="";
        String c ="";
        System.out.println("Base unit: " + uni);
        System.out.println("Device (parent): " + a);
        System.out.println("Material name: " + c);

        List<UnitConversion> l = n.getFilter(a, uni, c);

        System.out.println(l);

    }

}
