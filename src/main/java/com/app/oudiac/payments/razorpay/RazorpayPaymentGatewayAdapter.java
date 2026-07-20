package com.app.oudiac.payments.razorpay;

import com.app.oudiac.models.Payment;
import com.app.oudiac.models.enums.PaymentGateway;
import com.app.oudiac.models.enums.PaymentStatus;
import com.app.oudiac.payments.adapter.PaymentGatewayAdapter;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RazorpayPaymentGatewayAdapter implements PaymentGatewayAdapter {

    private final RazorpayClient razorpay;

    @Value("${razorpay.api.secret}")
    private String razorpayApiSecret;

    @Override
    public String createOrder(String orderNumber,BigDecimal amount, String currency) throws Exception {

        // Convert Rupees to Paise (Multiply by 100)
//        BigDecimal amount=order.getTotalAmount();
        int amountInPaise = amount.multiply(new BigDecimal("100")).intValue();

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", orderNumber); // E.g., OUD-2026-A7F9B

        // Ask Razorpay to create the order
        Order razorpayOrder = razorpay.orders.create(orderRequest);
        System.out.println("Razorpay Order created: " + razorpayOrder.toString());

        // Return the Razorpay Order ID (e.g., "order_H43k...") to the frontend
        return razorpayOrder.get("id");
    }

    @Override
    public String getGatewayName() {
        return "RAZORPAY";
    }

//    @Override
//    public ResponseEntity<?> verifyPayment(Map<String, String> paymentDetails) {
//        try {
//            // 1. Extract the data sent by React
//            String razorpayOrderId = paymentDetails.get("razorpayOrderId");
//            String razorpayPaymentId = paymentDetails.get("razorpayPaymentId");
//            String razorpaySignature = paymentDetails.get("razorpaySignature");
//
//            // 2. Build the payload exactly how Razorpay's SDK expects it
//            JSONObject verificationOptions = new JSONObject();
//            verificationOptions.put("razorpay_order_id", razorpayOrderId);
//            verificationOptions.put("razorpay_payment_id", razorpayPaymentId);
//            verificationOptions.put("razorpay_signature", razorpaySignature);
//
//            // 3. Cryptographically verify the signature using your hidden Key Secret
//            boolean isValid = Utils.verifyPaymentSignature(verificationOptions, razorpayApiSecret);
//
//            if (isValid) {
//                System.out.println("✅ Secure Verification Success! Payment ID: " + razorpayPaymentId);
//
//                // TODO: Find order by razorpayOrderId in your database
//                // TODO: Update order status to "PAID"
//                // TODO: Save to database
//
//
//                Map<String, String> response = new HashMap<>();
//                response.put("status", "success");
//                response.put("message", "Payment verified and order updated.");
//                return ResponseEntity.ok(response);
//            } else {
//                System.err.println("❌ ALERT: Fraudulent payment signature detected!");
//                return ResponseEntity.badRequest().body("Signature verification failed.");
//            }
//
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Error verifying payment: " + e.getMessage());
//        }
//    }
}
