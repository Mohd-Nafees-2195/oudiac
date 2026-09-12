package com.app.oudiac.controllers;

import com.app.oudiac.dtos.OrderDtos.OrderResponseDto;
import com.app.oudiac.dtos.OrderHistoryDtos.OrderHistoryResDto;
import com.app.oudiac.services.orderHistoyService.OrderHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/order-history/oudiac")
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;

    @GetMapping("/get-history/{orderId}")
    public ResponseEntity<List<OrderHistoryResDto>> getOrderHistory(@PathVariable Long orderId){
        return orderHistoryService.getOrderHistory(orderId);
    }
}
