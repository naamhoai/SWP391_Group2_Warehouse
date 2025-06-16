package model;

import java.math.BigDecimal;

public class Material {

    private int materialId;
    private String name;
    private int categoryId;
    private String categoryName;
    private int supplierId;
    private String supplierName;
    private int conversionId;
    private String imageUrl;
    private String materialCondition;
    private BigDecimal price;
    private String description;
    private int quantity; // Đây là total_inventory
    private String unit;

    public Material() {
    }

    public Material(int materialId, String name, int categoryId, String categoryName, int supplierId, String supplierName, int conversionId, String imageUrl, String materialCondition, BigDecimal price, String description, int quantity, String unit) {
        this.materialId = materialId;
        this.name = name;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.conversionId = conversionId;
        this.imageUrl = imageUrl;
        this.materialCondition = materialCondition;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
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


    public int getConversionId() {
        return conversionId;
    }

    public void setConversionId(int conversionId) {
        this.conversionId = conversionId;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMaterialCondition() {
        return materialCondition;
    }

    public void setMaterialCondition(String materialCondition) {
        this.materialCondition = materialCondition;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Material{" + "materialId=" + materialId + ", name=" + name + ", categoryId=" + categoryId + ", categoryName=" + categoryName + ", supplierId=" + supplierId + ", supplierName=" + supplierName + ", conversionId=" + conversionId + ", imageUrl=" + imageUrl + ", materialCondition=" + materialCondition + ", price=" + price + ", description=" + description + ", quantity=" + quantity + ", unit=" + unit + '}';
    }
    
}
