package com.app.oudiac.dtos.OrderDtos;

import com.app.oudiac.models.Order;
import com.app.oudiac.models.OrderItem;
import com.app.oudiac.models.enums.OrderStatus;
import com.app.oudiac.models.enums.PaymentStatus;
import jakarta.persistence.Column;
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
    private String email;
    private List<OrderItemResponseDto> orderItems;
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
    private BigDecimal subTotal;
    private BigDecimal shippingFee;
    private BigDecimal discount;
    private BigDecimal taxAmount;
    private Date date;
    private PaymentStatus payment;

    //Shipping Address
    private String shippingName;
    private String shippingPhone;
    private String shippingAddress;
    private String shippingCity;
    private String shippingPincode;
    private String shippingState;
    private String shippingCountry;

    public static OrderResponseDto from(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(order.getId());
        orderResponseDto.setOrderNumber(order.getOrderNumber());
        orderResponseDto.setOrderStatus(order.getStatus());
        orderResponseDto.setTotalPrice(order.getTotalAmount());
        orderResponseDto.setSubTotal(order.getSubTotal());
        orderResponseDto.setShippingFee(order.getShippingFee());
        orderResponseDto.setDiscount(order.getDiscount());
        orderResponseDto.setTaxAmount(order.getTaxAmount());
        orderResponseDto.setOrderId(order.getOrderId());
        //Shipping Detains
        orderResponseDto.setShippingName(order.getShippingName());
        orderResponseDto.setShippingPhone(order.getShippingPhone());
        orderResponseDto.setShippingAddress(order.getShippingAddress());
        orderResponseDto.setShippingCity(order.getShippingCity());
        orderResponseDto.setShippingPincode(order.getShippingPincode());
        orderResponseDto.setShippingState(order.getShippingState());
        orderResponseDto.setShippingCountry(order.getCountry());

        //Customer details, currently we are sending shipping details as customer details later change with customer details
        orderResponseDto.setCustomer(order.getShippingName());
        orderResponseDto.setPhone(order.getShippingPhone());
        //Items details
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
