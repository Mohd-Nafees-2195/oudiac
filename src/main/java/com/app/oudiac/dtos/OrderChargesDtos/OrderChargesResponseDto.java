package com.app.oudiac.dtos.OrderChargesDtos;

import com.app.oudiac.models.OrderCharges;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderChargesResponseDto {
    private Long id;
    private BigDecimal gst;
    private BigDecimal shippingFee;
    private BigDecimal freeShippingThreshold;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isDeleted;

    public static OrderChargesResponseDto fromOrderCharges(OrderCharges orderCharges){
        OrderChargesResponseDto response=new OrderChargesResponseDto();
        response.setId(orderCharges.getId());
        response.setGst(orderCharges.getGst());
        response.setShippingFee(orderCharges.getShippingFee());
        response.setFreeShippingThreshold(orderCharges.getFreeShippingThreshold());
        response.setCreatedAt(orderCharges.getCreated_at());
        response.setUpdatedAt(orderCharges.getUpdated_at());
        response.setIsDeleted(orderCharges.getIsDeleted());
        return response;
    }
}
