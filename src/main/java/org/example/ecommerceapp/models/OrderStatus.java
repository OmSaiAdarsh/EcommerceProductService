package org.example.ecommerceapp.models;

public enum OrderStatus {
    INITIATED(1),
    PAYMENT_PROCESSING(2),
    CONFIRMED(3),
    DELIVERED(4),
    CANCELLED(5);


    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

}
