package com.app.oudiac.controllers;

import com.app.oudiac.exceptions.OrderNotFoundException;
import com.app.oudiac.models.Order;
import com.app.oudiac.payments.adapter.PaymentGatewayAdapter;
import com.app.oudiac.repositories.OrderRepository;
import com.app.oudiac.services.orderService.OrderService;
import com.app.oudiac.services.paymentService.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

//    @GetMapping("/oudiac/make-payment/{orderNumber}/pay/razorpay")
//    public ResponseEntity<?> initiateRazorpayPayment(@PathVariable String orderNumber, Principal principal)  {
//
//        // 2. Generate the Razorpay ID
//        try {
//            return paymentService.createOrder(orderNumber,"INR","Razorpay",principal.getName());
//        } catch (Exception e) {
//            System.err.println("RAZORPAY REJECTED THE ORDER: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }

    @PostMapping("/webhook")
    public ResponseEntity<?> verifyPayment(@RequestBody String payload,@RequestHeader(value = "x-razorpay-signature", required = false) String signature)  {
        try {
            return paymentService.handlePaymentWebhook(payload, signature);
        } catch (Exception e) {
            System.err.println("RAZORPAY REJECTED THE ORDER: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
