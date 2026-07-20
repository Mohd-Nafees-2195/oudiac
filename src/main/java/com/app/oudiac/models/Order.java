package com.app.oudiac.models;

import com.app.oudiac.models.enums.OrderStatus;
import com.app.oudiac.models.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseModel {

    @Column(nullable = false, unique = true, updatable = false)
    private String orderNumber;  //System generated unique orderNumber

    @Column( unique = true, updatable = false)
    private String orderId;  //Razor Pay OrderId

    @Column(unique = true, nullable = false, updatable = false)
    private String idempotencyKey;

    // 🔗 User who placed the order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 📦 Order Items mapped to this order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    // 💳 Payment attempts linked to this order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Payment> payments;

    // ==========================================
    // 💰 FINANCIAL SNAPSHOT (Never calculate on the fly!)
    // ==========================================
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subTotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal shippingFee;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // ==========================================
    // 🚚 SHIPPING SNAPSHOT (Do NOT use @ManyToOne here!)
    // ==========================================
    @Column(nullable = false)
    private String shippingName;

    @Column(nullable = false)
    private String shippingPhone;

    @Column(nullable = false)
    private String shippingAddress;

    @Column(nullable = false)
    private String shippingCity;

    @Column(nullable = false)
    private String shippingPincode;

    // ==========================================
    // 📌 LIFECYCLE & TRACKING
    // ==========================================
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "courier_partner")
    private String courierPartner; // e.g., "Delhivery"

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;
}