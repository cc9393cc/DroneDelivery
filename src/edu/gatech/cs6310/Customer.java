package edu.gatech.cs6310;

import java.util.TreeMap;

public class Customer extends User {
    private static final int MIN_RELIABILITY_RATE = 1;
    private static final int MAX_RELIABILITY_RATE = 5;

    private int reliabilityRate;
    private int credit;
    private int pendingCharges;
    private Coordinate location;
    private TreeMap<String, Coupon> couponList = new TreeMap<>();


    public Customer(String account,
                    String firstName,
                    String lastName,
                    String phoneNumber,
                    int customerRating,
                    int credit,
                    Coordinate location) {
        super(account, firstName, lastName, phoneNumber);

        this.reliabilityRate = customerRating;
        this.credit = credit;
        this.pendingCharges = 0;
        this.location = location;
        this.couponList = new TreeMap<>();
    }

    public TreeMap<String, Coupon> getCouponList() {return this.couponList; }

    public int getReliabilityRate() {
        return this.reliabilityRate;
    }

    public Coordinate getLocation() {return this.location; }

    public void addCoupon(String couponID, Coupon coupon) { this.couponList.put(couponID, coupon); }

    public int getCredit() {
        return this.credit;
    }

    public int getPendingCharges() { return this.pendingCharges; }

    public void increasePendingCharges(int amount) {
        this.pendingCharges += amount;
    }

    public void resetPendingCharges() {
        this.pendingCharges = 0;
    }

    public void restorePendingCharges(int amount) {
        this.pendingCharges -= amount;
    }

    public void deductCredit(int amount) {
        this.credit -= amount;
    }

    public void increaseCredit(int amount) {this.credit += amount; }
}
