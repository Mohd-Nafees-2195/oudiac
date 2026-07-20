package com.app.oudiac.dtos.OrderDtos;

import com.app.oudiac.models.Order;
import com.app.oudiac.models.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderResponseDto {
    private Long id;
    private String orderNumber;
    private String orderId; //RazorPay Order Id
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;

    public static OrderResponseDto from(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(order.getId());
        orderResponseDto.setOrderNumber(order.getOrderNumber());
        orderResponseDto.setOrderStatus(order.getStatus());
        orderResponseDto.setTotalPrice(order.getTotalAmount());
        orderResponseDto.setOrderId(order.getOrderId());
        return orderResponseDto;
    }
}
