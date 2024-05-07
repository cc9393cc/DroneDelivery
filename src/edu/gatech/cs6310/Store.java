package edu.gatech.cs6310;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Store {
    private String storeName;
    private List<Employee> employees;
    private TreeMap<String, Item> items;
    private TreeMap<String, Drone> drones;
    private TreeMap<String, Order> orders;
    private int revenue;
    private Coordinate location;

    public Store(String storeName,
                 int revenue, Coordinate location) {
        this.storeName = storeName;
        this.employees = new ArrayList<>();
        this.items = new TreeMap<>();
        this.drones = new TreeMap<>();
        this.orders = new TreeMap<>();
        this.revenue = revenue;
        this.location = location;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public TreeMap<String, Item> getItems() {
        return this.items;
    }

    public TreeMap<String, Drone> getDrones() {
        return this.drones;
    }

    public TreeMap<String, Order> getOrders() { return this.orders; }

    public Coordinate getLocation() {return this.location; }

    public boolean addItem(String itemName, String weightStr) {
        boolean success = false;
        if (!this.items.containsKey(itemName)) {
            try {
                int weight = Integer.parseInt(weightStr);
                Item item = new Item(itemName, weight);
                this.items.put(itemName, item);
                success = true;
            } catch (NumberFormatException ex) {
                System.out.println("Invalid value for weight: " + weightStr);
            }
        }

        return success;
    }

    public boolean buyDrone(String droneID, String weightCapacityStr, String numberOfDeliveriesStr) {
        boolean success = false;
        if (!this.drones.containsKey(droneID)) {
            try {
                int weightCapacity = Integer.parseInt(weightCapacityStr);
                int numberOfDelivery = Integer.parseInt(numberOfDeliveriesStr);
                this.drones.put(droneID, new Drone(droneID, weightCapacity, numberOfDelivery));
                success = true;
            } catch (NumberFormatException ex) {
                System.out.println("Invalid value for weight or number of deliveries");
            }
        }

        return success;
    }

    public int displayRevenue() {
        return this.revenue;
    }

    public void increaseRevenue(double amount) {
        this.revenue += amount;
    }

    public void decreaseRevenue(double amount) {
        this.revenue -= amount;
    }
}
