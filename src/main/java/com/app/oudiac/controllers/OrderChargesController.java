package com.app.oudiac.controllers;

import com.app.oudiac.dtos.OrderChargesDtos.OrderChargesRequestDto;
import com.app.oudiac.dtos.OrderChargesDtos.OrderChargesResponseDto;
import com.app.oudiac.services.orderChrgesService.OrderChargesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order-charges/oudiac")
public class OrderChargesController {

    private final OrderChargesService orderChargesService;

    @PostMapping("/manager/add-charges")
    public ResponseEntity<OrderChargesResponseDto> addOrderCharges(@RequestBody OrderChargesRequestDto request){
        return orderChargesService.addOrderCharges(request);
    }
}
