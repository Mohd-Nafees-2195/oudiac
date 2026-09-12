package com.app.oudiac.services.orderHistoyService;

import com.app.oudiac.dtos.OrderDtos.OrderResponseDto;
import com.app.oudiac.dtos.OrderHistoryDtos.OrderHistoryResDto;
import com.app.oudiac.models.Order;
import com.app.oudiac.models.OrderHistory;
import com.app.oudiac.repositories.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;

    public ResponseEntity<List<OrderHistoryResDto>> getOrderHistory(Long orderId) {

        List<OrderHistory> orderHistories=orderHistoryRepository.findByOrderId(orderId);
        List<OrderHistoryResDto> resDtos=new ArrayList<>();
        for(OrderHistory orderHistory:orderHistories){
            resDtos.add(OrderHistoryResDto.fromOrderHistory(orderHistory));
        }
        return new ResponseEntity<>(resDtos, HttpStatus.OK);
    }
}
