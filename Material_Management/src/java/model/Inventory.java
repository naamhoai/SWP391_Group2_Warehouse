package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Inventory implements Serializable {
    private int inventoryId;
    private int materialId;
    private String materialCondition; // 'Mới' hoặc 'Cũ'
    private int quantityOnHand;
    private Timestamp lastUpdated;
    private String materialName;
    private String categoryName;
    private String supplierName;
    private String unitName;
    private int price;
    private String status; // active/inactive
    private int unitId;
    private int supplierId;

    public Inventory() {}

    public Inventory(int inventoryId, int materialId, String materialCondition, int quantityOnHand, Timestamp lastUpdated, String materialName, String categoryName, String supplierName, String unitName, int price, String status, int unitId) {
        this.inventoryId = inventoryId;
        this.materialId = materialId;
        this.materialCondition = materialCondition;
        this.quantityOnHand = quantityOnHand;
        this.lastUpdated = lastUpdated;
        this.materialName = materialName;
        this.categoryName = categoryName;
        this.supplierName = supplierName;
        this.unitName = unitName;
        this.price = price;
        this.status = status;
        this.unitId = unitId;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getMaterialCondition() {
        return materialCondition;
    }

    public void setMaterialCondition(String materialCondition) {
        this.materialCondition = materialCondition;
    }

    public int getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(int quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public String toString() {
        return "Inventory{" + "inventoryId=" + inventoryId + ", materialId=" + materialId + ", materialCondition=" + materialCondition + ", quantityOnHand=" + quantityOnHand + ", lastUpdated=" + lastUpdated + ", materialName=" + materialName + ", categoryName=" + categoryName + ", supplierName=" + supplierName + ", unitName=" + unitName + ", price=" + price + ", status=" + status + ", unitId=" + unitId + '}';
    }
    
}