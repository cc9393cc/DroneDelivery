package edu.gatech.cs6310;

public class DronePilot extends Employee {
    private String account;
    private String licenseID;
    private int successDeliveryCount;
    private String droneStore;
    private String droneID;

    public DronePilot(String account,
                      String firstName,
                      String lastName,
                      String phoneNumber,
                      String taxIdentifier,
                      String licenseID,
                      int numberOfDeliveries) {
        super(account, firstName, lastName, phoneNumber, taxIdentifier);
        this.licenseID = licenseID;
        successDeliveryCount = numberOfDeliveries;
        droneStore = null;
        droneID = null;
    }

    public String getDroneStore() {
        return this.droneStore;
    }

    public String getDroneID() {
        return this.droneID;
    }

    public void assignDrone(String droneStore, String droneID) {
        this.droneStore = droneStore;
        this.droneID = droneID;
    }

    public String getLicenseID() {
        return this.licenseID;
    }

    public int getExperience() {
        return this.successDeliveryCount;
    }

    public void increaseExperience(int numberOfDeliveries) {
        this.successDeliveryCount += numberOfDeliveries;
    }
}
