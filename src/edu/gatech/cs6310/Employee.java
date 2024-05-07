package edu.gatech.cs6310;

public abstract class Employee extends User {
    private String taxIdentifier;

    public Employee(String employeeID,
                    String firstName,
                    String lastName,
                    String phoneNumber,
                    String taxIdentifier) {
        super(employeeID, firstName, lastName, phoneNumber);
        this.taxIdentifier = taxIdentifier;
    }

    public String getTaxID() {
        return this.taxIdentifier;
    }
}
