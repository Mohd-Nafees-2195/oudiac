package com.app.oudiac.models;


import com.app.oudiac.models.enums.PaymentGateway;
import com.app.oudiac.models.enums.PaymentMethod;
import com.app.oudiac.models.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseModel{

    // A payment belongs to one specific order.
    // FetchType.LAZY ensures we don't load the whole order unless we specifically ask for it.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // A payment belongs to the user who made it.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The ID provided by Stripe or Razorpay (e.g., "pay_Mg3b...")
    // This is nullable because it might be empty if the payment is COD or hasn't hit the gateway yet.
    @Column(name = "gateway_transaction_id")
    private String gatewayTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_gateway", nullable = false)
    private PaymentGateway paymentGateway;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    // ALWAYS use BigDecimal for money to prevent rounding errors!
    // precision = 10, scale = 2 means up to 99,999,999.99
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    // e.g., "INR" or "USD"
    @Column(nullable = false, length = 3)
    private String currency = "INR";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    // If the payment fails, store the reason so you can help the customer
    @Column(name = "error_message", length = 500)
    private String errorMessage;
}