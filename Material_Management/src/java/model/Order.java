package model;

import java.util.Date;

public class Order {
    private int orderId;
    private String customerName;
    private String status;
    private double amount;
    private Date orderDate;
    
    public Order() {
    }
    
    public Order(int orderId, String customerName, String status, double amount, Date orderDate) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.status = status;
        this.amount = amount;
        this.orderDate = orderDate;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public Date getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
} 