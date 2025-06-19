package model;

public class MaterialSupplier {
    private int materialSupplierId;
    private int materialId;
    private String materialName;
    private int supplierId;
    private String supplierName;
    private String status;

    // Constructor mặc định
    public MaterialSupplier() {
    }

    // Constructor đầy đủ tham số
    public MaterialSupplier(int materialSupplierId, int materialId, String materialName, 
                           int supplierId, String supplierName, String status) {
        this.materialSupplierId = materialSupplierId;
        this.materialId = materialId;
        this.materialName = materialName;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.status = status;
    }

    // Constructor không có ID
    public MaterialSupplier(int materialId, String materialName, int supplierId, 
                           String supplierName, String status) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.status = status;
    }

    // Getters và Setters
    public int getMaterialSupplierId() {
        return materialSupplierId;
    }

    public void setMaterialSupplierId(int materialSupplierId) {
        this.materialSupplierId = materialSupplierId;
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

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 