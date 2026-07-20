package com.app.oudiac.dtos.OrderDtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemDTO {
    @NotNull
    private Long productId;

    @NotNull
    private Long variantId; // e.g., "50ml"

    @NotNull
    private Integer quantity;
}
