package model;

public class RequestDetail {

    private int requestDetailId;
    private int requestId;
    private Integer materialId; 
    private String materialName; 
    private int quantity;
    private int warehouseUnitId;
    private String unitName;
    private String materialCondition;
    private String description;
    private int categoryId;

    public RequestDetail(int requestDetailId, int requestId, Integer materialId, String materialName, int quantity, int warehouseUnitId, String materialCondition, String description, int categoryId) {
        this.requestDetailId = requestDetailId;
        this.requestId = requestId;
        this.materialId = materialId;
        this.materialName = materialName;
        this.quantity = quantity;
        this.warehouseUnitId = warehouseUnitId;
        this.materialCondition = materialCondition;
        this.description = description;
        this.categoryId = categoryId;
    }

    // Constructors
    public RequestDetail() {
    }

    public RequestDetail(int requestDetailId, int requestId, Integer materialId, String materialName, int quantity) {
        this.requestDetailId = requestDetailId;
        this.requestId = requestId;
        this.materialId = materialId;
        this.materialName = materialName;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getRequestDetailId() {
        return requestDetailId;
    }

    public void setRequestDetailId(int requestDetailId) {
        this.requestDetailId = requestDetailId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
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

    public int getWarehouseUnitId() {
        return warehouseUnitId;
    }

    public void setWarehouseUnitId(int warehouseUnitId) {
        this.warehouseUnitId = warehouseUnitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
