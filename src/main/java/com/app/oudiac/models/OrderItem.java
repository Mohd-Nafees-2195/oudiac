package com.app.oudiac.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // ==========================================
    // 1. THE REORDER LINK (Live Database Link)
    // ==========================================
    // We keep this relationship so the frontend knows EXACTLY which item this was.
    // If they click "Reorder", you use this to fetch the live inventory and current price.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;


    // ==========================================
    // 2. THE SNAPSHOT (Frozen in Time for Receipts)
    // ==========================================
    // These fields never change once the order is placed.
    // When showing the user their past receipt, you ONLY read these fields.
    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String variantType; // e.g., "50ml"

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int quantity;

    private String url;
}