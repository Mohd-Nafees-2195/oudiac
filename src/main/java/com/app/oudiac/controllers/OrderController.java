package com.app.oudiac.controllers;

import com.app.oudiac.dtos.OrderDtos.OrderRequestDto;
import com.app.oudiac.dtos.OrderDtos.OrderResponseDto;
import com.app.oudiac.models.User;
import com.app.oudiac.services.orderService.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

//    @PostMapping("/oudiac/place_order")
//    public ResponseEntity<String> placeOrder(@RequestBody @Valid OrderRequestDto orderRequestDTO, @RequestHeader(value = "Idempotency-Key") String idempotencyKey, @AuthenticationPrincipal User loggedInUser) {
//        System.out.println("Hittinh PlaceOrder : "+idempotencyKey + " principal "+loggedInUser);
//        orderService.placeOrder(orderRequestDTO, loggedInUser,idempotencyKey);
//        return new ResponseEntity<>("Order placed Success", HttpStatus.OK);
//    }
    @PostMapping("/oudiac/place_order")
    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody @Valid OrderRequestDto orderRequestDTO, @RequestHeader(value = "Idempotency-Key") String idempotencyKey, Principal principal) {
//        System.out.println("Hittinh PlaceOrder : "+idempotencyKey + " principal "+principal.getName());
        try{
            return orderService.placeOrder(orderRequestDTO, principal.getName(),idempotencyKey,"Razorpay","INR");
        }catch (Exception e) {
            System.err.println("RAZORPAY REJECTED THE ORDER: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/oudiac/get/{orderNumber}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable String orderNumber,Principal principal) {
        System.out.println("getOrder");
        return orderService.getOrder(orderNumber,principal);
    }
}
