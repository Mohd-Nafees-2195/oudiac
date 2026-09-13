package com.app.oudiac.dtos.OrderChargesDtos;

import com.app.oudiac.models.OrderCharges;
import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderChargesRequestDto {
    private BigDecimal gst;
    private BigDecimal shippingFee;
    private BigDecimal freeShippingThreshold;

    public static OrderCharges fromOrderChargesRequestDto(OrderChargesRequestDto request){
        OrderCharges newOrderCharges=new OrderCharges();
        newOrderCharges.setGst(request.getGst());
        newOrderCharges.setShippingFee(request.getShippingFee());
        newOrderCharges.setFreeShippingThreshold(request.getFreeShippingThreshold());

        newOrderCharges.setCreated_at(new Date());
        newOrderCharges.setUpdated_at(new Date());
        newOrderCharges.setIsDeleted(false);
        return newOrderCharges;
    }
}
