package model;
import java.util.Date;

public class InventoryHistoryRow {
    private String transactionType; // Loại giao dịch: Mua hàng, Xuất kho, Nhập kho
    private Integer referenceId;    // Mã đơn
    private Date transactionDate;   // Ngày thực hiện
    private String materialName;    // Tên vật tư
    private String materialCondition; // Trạng thái vật tư (Mới/Cũ)
    private Integer quantity;       // Số lượng
    private Integer materialId;     // ID vật tư (có thể null với nhập kho thủ công)
    private String actorName;       // Tên người thực hiện

    public InventoryHistoryRow() {}

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public Integer getReferenceId() { return referenceId; }
    public void setReferenceId(Integer referenceId) { this.referenceId = referenceId; }

    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }

    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public String getMaterialCondition() { return materialCondition; }
    public void setMaterialCondition(String materialCondition) { this.materialCondition = materialCondition; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getMaterialId() { return materialId; }
    public void setMaterialId(Integer materialId) { this.materialId = materialId; }

    public String getActorName() { return actorName; }
    public void setActorName(String actorName) { this.actorName = actorName; }
} 