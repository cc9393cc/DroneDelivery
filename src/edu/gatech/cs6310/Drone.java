package edu.gatech.cs6310;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Drone {
    private String droneID;
    private int maxCapacity;
    private int currentWeight;
    private List<String> orders;
    private int maxTrips;
    private int currentTripCount;
    private String pilotAccount;
    private final int speed = 1; // n unit distance per day

    public Drone(String droneID,
                 int maxCapacity,
                 int maxTrips) {
        this.droneID = droneID;
        this.maxCapacity = maxCapacity;
        this.maxTrips = maxTrips;

        this.currentWeight = 0;
        this.currentTripCount = 0;
        this.orders = new ArrayList<>();
        this.pilotAccount = null;
    }

    public String getDroneID() {
        return this.droneID;
    }

    public String getLastPilot() {
        return this.pilotAccount;
    }

    public int getSpeed() {return speed; }

    public void assignPilot(String pilotAccount) {
        this.pilotAccount = pilotAccount;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    public List<String> getOrders() {
        return this.orders;
    }

    public int getRemainingCapacity() {
        return this.maxCapacity - this.currentWeight;
    }

    public int getTripsLeft() {
        return this.maxTrips - this.currentTripCount;
    }

    public void addOrder(String orderID) {
        this.getOrders().add(orderID);
    }

    public void reduceTrip(int numberOfTrips) {
        this.currentTripCount += numberOfTrips;
    }

    public void reduceCapacity(int weight) {
        this.currentWeight += weight;
    }

    public void restoreCapacity(int weight) {
        this.currentWeight -= weight;
    }
}
