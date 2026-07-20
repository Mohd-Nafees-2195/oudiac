package com.app.oudiac.payments.Cod;

import com.app.oudiac.models.Order;
import com.app.oudiac.payments.adapter.PaymentGatewayAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class CodAdapter implements PaymentGatewayAdapter {

    @Override
    public String createOrder(String orderNumber,BigDecimal amount, String currency) throws Exception {
        System.out.println("Processing COD confirmation...");
        return "COD_CONFIRMED";
    }

    @Override
    public String getGatewayName() {
        return "COD";
    }

//    @Override
//    public ResponseEntity<?> verifyPayment(Map<String, String> paymentDetails) {
//        return new ResponseEntity<>(true,HttpStatus.OK);
//    }
}
