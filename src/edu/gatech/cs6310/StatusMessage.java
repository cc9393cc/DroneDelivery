package edu.gatech.cs6310;

public interface StatusMessage {
    public static final String STATUS_OK = "OK";
    public static final String STATUS_ERROR = "ERROR";

    public static final String CHANGE_COMPLETED = "change_completed";
    public static final String DISPLAY_COMPLETED = "display_completed";

    public static final String CUSTOMER_CANT_AFFORD_NEW_ITEM = "customer_cant_afford_new_item";
    public static final String CUSTOMER_IDENTIFIER_ALREADY_EXISTS = "customer_identifier_already_exists";
    public static final String CUSTOMER_IDENTIFIER_DOES_NOT_EXIST = "customer_identifier_does_not_exist";
    public static final String DRONE_CANT_CARRY_NEW_ITEM = "drone_cant_carry_new_item";
    public static final String DRONE_IDENTIFIER_ALREADY_EXISTS = "drone_identifier_already_exists";
    public static final String DRONE_IDENTIFIER_DOES_NOT_EXIST = "drone_identifier_does_not_exist";
    public static final String DRONE_NEEDS_FUEL = "drone_needs_fuel";
    public static final String DRONE_NEEDS_PILOT = "drone_needs_pilot";
    public static final String ITEM_ALREADY_ORDERED = "item_already_ordered";
    public static final String ITEM_IDENTIFIER_ALREADY_EXISTS = "item_identifier_already_exists";
    public static final String ITEM_IDENTIFIER_DOES_NOT_EXIST = "item_identifier_does_not_exist";
    public static final String ORDER_IDENTIFIER_ALREADY_EXISTS = "order_identifier_already_exists";
    public static final String ORDER_IDENTIFIER_DOES_NOT_EXIST = "order_identifier_does_not_exist";
    public static final String PILOT_IDENTIFIER_ALREADY_EXISTS = "pilot_identifier_already_exists";
    public static final String PILOT_IDENTIFIER_DOES_NOT_EXIST = "pilot_identifier_does_not_exist";
    public static final String PILOT_LICENSE_ALREADY_EXISTS = "pilot_license_already_exists";
    public static final String STORE_IDENTIFIER_ALREADY_EXISTS = "store_identifier_already_exists";
    public static final String STORE_IDENTIFIER_DOES_NOT_EXIST = "store_identifier_does_not_exist";
    public static final String COUPON_IDENTIFIER_DOES_NOT_EXIST = "coupon_identifier_does_not_exist";
    public static final String COUPON_EXPIRED = "coupon_expired";
    public static final String COUPON_FAILED = "invalid_produce_coupon";
    public static final String STORE_CANNOT_DELIVER = "store_cannot_deliver_the_order_in_time";


}
