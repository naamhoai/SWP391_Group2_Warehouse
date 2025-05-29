package model;

public class Item {
    private int itemId;
    private String itemName;
    private int currentStock;
    private int minimumStock;
    private String status;
    
    public Item() {
    }
    
    public Item(int itemId, String itemName, int currentStock, int minimumStock) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        updateStatus();
    }
    
    private void updateStatus() {
        if (currentStock == 0) {
            status = "Out of Stock";
        } else if (currentStock <= minimumStock) {
            status = "Low Stock";
        } else {
            status = "In Stock";
        }
    }
    
    public int getItemId() {
        return itemId;
    }
    
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public int getCurrentStock() {
        return currentStock;
    }
    
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
        updateStatus();
    }
    
    public int getMinimumStock() {
        return minimumStock;
    }
    
    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
        updateStatus();
    }
    
    public String getStatus() {
        return status;
    }
} 