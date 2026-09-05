package com.app.oudiac.dtos.OrderDtos;

import com.app.oudiac.models.OrderItem;
import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponseDto {
    private Long id;
    private String productName;
    private String variantType; // e.g., "50ml"
    private BigDecimal unitPrice;
    private int quantity;
    private String url;

    public static OrderItemResponseDto fromOrderItem(OrderItem orderItem) {
        OrderItemResponseDto responseDto=new OrderItemResponseDto();
        responseDto.setId(orderItem.getId());
        responseDto.setProductName(orderItem.getProductName());
        responseDto.setVariantType(orderItem.getVariantType());
        responseDto.setUnitPrice(orderItem.getUnitPrice());
        responseDto.setQuantity(orderItem.getQuantity());
        responseDto.setUrl(orderItem.getUrl());
        return responseDto;
    }
}
