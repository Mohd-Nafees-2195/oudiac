package com.app.oudiac.payments.adapter;

import com.app.oudiac.models.Order;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

public interface PaymentGatewayAdapter {
    String createOrder(String orderNumber,BigDecimal amount, String  currency) throws Exception;
    String getGatewayName();
//    ResponseEntity<?> verifyPayment(Map<String, String> paymentDetails);
}
