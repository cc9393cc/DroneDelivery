import edu.gatech.cs6310.*;

import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws ParseException {
        System.out.println("Welcome to the Grocery Express Delivery Service!");
        DeliveryService simulator = new DeliveryService();
        simulator.commandLoop();
    }
}
