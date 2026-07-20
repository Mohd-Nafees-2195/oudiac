package com.app.oudiac.dtos.paymentDtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentUrlDto {
    private String paymentUrl;
    private BigDecimal amount;
    private String orderNumber;
}
