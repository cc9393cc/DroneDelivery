package edu.gatech.cs6310;

import java.util.Date;

public class Coupon {
    public String couponID;
    private String orderID;
    private final int discount = 5;
    public Date expirationDate;

    public Coupon(String couponID, Date expirationDate) {
        this.couponID = couponID;
        this.orderID = "";
        this.expirationDate = expirationDate;
    }

    public String getOrderID() { return orderID; }
    public Date getExpirationDate() { return expirationDate; }
    public int getDiscount() { return discount; }
}
