/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author kien3
 */
public class UnitConversion {


    private String conversionfactor;
    private String note;

    private int conversionid;
    private int SupplierUnitId;
    private int warehouseunitid;
    private String status;
    private Unit units;

    public UnitConversion() {
    }

    public UnitConversion(String conversionfactor, String note, int conversionid, int SupplierUnitId, int warehouseunitid, String status, Unit units) {
        this.conversionfactor = conversionfactor;
        this.note = note;
        this.conversionid = conversionid;
        this.SupplierUnitId = SupplierUnitId;
        this.warehouseunitid = warehouseunitid;
        this.status = status;
        this.units = units;
    }

    public String getConversionfactor() {
        return conversionfactor;
    }

    public void setConversionfactor(String conversionfactor) {
        this.conversionfactor = conversionfactor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getConversionid() {
        return conversionid;
    }

    public void setConversionid(int conversionid) {
        this.conversionid = conversionid;
    }

    public int getSupplierUnitId() {
        return SupplierUnitId;
    }

    public void setSupplierUnitId(int SupplierUnitId) {
        this.SupplierUnitId = SupplierUnitId;
    }

    public int getWarehouseunitid() {
        return warehouseunitid;
    }

    public void setWarehouseunitid(int warehouseunitid) {
        this.warehouseunitid = warehouseunitid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Unit getUnits() {
        return units;
    }

    public void setUnits(Unit units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "UnitConversion{" + "conversionfactor=" + conversionfactor + ", note=" + note + ", conversionid=" + conversionid + ", SupplierUnitId=" + SupplierUnitId + ", warehouseunitid=" + warehouseunitid + ", status=" + status + ", units=" + units + '}';
    }

    
   

   
   
   
   
}
