package com.app.oudiac.dtos.OrderDtos;

import com.app.oudiac.models.Order;
import com.app.oudiac.models.OrderItem;
import com.app.oudiac.models.enums.OrderStatus;
import com.app.oudiac.models.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private String orderNumber;
    private String orderId; //RazorPay Order Id
    private String customer;
    private String phone;
    private List<OrderItemResponseDto> orderItems;
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
        List<OrderItemResponseDto> orderItemsDto=new ArrayList<>();
        for(OrderItem orderItem: order.getItems()){
            orderItemsDto.add(OrderItemResponseDto.fromOrderItem(orderItem));
        }
        orderResponseDto.setOrderItems(orderItemsDto);
        orderResponseDto.setDate(order.getCreated_at());
        orderResponseDto.setPayment(order.getPaymentStatus());
        return orderResponseDto;
    }
}
