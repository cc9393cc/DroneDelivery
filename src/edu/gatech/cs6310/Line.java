package edu.gatech.cs6310;

public class Line {
    private int price;
    private int quantity;
    private Item item;

    public Line(int price, int quantity, Item item) {
        this.price = price;
        this.quantity = quantity;
        this.item = item;
    }

    public Item getItem() { return this.item; }
    public int getQuantity() { return this.quantity; }

    public int calculateCost() {
        if (this.price > 0 && this.quantity > 0) {
            return this.price * this.quantity;
        } else {
            return 0;
        }
    }

    public int calculateWeight() {
        int itemWeight = this.item.getWeight();
        if (itemWeight > 0 && this.quantity > 0) {
            return itemWeight * this.quantity;
        } else {
            return 0;
        }
    }
}
