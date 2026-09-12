package com.app.oudiac.services.paymentService;

import com.app.oudiac.dtos.paymentDtos.PaymentUrlDto;
import com.app.oudiac.exceptions.OrderNotFoundException;
import com.app.oudiac.exceptions.UserNotFoundException;
import com.app.oudiac.models.Order;
import com.app.oudiac.models.OrderHistory;
import com.app.oudiac.models.Payment;
import com.app.oudiac.models.User;
import com.app.oudiac.models.enums.OrderStatus;
import com.app.oudiac.models.enums.PaymentGateway;
import com.app.oudiac.models.enums.PaymentMethod;
import com.app.oudiac.models.enums.PaymentStatus;
import com.app.oudiac.payments.adapter.PaymentGatewayAdapter;
import com.app.oudiac.payments.adapter.PaymentGatewayAdapterFactory;
import com.app.oudiac.repositories.OrderHistoryRepository;
import com.app.oudiac.repositories.OrderRepository;
import com.app.oudiac.repositories.UserRepository;
import com.razorpay.Utils;
import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentGatewayAdapterFactory paymentGatewayAdapterFactory;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    // This is a NEW secret specifically for webhooks.
    // You will generate this in the Razorpay Dashboard.
    @Value("${razorpay.api.secret}")  //razorpay.webhook.secret
    private String webhookSecret;

//    public ResponseEntity<PaymentUrlDto> createOrder(String orderNumber, String inr, String adapter,String email) throws Exception {
//
//        // 1. Fetch your Order from the database using orderNumber
//        // Order myOrder = orderRepository.findByOrderNumber(orderNumber);
//        Optional< Order > order=orderRepository.findByOrderNumber(orderNumber);
//        if(order.isEmpty()){
//            throw new OrderNotFoundException("Order not found");
//        }
//
//        Optional<User> user=userRepository.findByEmail(email);
//        if(user.isEmpty()){
//            throw new UserNotFoundException("User not found");
//        }
//
////        PaymentGatewayAdapter paymentGatewayAdapter=paymentGatewayAdapterFactory.getAdapter(adapter);
////        String rzpOrderId=paymentGatewayAdapter.createOrder(order.get(),inr);
//
//        List<Payment> payments=new ArrayList<>();
//        Payment payment=new Payment();
//        payment.setAmount(order.get().getTotalAmount());
//        payment.setCurrency(inr);
////        payment.setGatewayTransactionId(rzpOrderId);
//        payment.setStatus(PaymentStatus.PENDING);
//        payment.setOrder(order.get());
//        payment.setPaymentGateway(PaymentGateway.RAZORPAY); //or Razorpay
//        payment.setUser(user.get());
//        payments.add(payment);
//        order.get().setPayments(payments);
//
//
//        orderRepository.save(order.get());
//        PaymentUrlDto response=new PaymentUrlDto();
////        response.setPaymentUrl(rzpOrderId);
//        response.setAmount(order.get().getTotalAmount());
//        response.setOrderNumber(order.get().getOrderNumber());
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    public ResponseEntity<?> verify(Map<String, String> paymentPayload) {
//        PaymentGatewayAdapter paymentGatewayAdapter=paymentGatewayAdapterFactory.getAdapter("Razorpay");
//        return paymentGatewayAdapter.verifyPayment(paymentPayload);
//    }

    //Handle Webhook call from razorpay

    public ResponseEntity<String> handlePaymentWebhook(String payload, String signature) throws Exception {
        try {
            // 1. Verify the signature securely
            // This ensures the request actually came from Razorpay
            System.out.println("Webhook is working "+payload);
            boolean isValid = Utils.verifyWebhookSignature(payload, signature, webhookSecret);

            if (!isValid) {
                System.err.println("❌ Invalid Webhook Signature!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Signature");
            }

            // 2. Parse the payload JSON
            JSONObject payloadJson = new JSONObject(payload);
            String event = payloadJson.getString("event");

            System.out.println("✅ Webhook Received: " + event);

            // 3. Handle specific events
            if ("order.paid".equals(event) || "payment.captured".equals(event)) {

                // Navigate through the nested JSON to get the IDs
                JSONObject paymentEntity = payloadJson
                        .getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

                String orderId = paymentEntity.getString("order_id");
                String paymentId = paymentEntity.getString("id");
//                String paymentStatus = paymentEntity.getString("payment_status");
                String paymentMethod = paymentEntity.getString("method");

                System.out.println("Processing Success for Order: " + orderId);
                // TODO: Update your database order status to "PAID"

                UpdateDbOrder(orderId,paymentId,PaymentStatus.SUCCESS,paymentMethod,OrderStatus.CONFIRMED,"Payment Confirmed");

            } else if ("payment.failed".equals(event)) {

                JSONObject paymentEntity = payloadJson
                        .getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

                String orderId = paymentEntity.getString("order_id");
                System.out.println("Processing Failure for Order: " + orderId);
                String paymentId = paymentEntity.getString("id");
                String paymentMethod = paymentEntity.getString("method");
                // TODO: Update your database order status to "FAILED"
                UpdateDbOrder(orderId,paymentId,PaymentStatus.FAILED,paymentMethod,OrderStatus.PAYMENT_FAILED,"Payment Failed");
            }

            // Razorpay expects a 200 OK response, otherwise it will keep retrying the webhook
            return ResponseEntity.ok("Webhook Processed successfully");

        } catch (Exception e) {
            System.err.println("Webhook Processing Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    private void UpdateDbOrder(String orderId,String paymentId,PaymentStatus paymentStatus, String paymentMethod,OrderStatus orderStatus,String title) throws Exception {
        Optional<Order> dbOrder=orderRepository.findByOrderId(orderId);
        if(dbOrder.isEmpty()){
            throw new OrderNotFoundException("Order Not Found");
        }
        List<Payment> payments = dbOrder.get().getPayments();
        dbOrder.get().setPaymentStatus(paymentStatus);
        dbOrder.get().setStatus(orderStatus);
        for (Payment payment : payments) {
            payment.setGatewayTransactionId(paymentId);
            payment.setPaymentMethod(PaymentMethod.fromString(paymentMethod));
            payment.setStatus(paymentStatus);
        }
        // ==========================================
        // 📌 Update History Status
        // ==========================================
        OrderHistory newOrderHistory=new OrderHistory();
        newOrderHistory.setStatus(orderStatus);
        newOrderHistory.setChangedBy("WEBHOOK");
        newOrderHistory.setTitle(title);
//        newOrderHistory.setChangedByUserId(user.getId());
        newOrderHistory.setOrder(dbOrder.get());
        newOrderHistory.setCreated_at(new Date());
        newOrderHistory.setUpdated_at(new Date());

        orderRepository.save(dbOrder.get());
        orderHistoryRepository.save(newOrderHistory);
    }
}
