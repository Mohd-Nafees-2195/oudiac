package com.app.oudiac.dtos.OrderHistoryDtos;

import com.app.oudiac.models.OrderHistory;
import com.app.oudiac.models.enums.OrderStatus;
import lombok.Data;

import java.util.Date;

@Data
public class OrderHistoryResDto {
    private Long id;
    private OrderStatus status;
    private Long changedByUserId;
    private String notes;
    private String title;
    private Date updatedAt;

    public  static OrderHistoryResDto fromOrderHistory(OrderHistory orderHistory){
        OrderHistoryResDto resDto=new OrderHistoryResDto();
        resDto.setId(orderHistory.getId());
        resDto.setStatus(orderHistory.getStatus());
        resDto.setChangedByUserId(orderHistory.getChangedByUserId());
        resDto.setNotes(orderHistory.getNotes());
        resDto.setTitle(orderHistory.getTitle());
        resDto.setUpdatedAt(orderHistory.getUpdated_at());
        return resDto;
    }
}
