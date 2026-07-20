package com.app.oudiac.dtos.OrderDtos;

import com.app.oudiac.models.enums.PaymentMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {

    @NotNull
    private Long addressId;

    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemDTO> items;

}
