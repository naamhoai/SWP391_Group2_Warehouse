package model;

public class PurchaseOrderDetail {
    private int purchaseOrderDetailId;
    private int purchaseOrderId;
    private int materialId;
    private String materialName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private String materialCondition;
    
    // Getters and Setters
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
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public String getMaterialCondition() {
        return materialCondition;
    }
    
    public void setMaterialCondition(String materialCondition) {
        this.materialCondition = materialCondition;
    }
} 