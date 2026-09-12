package com.app.oudiac.models.enums;

public enum OrderStatus {
    PENDING,  //Order Placed
    CONFIRMED,  //Payment Confirmed
    PROCESSING,
    PACKED,
    SHIPPED,   //Handed to Courier
    OUT_FOR_DELIVERY,
    DELIVERED, //Delivered

    CANCELLED, //Cancelled
    RETURN_REQUESTED,
    RETURN_APPROVED,
    RETURNED,  

    REFUND_REQUESTED,
    REFUND_PROCESSING,
    REFUNDED,

    PAYMENT_FAILED
}
