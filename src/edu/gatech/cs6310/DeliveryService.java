package edu.gatech.cs6310;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DeliveryService {
    private TreeMap<String, Store> storesMap = new TreeMap<>();

    // The pilot must have an account that is unique among all pilots,
    // i.e., this TreeMap cannot live inside a Store
    private TreeMap<String, DronePilot> dronePilotMap = new TreeMap<>();

    // The customer must have an account that is unique among all customers,
    // i.e., this TreeMap cannot live inside a Store
    private TreeMap<String, Customer> customerMap = new TreeMap<>();


    private int couponGenFrequency = 0;
    private double[] rateDistribution;
    private int couponSequence = 1;
    private Date lastCouponGeneration;
    private int couponValidDays;

    public void commandLoop() throws ParseException {
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;
        String[] tokens;
        final String DELIMITER = ",";
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        lastCouponGeneration = sdformat.parse("1900-01-01");

        while (true) {
            try {
                // Determine the next command and echo it to the monitor for testing purposes
                wholeInputLine = commandLineInput.nextLine();
                tokens = wholeInputLine.split(DELIMITER);
                System.out.println("> " + wholeInputLine);

                if (tokens == null) {
                    continue;
                } else if (wholeInputLine.startsWith("//")) {
                    // [25] Comments
                    continue;
                } else if (tokens[0].equals("make_store") && tokens.length >= 5) {
                    // [1] make_store
                    makeStoreHandler(tokens[1], tokens[2], tokens[3], tokens[4]);
                } else if (tokens[0].equals("display_stores")) {
                    // [2] display_stores
                    displayStoresHandler();
                } else if (tokens[0].equals("sell_item") && tokens.length >= 4) {
                    // [3] sell_item
                    sellItemHandler(tokens[1], tokens[2], tokens[3]);
                } else if (tokens[0].equals("display_items") && tokens.length >= 2) {
                    // [4] display_items
                    displayItemsHandler(tokens[1]);
                } else if (tokens[0].equals("make_pilot") && tokens.length >= 8) {
                    // [5] make_pilot
                    makePilotHandler(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7]);
                } else if (tokens[0].equals("display_pilots")) {
                    // [6] display_pilots
                    displayPilotsHandler();
                } else if (tokens[0].equals("make_drone") && tokens.length >= 5) {
                    // [7] make_drone
                    makeDroneHandler(tokens[1], tokens[2], tokens[3], tokens[4]);
                } else if (tokens[0].equals("display_drones") && tokens.length >= 2) {
                    // [8] [10] display_drones
                    displayDronesHandler(tokens[1]);
                } else if (tokens[0].equals("fly_drone") && tokens.length >= 4) {
                    // [9] fly_drone
                    flyDroneHandler(tokens[1], tokens[2], tokens[3]);
                } else if (tokens[0].equals("make_customer") && tokens.length >= 9) {
                    // [11] make_customer
                    makeCustomerHandler(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7], tokens[8]);
                } else if (tokens[0].equals("display_customers")) {
                    // [12] display_customers
                    displayCustomersHandler();
                } else if (tokens[0].equals("start_order") && tokens.length >= 5) {
                    // [13] start_order
                    startOrderHandler(tokens[1], tokens[2], tokens[3], tokens[4]);
                } else if (tokens[0].equals("display_orders") && tokens.length >= 2) {
                    // [14] display_orders
                    displayOrdersHandler(tokens[1]);
                } else if (tokens[0].equals("request_item") && tokens.length >= 6) {
                    // [15] request_item
                    requestItemHandler(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5]);
                } else if (tokens[0].equals("purchase_order") && tokens.length >= 3) {
                    // [18] purchase_order
                    purchaseOrderHandler(tokens[1], tokens[2]);
                } else if (tokens[0].equals("cancel_order") && tokens.length >= 3) {
                    // [22] cancel_order
                    cancelOrderHandler(tokens[1], tokens[2]);
                } else if (tokens[0].equals("author")) {
                    // [26] author
                    System.out.println("kchen478");
                } else if (tokens[0].equals("coupon_init") && tokens.length >= 8) {
                    // coupon initiation    freq, r0, r1, r2, r3, r4, r5
                    couponInitHandler(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7]);
                } else if (tokens[0].equals("apply_coupon")&& tokens.length >= 5) {
                    //apply coupon
                    applyCoupon(tokens[1], tokens[2], tokens[3], tokens[4]);
                }  else if (tokens[0].equals("display_coupon")&& tokens.length >= 1) {
                    //display coupon
                   displayCoupon(tokens[1]);
                }  else if (tokens[0].equals("stop")) {
                    // [24] stop
                    System.out.println("stop acknowledged");
                    break;
                } else {
                    System.out.println("command " + tokens[0] + " NOT acknowledged");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println();
            }
            generateCoupon();
        }

        System.out.println("simulation terminated");
        commandLineInput.close();
    }

    private void generateCoupon() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String lastDateString = dateFormat.format(lastCouponGeneration.getTime());
        String currentDateString = dateFormat.format(new Date());

        if (lastDateString.compareTo(currentDateString) < 0) {
            //Start generate Coupon
            Random generator = new Random();
            for(int i = 0; i < couponGenFrequency; i++) {
                for (Map.Entry<String, Customer> entry : customerMap.entrySet()) {
                    Customer customer = entry.getValue();
                    boolean doGenerate = generator.nextInt(100) < (int)rateDistribution[customer.getReliabilityRate()] * 100;
                    if(doGenerate) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(new Date());
                        c.add(Calendar.DATE, couponValidDays);
                        // convert calendar to date
                        String couponID = String.valueOf(couponSequence);
                        Coupon coupon = new Coupon(couponID, c.getTime());
                        customer.addCoupon(couponID, coupon);
                        couponSequence++;
                    }
                }
                lastCouponGeneration = new Date();
            }
        }
    }
    private void displayCoupon(String customerID) {
        if (!customerMap.containsKey(customerID)) {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.CUSTOMER_IDENTIFIER_DOES_NOT_EXIST);
        } else {
            Customer customer = customerMap.get(customerID);
            customer.getCouponList();
            TreeMap<String, Coupon> showCoupon = customer.getCouponList();
            String output = "CustomerID:%s,OrderID:%s,ExpirationDate:%s,Discount:%d";
            for (Map.Entry<String, Coupon> entry : showCoupon.entrySet()) {
                Coupon coupon = entry.getValue();
                System.out.println(String.format(output,
                        entry.getKey(),
                        coupon.getOrderID(),
                        coupon.getExpirationDate(),
                        coupon.getDiscount()
                        ));
            }
            printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.DISPLAY_COMPLETED);
        }
    }
    private void applyCoupon (String customerID, String couponID,String storeID, String orderID){
        if (!customerMap.containsKey(customerID)) {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.CUSTOMER_IDENTIFIER_DOES_NOT_EXIST);
        } else {
            Customer customer = customerMap.get(customerID);
            if (!customer.getCouponList().containsKey(couponID)) {
                printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.COUPON_IDENTIFIER_DOES_NOT_EXIST);
            } else {
                Coupon coupon = customer.getCouponList().get(couponID);
                Date currentDate = new Date();
                if (coupon.expirationDate.compareTo(currentDate) < 0) {
                    printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.COUPON_EXPIRED);
                } else {
                    if (!storesMap.containsKey(storeID)) {
                        printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.STORE_IDENTIFIER_DOES_NOT_EXIST);
                    } else {
                        Store store = storesMap.get(storeID);
                        if (!store.getOrders().containsKey(orderID)) {
                            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.ORDER_IDENTIFIER_ALREADY_EXISTS);
                        } else {
                            Order order = store.getOrders().get(orderID);
                            order.setCouponID(couponID);
                            printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.CHANGE_COMPLETED);
                        }
                    }
                }
            }
        }


    }
    private void couponInitHandler(String frequency, String r1, String r2, String r3, String r4, String r5, String couponValidDays) {
        try {
            couponGenFrequency = Integer.parseInt(frequency);
            rateDistribution = new double[6];
            rateDistribution[1] = Double.parseDouble(r1);
            rateDistribution[2] = Double.parseDouble(r2);
            rateDistribution[3] = Double.parseDouble(r3);
            rateDistribution[4] = Double.parseDouble(r4);
            rateDistribution[5] = Double.parseDouble(r5);
            this.couponValidDays = Integer.parseInt(couponValidDays);
        } catch(Exception e) {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.COUPON_FAILED);
        }
    }

    private double getDistance(Coordinate a, Coordinate b) {
        return Math.sqrt(Math.pow((a.x - b.x), 2) + Math.pow((a.y - b.y), 2));
    }

    private void printStatusMessage(String status, String details) {
        System.out.println(String.format("%s:%s", status, details));
    }

    private void makeStoreHandler(String storeID, String initRevenueStr, String xStr, String yStr) {
        if (!storesMap.containsKey(storeID)) {
            try {
                int initRevenue = Integer.parseInt(initRevenueStr);
                double x = Double.parseDouble(xStr);
                double y = Double.parseDouble(yStr);
                Coordinate location = new Coordinate(x, y);
                Store store = new Store(storeID, initRevenue, location);
                storesMap.put(storeID, store);
                printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.CHANGE_COMPLETED);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid value for revenue: " + initRevenueStr);
            }
        } else {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.STORE_IDENTIFIER_ALREADY_EXISTS);
        }
    }

    private void displayStoresHandler() {
        String output = "name:%s,revenue:%d,location_x:%f,location_y:%f";
        for (Map.Entry<String, Store> entry : storesMap.entrySet()) {
            Store store = entry.getValue();
            System.out.println(String.format(output,
                    entry.getKey(),
                    store.displayRevenue(),
                    store.getLocation().x,
                    store.getLocation().y));
        }
        printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.DISPLAY_COMPLETED);
    }

    private void sellItemHandler(String storeID, String itemName, String weight) {
        if (storesMap.containsKey(storeID)) {
            Store store = storesMap.get(storeID);
            if (store.addItem(itemName, weight)) {
                printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.CHANGE_COMPLETED);
            } else {
                printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.ITEM_IDENTIFIER_ALREADY_EXISTS);
            }
        } else {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.STORE_IDENTIFIER_DOES_NOT_EXIST);
        }
    }

    private void displayItemsHandler(String storeID) {
        if (storesMap.containsKey(storeID)) {
            Store store = storesMap.get(storeID);
            for (Map.Entry<String, Item> entry : store.getItems().entrySet()) {
                Item item = entry.getValue();
                System.out.println(entry.getKey() + "," + item.getWeight());
            }
            printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.DISPLAY_COMPLETED);
        } else {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.STORE_IDENTIFIER_DOES_NOT_EXIST);
        }
    }

    private void makePilotHandler(String pilotID,
                                  String firstName,
                                  String lastName,
                                  String phoneNumber,
                                  String taxIdentifier,
                                  String licenseID,
                                  String numberOfDeliveriesStr) {
        if (dronePilotMap.containsKey(pilotID)) {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.PILOT_IDENTIFIER_ALREADY_EXISTS);
        } else {
            boolean pilotWithSameLicenseFound = false;
            for (DronePilot pilot : dronePilotMap.values()) {
                if (pilot.getLicenseID().contentEquals(licenseID)) {
                    printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.PILOT_LICENSE_ALREADY_EXISTS);
                    pilotWithSameLicenseFound = true;
                    break;
                }
            }

            if (!pilotWithSameLicenseFound) {
                try {
                    int numberOfDeliveries = Integer.parseInt(numberOfDeliveriesStr);
                    DronePilot pilot = new DronePilot(
                            pilotID,
                            firstName,
                            lastName,
                            phoneNumber,
                            taxIdentifier,
                            licenseID,
                            numberOfDeliveries);
                    dronePilotMap.put(pilotID, pilot);
                    printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.CHANGE_COMPLETED);
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid value for experience: " + numberOfDeliveriesStr);
                }
            }
        }
    }

    private void displayPilotsHandler() {
        String output = "name:%s_%s,phone:%s,taxID:%s,licenseID:%s,experience:%d";
        for (Map.Entry<String, DronePilot> entry : dronePilotMap.entrySet()) {
            DronePilot pilot = entry.getValue();
            System.out.println(String.format(output,
                    pilot.getFirstName(),
                    pilot.getLastName(),
                    pilot.getPhoneNumber(),
                    pilot.getTaxID(),
                    pilot.getLicenseID(),
                    pilot.getExperience()));
        }
        printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.DISPLAY_COMPLETED);
    }

    private void makeDroneHandler(String storeID,
                                  String droneID,
                                  String weight,
                                  String numberOfDeliveries) {
        if (storesMap.containsKey(storeID)) {
            if (storesMap.get(storeID).buyDrone(droneID, weight, numberOfDeliveries)) {
                printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.CHANGE_COMPLETED);
            } else {
                printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.DRONE_IDENTIFIER_ALREADY_EXISTS);
            }
        } else {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.STORE_IDENTIFIER_DOES_NOT_EXIST);
        }
    }

    private void displayDronesHandler(String storeID) {
        if (!storesMap.containsKey(storeID)) {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.STORE_IDENTIFIER_DOES_NOT_EXIST);
        } else {
            Store store = storesMap.get(storeID);

            // without pilot
            String outputWithoutPilot = "droneID:%s,total_cap:%d,num_orders:%d,remaining_cap:%d,trips_left:%d,speed:%d";

            // with pilot
            String outputWithPilot = "droneID:%s,total_cap:%d,num_orders:%d,remaining_cap:%d,trips_left:%d,flown_by:%s_%s,speed:%d";

            for (Map.Entry<String, Drone> entry : store.getDrones().entrySet()) {
                Drone drone = entry.getValue();
                String lastPilotAccount = drone.getLastPilot();
                if (lastPilotAccount == null || lastPilotAccount.isEmpty() ||
                        !dronePilotMap.containsKey(lastPilotAccount)) {
                    System.out.println(String.format(outputWithoutPilot,
                            drone.getDroneID(),
                            drone.getMaxCapacity(),
                            drone.getOrders().size(),
                            drone.getRemainingCapacity(),
                            drone.getTripsLeft(),
                            drone.getSpeed()));
                } else {
                    DronePilot pilot = dronePilotMap.get(lastPilotAccount);
                    System.out.println(String.format(outputWithPilot,
                            drone.getDroneID(),
                            drone.getMaxCapacity(),
                            drone.getOrders().size(),
                            drone.getRemainingCapacity(),
                            drone.getTripsLeft(),
                            pilot.getFirstName(),
                            pilot.getLastName(),
                            drone.getSpeed()));
                }
            }

            printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.DISPLAY_COMPLETED);
        }
    }

    private void flyDroneHandler(String storeID, String droneID, String account) {
        if (!storesMap.containsKey(storeID)) {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.STORE_IDENTIFIER_DOES_NOT_EXIST);
        } else {
            Store store = storesMap.get(storeID);
            if (!store.getDrones().containsKey(droneID)) {
                printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.DRONE_IDENTIFIER_DOES_NOT_EXIST);
            } else {
                if (!dronePilotMap.containsKey(account)) {
                    printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.PILOT_IDENTIFIER_DOES_NOT_EXIST);
                } else {
                    /* Suppose pilotA is being assigned to fly droneX.
                        In this case:
                        (1) if pilotA is currently flying a different droneY,
                            then droneY will be left without a pilot; and,
                        (2) if droneX is currently being flown by a different pilotB,
                            then pilotB will be replaced by pilotA, and pilotB won’t fly
                            a drone again until they are reassigned
                     */
                    DronePilot pilot = dronePilotMap.get(account);
                    String previousDroneStore = pilot.getDroneStore();
                    String previousDroneID = pilot.getDroneID();

                    // if pilotA is flying droneY, then clear droneY's pilot to null
                    if (previousDroneStore != null && !previousDroneStore.isEmpty() &&
                            previousDroneID != null && !previousDroneID.isEmpty()) {
                        storesMap.get(previousDroneStore).getDrones().get(previousDroneID).assignPilot(null);
                    }

                    Drone drone = store.getDrones().get(droneID);

                    // if droneX is being flown by pilotB, then clear pilotB's drone to null
                    if (drone.getLastPilot() != null && !drone.getLastPilot().isEmpty()) {
                        dronePilotMap.get(drone.getLastPilot()).assignDrone(null, null);
                    }

                    // set droneX's pilot to pilotX
                    drone.assignPilot(pilot.getUserID());

                    // set pilotX's drone to droneA
                    pilot.assignDrone(store.getStoreName(), drone.getDroneID());

                    printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.CHANGE_COMPLETED);
                }
            }
        }
    }

    private void makeCustomerHandler(String account,
                                     String firstName,
                                     String lastName,
                                     String phoneNumber,
                                     String ratingStr,
                                     String creditStr,
                                     String xStr,
                                     String yStr
    ) {
        if (customerMap.containsKey(account)) {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.CUSTOMER_IDENTIFIER_ALREADY_EXISTS);
        } else {
            try {
                int rating = Integer.parseInt(ratingStr);
                int credit = Integer.parseInt(creditStr);
                double x = Double.parseDouble(xStr);
                double y = Double.parseDouble(yStr);
                Coordinate location = new Coordinate(x,y);
                Customer customer = new Customer(
                        account,
                        firstName,
                        lastName,
                        phoneNumber,
                        rating,
                        credit,
                        location);
                customerMap.put(account, customer);
                printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.CHANGE_COMPLETED);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid value for rating or credit");
            }
        }
    }

    private void displayCustomersHandler() {
        String output = "name:%s_%s,phone:%s,rating:%d,credit:%d,location_x:%f,location_y:%f";
        for (Map.Entry<String, Customer> entry : customerMap.entrySet()) {
            Customer customer = entry.getValue();
            System.out.println(String.format(output,
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getPhoneNumber(),
                    customer.getReliabilityRate(),
                    customer.getCredit(),
                    customer.getLocation().x,
                    customer.getLocation().y));
        }
        printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.DISPLAY_COMPLETED);
    }

    private void startOrderHandler(String storeID,
                                   String orderID,
                                   String droneID,
                                   String customerID) {
        if (!storesMap.containsKey(storeID))  {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.STORE_IDENTIFIER_DOES_NOT_EXIST);
        } else {
            Store store = storesMap.get(storeID);
            if (store.getOrders().containsKey(orderID)) {
                printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.ORDER_IDENTIFIER_ALREADY_EXISTS);
            } else if (!store.getDrones().containsKey(droneID)) {
                printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.DRONE_IDENTIFIER_DOES_NOT_EXIST);
            } else if (!customerMap.containsKey(customerID)) {
                printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.CUSTOMER_IDENTIFIER_DOES_NOT_EXIST);
            } else {
                Order order = new Order(orderID, droneID, customerID);
                store.getOrders().put(orderID, order);
                store.getDrones().get(droneID).addOrder(orderID);
                printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.CHANGE_COMPLETED);
            }
        }
    }

    private void displayOrdersHandler(String storeID) {
        if (!storesMap.containsKey(storeID)) {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.STORE_IDENTIFIER_DOES_NOT_EXIST);
        } else {
            String orderIDOutput = "orderID:%s";
            String lineOutput = "item_name:%s,total_quantity:%d,total_cost:%d,total_weight:%d";

            // For each order, display its orderID.
            for (Map.Entry<String, Order> entry : storesMap.get(storeID).getOrders().entrySet()) {
                Order order = entry.getValue();
                System.out.println(String.format(orderIDOutput, entry.getKey()));

                // If the order contains one or more lines, then display the line info
                for (Map.Entry<String, Line> lineEntry : order.getLines().entrySet()) {
                    Line line = lineEntry.getValue();
                    System.out.println(String.format((lineOutput),
                            line.getItem().getItemName(),
                            line.getQuantity(),
                            line.calculateCost(),
                            line.calculateWeight()));
                }
            }

            printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.DISPLAY_COMPLETED);
        }
    }

    private void requestItemHandler(String storeID,
                                    String orderID,
                                    String itemName,
                                    String quantityStr,
                                    String unitPriceStr) {
        if (!storesMap.containsKey(storeID)) {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.STORE_IDENTIFIER_DOES_NOT_EXIST);
        } else {
            Store store = storesMap.get(storeID);
            if (!store.getOrders().containsKey(orderID)) {
                printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.ORDER_IDENTIFIER_DOES_NOT_EXIST);
            } else if (!store.getItems().containsKey(itemName)) {
                printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.ITEM_IDENTIFIER_DOES_NOT_EXIST);
            } else {
                // Check each line of the order.
                // If a line contains the same item name as itemName,
                // then return error "item_already_ordered"
                Order order = store.getOrders().get(orderID);
                for (Map.Entry<String, Line> lineEntry : order.getLines().entrySet()) {
                    Line line = lineEntry.getValue();
                    if (line.getItem().getItemName().contentEquals(itemName)) {
                        printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.ITEM_ALREADY_ORDERED);
                        return;
                    }
                }

                try {
                    int quantity = Integer.parseInt(quantityStr);

                    // check if the customer has enough remaining credits to afford the new item
                    int unitPrice = Integer.parseInt(unitPriceStr);
                    int creditsRequired = quantity * unitPrice;
                    Customer customer = customerMap.get(order.getCustomerID());
                    if (creditsRequired > (customer.getCredit() - customer.getPendingCharges())) {
                        printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.CUSTOMER_CANT_AFFORD_NEW_ITEM);
                        return;
                    }

                    // check if the drone has enough remaining capacity
                    // to carry the new item as part of its payload
                    int weight = store.getItems().get(itemName).getWeight();
                    int capacityRequired = quantity * weight;
                    Drone drone = store.getDrones().get(order.getDroneID());
                    int droneRemainingCapacity = drone.getRemainingCapacity();
                    if (capacityRequired > droneRemainingCapacity) {
                        printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.DRONE_CANT_CARRY_NEW_ITEM);
                        return;
                    }

                    // add new line with the item requested
                    Line line = new Line(unitPrice, quantity, store.getItems().get(itemName));
                    order.addLine(itemName, line);

                    // update drone's remaining capacity
                    drone.reduceCapacity(capacityRequired);

                    // increase customer's pending charges
                    customer.increasePendingCharges(creditsRequired);

                    printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.CHANGE_COMPLETED);
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid weight.");
                }
            }
        }
    }

    private void purchaseOrderHandler(String storeID, String orderID) {
        if (!storesMap.containsKey(storeID)) {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.STORE_IDENTIFIER_DOES_NOT_EXIST);
        } else {
            Store store = storesMap.get(storeID);
            if (!store.getOrders().containsKey(orderID)) {
                printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.ORDER_IDENTIFIER_DOES_NOT_EXIST);
            } else {
                Order order = store.getOrders().get(orderID);
                Drone drone = store.getDrones().get(order.getDroneID());
                if (drone == null) {
                    System.out.println("Missing drone with ID: " + order.getDroneID());
                    return;
                } else if (drone.getLastPilot() == null) {
                    printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.DRONE_NEEDS_PILOT);
                    return;
                } else if (drone.getTripsLeft() <= 0) {
                    printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.DRONE_NEEDS_FUEL);
                    return;
                }

                // (1) the cost of the order must be deducted from the customer’s credits
                Customer customer = customerMap.get(order.getCustomerID());
                double Time = getDistance(customer.getLocation(), store.getLocation()) / drone.getSpeed();

                if(order.getCouponID() != null) {
                    // if order has a coupon, and coupon does not/no longer exist
                    // TODO: what happens if there's no coupon (i.e., couponID is null or "")
                    if (!customer.getCouponList().containsKey(order.getCouponID())) {
                        printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.COUPON_IDENTIFIER_DOES_NOT_EXIST);
                    } else {
                        Coupon coupon = customer.getCouponList().get(order.getCouponID());
                        Date currentDate = new Date();
                        if (coupon.expirationDate.compareTo(currentDate) < 0) {
                            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.COUPON_EXPIRED);
                            customer.getCouponList().remove(order.getCouponID());
                            return;
                        } else {
                            long diffInMillies = coupon.expirationDate.getTime() - currentDate.getTime();
                            double couponValidPeriod = (double) (TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS));

                            // if coupon will expire before order can be delivered
                            if (Time > couponValidPeriod) {
                                printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.STORE_CANNOT_DELIVER);
                                int penalty = coupon.getDiscount();
                                customer.increaseCredit(penalty);//penalty for customer
                                store.decreaseRevenue(penalty); // store penalty

                                // cancel order
                                drone.getOrders().remove(orderID);
                                drone.restoreCapacity(order.getTotalWeight());
                                customer.restorePendingCharges(order.getTotalCost());
                                store.getOrders().remove(orderID);
                                return;
                            } else {
                                // apply coupon to order
                                customer.restorePendingCharges(coupon.getDiscount());
                                order.totalCostWithCoupon(coupon.getDiscount());//the cost for discounted item
                            }
                        }
                    }
                }
                customer.deductCredit(order.getTotalCost());

                // TODO: maybe this should be
                //       customer.restorePendingCharges(order.getTotalCost())
                customer.resetPendingCharges();
                // (2) the cost of the order must be added to the store’s revenue
                store.increaseRevenue(order.getTotalCost());

                // (3) the number of remaining deliveries (i.e., fuel) for the drone must be reduced by one
                drone.reduceTrip(1);
                drone.getOrders().remove(orderID);
                drone.restoreCapacity(order.getTotalWeight());

                // (4) the pilot’s experience (i.e., number of successful deliveries) must be increased by one
                dronePilotMap.get(drone.getLastPilot()).increaseExperience(1);

                // (5) the order must otherwise be removed from the system
                store.getOrders().remove(orderID);

                printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.CHANGE_COMPLETED);
            }
        }
    }

    private void cancelOrderHandler(String storeID, String orderID) {
        if (!storesMap.containsKey(storeID)) {
            printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.STORE_IDENTIFIER_DOES_NOT_EXIST);
        } else {
            Store store = storesMap.get(storeID);
            if (!store.getOrders().containsKey(orderID)) {
                printStatusMessage(StatusMessage.STATUS_ERROR, StatusMessage.ORDER_IDENTIFIER_DOES_NOT_EXIST);
            } else {
                Order order = store.getOrders().get(orderID);

                // remove order from drone, and restore its capacity
                Drone drone = store.getDrones().get(order.getDroneID());
                drone.getOrders().remove(orderID);
                drone.restoreCapacity(order.getTotalWeight());

                // restore order's total price from customer's pending charges
                customerMap.get(order.getCustomerID()).restorePendingCharges(order.getTotalCost());

                // remove order from store
                store.getOrders().remove(orderID);

                printStatusMessage(StatusMessage.STATUS_OK, StatusMessage.CHANGE_COMPLETED);
            }
        }
    }
}
