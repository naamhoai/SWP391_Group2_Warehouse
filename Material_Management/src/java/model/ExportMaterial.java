package model;

import java.math.BigDecimal;

public class ExportMaterial {
    private int exportMaterialId;
    private int exportId;
    private int materialId;
    private String materialName;
    private int categoryId;
    private int warehouseUnitId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String materialCondition; // Mới, Cũ, Hỏng
    private String description;
    
    // Additional fields for display
    private String categoryName;
    private String unitName;

    public ExportMaterial() {
    }

    public ExportMaterial(int exportMaterialId, int exportId, int materialId, String materialName,
                         int categoryId, int warehouseUnitId, int quantity, BigDecimal unitPrice,
                         BigDecimal totalPrice, String materialCondition, String description) {
        this.exportMaterialId = exportMaterialId;
        this.exportId = exportId;
        this.materialId = materialId;
        this.materialName = materialName;
        this.categoryId = categoryId;
        this.warehouseUnitId = warehouseUnitId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.materialCondition = materialCondition;
        this.description = description;
    }

    // Getters and Setters
    public int getExportMaterialId() {
        return exportMaterialId;
    }

    public void setExportMaterialId(int exportMaterialId) {
        this.exportMaterialId = exportMaterialId;
    }

    public int getExportId() {
        return exportId;
    }

    public void setExportId(int exportId) {
        this.exportId = exportId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getWarehouseUnitId() {
        return warehouseUnitId;
    }

    public void setWarehouseUnitId(int warehouseUnitId) {
        this.warehouseUnitId = warehouseUnitId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getMaterialCondition() {
        return materialCondition;
    }

    public void setMaterialCondition(String materialCondition) {
        this.materialCondition = materialCondition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    // Helper methods
    public boolean isNew() {
        return "Mới".equals(materialCondition);
    }

    public boolean isUsed() {
        return "Cũ".equals(materialCondition);
    }

    public boolean isDamaged() {
        return "Hỏng".equals(materialCondition);
    }

    public void calculateTotalPrice() {
        if (unitPrice != null && quantity > 0) {
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
} 