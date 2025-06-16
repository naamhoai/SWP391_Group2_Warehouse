package model;

public class RequestDetail {

    private int requestDetailId;
    private int requestId;
    private Integer materialId; // Có thể null nếu là vật tư mới
    private String materialName; // Nếu là vật tư mới
    private Integer categoryId; // Có thể null
    private int quantity;
    private String description;
    private Integer parentCategoryId;
    private String unitName;
    private String parentCategoryName;
    private String categoryName;
    private String materialCondition;

    public RequestDetail(int requestDetailId, int requestId, Integer materialId, String materialName, Integer categoryId, int quantity, String description, Integer parentCategoryId, String unitName, String parentCategoryName, String categoryName, String materialCondition) {
        this.requestDetailId = requestDetailId;
        this.requestId = requestId;
        this.materialId = materialId;
        this.materialName = materialName;
        this.categoryId = categoryId;
        this.quantity = quantity;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.unitName = unitName;
        this.parentCategoryName = parentCategoryName;
        this.categoryName = categoryName;
        this.materialCondition = materialCondition;
    }

    // Constructors
    public RequestDetail() {
    }

    public RequestDetail(int requestDetailId, int requestId, Integer materialId, String materialName, Integer categoryId, int quantity, String description) {
        this.requestDetailId = requestDetailId;
        this.requestId = requestId;
        this.materialId = materialId;
        this.materialName = materialName;
        this.categoryId = categoryId;
        this.quantity = quantity;
        this.description = description;
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String name) {
        this.parentCategoryName = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String name) {
        this.categoryName = name;
    }

    public String getMaterialCondition() {
        return materialCondition;
    }

    public void setMaterialCondition(String materialCondition) {
        this.materialCondition = materialCondition;
    }
    
}
