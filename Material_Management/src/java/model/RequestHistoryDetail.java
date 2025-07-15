package model;

public class RequestHistoryDetail {

    private int historyDetailId;
    private int historyId;
    private int materialId;
    private String materialName;
    private int quantity;
    private int warehouseUnitId;
    private String materialCondition;

    // Additional fields for display
    private String unitName;

    public RequestHistoryDetail() {
    }

    public RequestHistoryDetail(int historyDetailId, int historyId, int materialId, String materialName,
            int quantity, int warehouseUnitId, String materialCondition) {
        this.historyDetailId = historyDetailId;
        this.historyId = historyId;
        this.materialId = materialId;
        this.materialName = materialName;
        this.quantity = quantity;
        this.warehouseUnitId = warehouseUnitId;
        this.materialCondition = materialCondition;
    }

    public int getHistoryDetailId() {
        return historyDetailId;
    }

    public void setHistoryDetailId(int historyDetailId) {
        this.historyDetailId = historyDetailId;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
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

    public int getWarehouseUnitId() {
        return warehouseUnitId;
    }

    public void setWarehouseUnitId(int warehouseUnitId) {
        this.warehouseUnitId = warehouseUnitId;
    }

    public String getMaterialCondition() {
        return materialCondition;
    }

    public void setMaterialCondition(String materialCondition) {
        this.materialCondition = materialCondition;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }


    public String getQuantityDisplay() {
        return quantity + " " + (unitName != null ? unitName : "");
    }
}
