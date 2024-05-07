package edu.gatech.cs6310;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Order {
    private String orderID;
    private TreeMap<String, Line> lines;
    private String droneID;
    private String customerID;
    private int totalCost;
    private int totalWeight;
    private String couponID;

    public Order(String orderID, String droneID, String customerID) {
        this.orderID = orderID;
        this.lines = new TreeMap<>();
        this.droneID = droneID;
        this.customerID = customerID;
        this.totalCost = 0;
        this.totalWeight = 0;
    }

    public TreeMap<String, Line> getLines() { return this.lines; }
    public String getCustomerID() { return this.customerID; }
    public String getDroneID() { return this.droneID; }
    public int getTotalCost() { return this.totalCost; }
    public int getTotalWeight() { return this.totalWeight; }
    public String getCouponID() { return this.couponID; }

    public void addLine(String itemName, Line line) {
        totalCost += line.calculateCost();
        totalWeight += line.calculateWeight();

        this.lines.put(itemName, line);
    }

    public void setCouponID(String couponID){
        this.couponID = couponID;
    }

    public void totalCostWithCoupon(double discount){
        totalCost -= discount;
    }
}
