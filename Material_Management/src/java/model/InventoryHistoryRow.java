package model;
import java.util.Date;

public class InventoryHistoryRow {
    private String type; // import/export/purchase
    private Date date;
    private String code; // mã phiếu
    private String materialName;
    private Integer quantity;
    private String unit;
    private String operator;
    private String status;
    private String note;

    public InventoryHistoryRow() {}

    public InventoryHistoryRow(String type, Date date, String code, String materialName, Integer quantity, String unit, String operator, String status, String note) {
        this.type = type;
        this.date = date;
        this.code = code;
        this.materialName = materialName;
        this.quantity = quantity;
        this.unit = unit;
        this.operator = operator;
        this.status = status;
        this.note = note;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
} 