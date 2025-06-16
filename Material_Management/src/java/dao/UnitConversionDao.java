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
        String sql = "SELECT u.conversion_id,u.material_id,m.material_id,u.base_unit,u.converted_unit,u.conversion_factor,u.note,m.name,m.category_id\n"
                + "FROM unit_conversion u \n"
                + "join materials m on m.material_id = u.material_id;";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet sm = st.executeQuery();
            while (sm.next()) {

                Material mate = new Material();
                UnitConversion uni = new UnitConversion();
                mate.setMaterialId(sm.getInt("material_id"));
//                uni.setConversionid(sm.getInt("conversion_id"));
                mate.setName(sm.getString("name"));
//                uni.setMaterialid(sm.getInt("material_id"));
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
    
    public List<Material> getMaterial() {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT material_id,name FROM materials;";
        try {
            PreparedStatement ca = connection.prepareStatement(sql);
            ResultSet st = ca.executeQuery();
            while (st.next()) {
                Material mate = new Material();
                mate.setName(st.getString("name"));
                mate.setMaterialId(st.getInt("material_id"));

                list.add(mate);
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
        String sql = "SELECT DISTINCT base_unit,converted_unit FROM unit_conversion;";
        try {
            PreparedStatement ca = connection.prepareStatement(sql);
            ResultSet st = ca.executeQuery();
            while (st.next()) {
                UnitConversion uni = new UnitConversion();
                uni.setBaseunit(st.getString("base_unit"));
                uni.setConvertedunit(st.getString("converted_unit"));
                
                list.add(uni);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public List<UnitConversion> getAllunitbase() {
        List<UnitConversion> list = new ArrayList<>();
        String sql = "SELECT DISTINCT base_unit FROM unit_conversion;";
        try {
            PreparedStatement ca = connection.prepareStatement(sql);
            ResultSet st = ca.executeQuery();
            while(st.next()) {
                UnitConversion uni = new UnitConversion();
                uni.setBaseunit(st.getString("base_unit"));
                
                list.add(uni);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }
    public List<UnitConversion> getAllunitconverted() {
        List<UnitConversion> list = new ArrayList<>();
        String sql = "SELECT DISTINCT converted_unit FROM unit_conversion;";
        try {
            PreparedStatement ca = connection.prepareStatement(sql);
            ResultSet st = ca.executeQuery();
            while (st.next()) {
                UnitConversion uni = new UnitConversion();
          
                uni.setConvertedunit(st.getString("converted_unit"));
                list.add(uni);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }
    
    
    
    

     public List<UnitConversion> getFilter(String baseunit, String convertedunit, String name) {
        List<UnitConversion> list = new ArrayList<>();
        List<Object> param = new ArrayList<>();
        String sql = "SELECT u.conversion_id,m.material_id,u.base_unit,u.converted_unit, \n"
                + "u.conversion_factor,u.note,m.name,m.category_id\n"
                + "FROM unit_conversion u \n"
                + "join materials m on m.material_id = u.material_id\n"
                + "where  1 = 1";

        if (baseunit != null && !baseunit.equalsIgnoreCase("all")) {

            sql += " and base_unit = ?";
            param.add(baseunit.trim());
        }
        if (convertedunit != null && !convertedunit.equalsIgnoreCase("all")) {
            sql += " and converted_unit = ?";
            param.add(convertedunit.trim());
        }

        if (name != null && !name.isEmpty()) {
            sql += " and m.name like ?";
            param.add("%" + name.trim() + "%");
        }

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            for (int i = 0; i < param.size(); i++) {
                st.setObject(i + 1, param.get(i));
            }
            ResultSet sm = st.executeQuery();
            while (sm.next()) {

                Material mate = new Material();
                UnitConversion uni = new UnitConversion();

                mate.setMaterialId(sm.getInt("material_id"));
//                uni.setConversionid(sm.getInt("conversion_id"));
                mate.setName(sm.getString("name"));

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
     
     public UnitConversion Update(int materialid, String baseunit, String convertedunit, String conversionfactor, String note, int conversionid) {
        String sql = "UPDATE unit_conversion\n"
                + "SET\n"
                + "material_id = ?,\n"
                + "base_unit = ?, \n"
                + "converted_unit = ?, \n"
                + "conversion_factor = ?, \n"
                + "note = ?\n"
                + "WHERE conversion_id = ?;";
        try {
            PreparedStatement sm = connection.prepareStatement(sql);
            sm.setInt(1, materialid);
            sm.setString(2, baseunit);
            sm.setString(3, convertedunit);
            sm.setString(4, conversionfactor);
            sm.setString(5, note);
            sm.setInt(6, conversionid);
            sm.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }
     
     public List<Category> getname() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT name\n"
                + "FROM categories\n"
                + "WHERE parent_id IS NOT NULL;";
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
     
     
     public User unitCreate2(String base_unit, String converted_unit, String note, double conversion_factor, int material_id) {
        String sql = "INSERT INTO unit_conversion (base_unit, converted_unit, note,conversion_factor, material_id)\n"
                + "VALUES (?, ? ,?, ?, ?);";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, base_unit);
            st.setString(2, converted_unit);
            st.setString(3, note);
            st.setDouble(4, conversion_factor);
            st.setInt(5, material_id);
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;

    }

    public static void main(String[] args) {
        UnitConversionDao n = new UnitConversionDao();
        String a ="Thiết bị điện";
        String uni ="";
        String c ="";
        System.out.println("Base unit: " + uni);
        System.out.println("Device (parent): " + a);
        System.out.println("Material name: " + c);

        List<UnitConversion> l = n.getAllunitbase();

        System.out.println(l);

    }

}
