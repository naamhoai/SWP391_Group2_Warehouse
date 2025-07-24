package model;

public class PurchaseOrderDetail {
    private int purchaseOrderDetailId;
    private int purchaseOrderId;
    private Integer materialId;
    private String materialName;
    private int quantity;
    private String unit;
    private String convertedUnit;
    private double unitPrice;
    private double totalPrice;

    public PurchaseOrderDetail() {
    }

    public PurchaseOrderDetail(int purchaseOrderId, Integer materialId, String materialName, 
                              int quantity, String unit, String convertedUnit, double unitPrice) {
        this.purchaseOrderId = purchaseOrderId;
        this.materialName = materialName;
        this.quantity = quantity;
        this.unit = unit;
        this.convertedUnit = convertedUnit;
        this.unitPrice = unitPrice;
        this.totalPrice = quantity * unitPrice;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    @Override
    public String toString() {
        return "PurchaseOrderDetail{" + "purchaseOrderDetailId=" + purchaseOrderDetailId + 
               ", purchaseOrderId=" + purchaseOrderId + ", materialName=" + materialName + 
               ", quantity=" + quantity + ", unitPrice=" + unitPrice + 
               ", totalPrice=" + totalPrice + ", unit=" + unit + 
               ", convertedUnit=" + convertedUnit + ", materialId=" + materialId + '}';
    }
}
