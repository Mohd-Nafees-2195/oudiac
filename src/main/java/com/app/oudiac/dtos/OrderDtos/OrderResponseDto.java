package com.app.oudiac.dtos.OrderDtos;

import com.app.oudiac.models.Order;
import com.app.oudiac.models.enums.OrderStatus;
import com.app.oudiac.models.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderResponseDto {
    private Long id;
    private String orderNumber;
    private String orderId; //RazorPay Order Id
    private String customer;
    private String phone;
    private Long items;
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
    private Date date;
    private PaymentStatus payment;


//    customer: "Anita Desai",
//    phone: "+91 98765 43215",
//    items: 4,
//    amount: 620,
//    date: "Yesterday, 08:30 PM",
//    status: "Delivered",
//    payment: "COD",

    public static OrderResponseDto from(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(order.getId());
        orderResponseDto.setOrderNumber(order.getOrderNumber());
        orderResponseDto.setOrderStatus(order.getStatus());
        orderResponseDto.setTotalPrice(order.getTotalAmount());
        orderResponseDto.setOrderId(order.getOrderId());

        orderResponseDto.setCustomer(order.getShippingName());
        orderResponseDto.setPhone(order.getShippingPhone());
        orderResponseDto.setItems(5L);// currently total item not saving into order table, please chang the code while ordering save item count as well
        orderResponseDto.setDate(order.getCreated_at());
        orderResponseDto.setPayment(order.getPaymentStatus());
        return orderResponseDto;
    }
}
