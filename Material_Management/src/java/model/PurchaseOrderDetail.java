package model;

public class PurchaseOrderDetail {
    private int purchaseOrderDetailId;
    private int purchaseOrderId;
    private String materialName;
    private String supplier;
    private String contactPerson;
    private int quantity;
    private String unit;
    private String baseUnit;
    private String convertedUnit;
    private double unitPrice;
    private double totalPrice;
    private String description;
    private String materialCondition;
    private int materialId;
    private int categoryId;
    private int conversionId;

    public PurchaseOrderDetail() {
    }

    public PurchaseOrderDetail(int purchaseOrderId, Integer materialId, String materialName, 
                              Integer categoryId, Integer conversionId, int quantity, 
                              double unitPrice, String description, String materialCondition) {
        this.purchaseOrderId = purchaseOrderId;
        this.materialName = materialName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = quantity * unitPrice;
        this.description = description;
        this.materialCondition = materialCondition;
        this.materialId = materialId != null ? materialId : 0;
        this.categoryId = categoryId != null ? categoryId : 0;
        this.conversionId = conversionId != null ? conversionId : 0;
    }

    public int getPurchaseOrderDetailId() {
        return purchaseOrderDetailId;
    }

    public void setPurchaseOrderDetailId(int purchaseOrderDetailId) {
        this.purchaseOrderDetailId = purchaseOrderDetailId;
    }

    public int getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(int purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = quantity * unitPrice;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.totalPrice = quantity * unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterialCondition() {
        return materialCondition;
    }

    public void setMaterialCondition(String materialCondition) {
        this.materialCondition = materialCondition;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
    }

    public String getConvertedUnit() {
        return convertedUnit;
    }

    public void setConvertedUnit(String convertedUnit) {
        this.convertedUnit = convertedUnit;
    }

    public int getMaterialId() {
        return materialId;
    }
    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getConversionId() {
        return conversionId;
    }

    public void setConversionId(int conversionId) {
        this.conversionId = conversionId;
    }

    @Override
    public String toString() {
        return "PurchaseOrderDetail{" + "purchaseOrderDetailId=" + purchaseOrderDetailId + 
               ", purchaseOrderId=" + purchaseOrderId + ", materialName=" + materialName + 
               ", supplier=" + supplier + ", contactPerson=" + contactPerson + 
               ", quantity=" + quantity + ", unitPrice=" + unitPrice + 
               ", totalPrice=" + totalPrice + ", description=" + description + 
               ", materialCondition=" + materialCondition + ", unit=" + unit + 
               ", baseUnit=" + baseUnit + ", convertedUnit=" + convertedUnit + 
               ", materialId=" + materialId + ", categoryId=" + categoryId + 
               ", conversionId=" + conversionId + '}';
    }
}
