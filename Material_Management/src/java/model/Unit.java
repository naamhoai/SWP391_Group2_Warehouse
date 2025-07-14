/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author kien3
 */
public class Unit {
    private int unit_id;
    private String unit_name;
    private String unit_namePr;
    private boolean is_system_unit;
    private String status;
    private String description;
    
    public Unit() {
    }

    public Unit(int unit_id, String unit_name, String unit_namePr, boolean is_system_unit, String status, String description) {
        this.unit_id = unit_id;
        this.unit_name = unit_name;
        this.unit_namePr = unit_namePr;
        this.is_system_unit = is_system_unit;
        this.status = status;
        this.description = description;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getUnit_namePr() {
        return unit_namePr;
    }

    public void setUnit_namePr(String unit_namePr) {
        this.unit_namePr = unit_namePr;
    }

    public boolean isIs_system_unit() {
        return is_system_unit;
    }

    public void setIs_system_unit(boolean is_system_unit) {
        this.is_system_unit = is_system_unit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Units{" + "unit_id=" + unit_id + ", unit_name=" + unit_name + ", unit_namePr=" + unit_namePr + ", is_system_unit=" + is_system_unit + ", status=" + status + ", description=" + description + '}';
    }

   
    
}
