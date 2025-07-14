package model;

public class Material {

    private int materialId;
    private String name;
    private int categoryId;
    private String categoryName;
    private int supplierId;
    private String supplierName;
    private int unitId;
    private String imageUrl;
    private long price;
    private String description;
    private String unitName;
    private String status;

    public Material() {
    }

    public Material(int materialId, String name, int categoryId, String categoryName, int supplierId, String supplierName, int unitId, String imageUrl, long price, String description, String unitName, String status) {
        this.materialId = materialId;
        this.name = name;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.unitId = unitId;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
        this.unitName = unitName;
        this.status = status;
    }
    
    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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


    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Material{" + "materialId=" + materialId + ", name=" + name + ", categoryId=" + categoryId + ", categoryName=" + categoryName + ", supplierId=" + supplierId + ", supplierName=" + supplierName + ", unitId=" + unitId + ", imageUrl=" + imageUrl + ", price=" + price + ", description=" + description + ", unitName=" + unitName + ", status=" + status + '}';
    }
    
}
