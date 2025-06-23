package model;

public class MaterialSupplier {
    private int materialId;
    private String materialName;
    private int supplierId;
    private String supplierName;

    public MaterialSupplier() {
    }

    public MaterialSupplier(int materialId, String materialName, int supplierId, String supplierName) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
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
} 