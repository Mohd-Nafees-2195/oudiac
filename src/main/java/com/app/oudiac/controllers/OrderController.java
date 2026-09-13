package com.app.oudiac.controllers;

import com.app.oudiac.dtos.OrderDtos.OrderRequestDto;
import com.app.oudiac.dtos.OrderDtos.OrderResponseDto;
import com.app.oudiac.models.User;
import com.app.oudiac.models.enums.OrderStatus;
import com.app.oudiac.services.orderService.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders/oudiac")
public class OrderController {

    @Autowired
    private OrderService orderService;

//    @PostMapping("/oudiac/place_order")
//    public ResponseEntity<String> placeOrder(@RequestBody @Valid OrderRequestDto orderRequestDTO, @RequestHeader(value = "Idempotency-Key") String idempotencyKey, @AuthenticationPrincipal User loggedInUser) {
//        System.out.println("Hittinh PlaceOrder : "+idempotencyKey + " principal "+loggedInUser);
//        orderService.placeOrder(orderRequestDTO, loggedInUser,idempotencyKey);
//        return new ResponseEntity<>("Order placed Success", HttpStatus.OK);
//    }
    @PostMapping("/place_order")
    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody @Valid OrderRequestDto orderRequestDTO, @RequestHeader(value = "Idempotency-Key") String idempotencyKey, Principal principal) {
//        System.out.println("Hittinh PlaceOrder : "+idempotencyKey + " principal "+principal.getName());
        try{
            return orderService.placeOrder(orderRequestDTO, principal.getName(),idempotencyKey,"Razorpay","INR");
        }catch (Exception e) {
            System.err.println("RAZORPAY REJECTED THE ORDER: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get/{orderNumber}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable String orderNumber,Principal principal) {
        return orderService.getOrder(orderNumber,principal);
    }

    @GetMapping("/get-order/{id}")
    public Page<OrderResponseDto> getOrdersByUserId(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size,
                                                    Principal principal) {
        return orderService.getOrdersByUserId(page,size,principal);
    }

    //Accessible by manager only
    @GetMapping("/manager/get-orders")
    public Page<OrderResponseDto> getOrders(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size,
                                                  Principal principal) {
        return orderService.getOrders(page,size,principal);
    }

    @GetMapping("/manager/get-curr-orders")
    public Page<OrderResponseDto> getCurrentDayOrders(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size,
                                            Principal principal) {
        return orderService.getCurrDayOrders(page,size,principal);
    }


    @PutMapping("/manager/order-update/{id}/{newStatus}/{title}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable Long id, @PathVariable OrderStatus newStatus,@PathVariable String title, Principal principal) {
        return orderService.updateOrder(id,newStatus,title,principal);
    }


}
