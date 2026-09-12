package com.app.oudiac.models;

import com.app.oudiac.models.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "order_history",
        indexes = {
                @Index(name = "idx_order_history_order_id", columnList = "order_id"),
                @Index(name = "idx_order_history_created_at", columnList = "created_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderHistory extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Order whose status changed
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * Status before the change
     */
//    @Enumerated(EnumType.STRING)
//    @Column(name = "previous_status", length = 50)
//    private OrderStatus previousStatus;

    /**
     * New/current status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private OrderStatus status;

    /**
     * Optional reason for the status change.
     *
     * Example:
     * "Customer requested cancellation"
     * "Payment failed"
     * "Customer unavailable"
     */
    @Column(name = "reason", length = 500)
    private String reason;

    /**
     * Who/what changed the status.
     *
     * Example:
     * CUSTOMER
     * ADMIN
     * SYSTEM
     * DELIVERY_PARTNER
     */
    @Column(name = "changed_by", length = 50)
    private String changedBy;

    /**
     * Optional user/admin ID responsible for the change.
     */
    @Column(name = "changed_by_user_id")
    private Long changedByUserId;

    /**
     * Additional information.
     */
    @Column(name = "notes", length = 1000)
    private String notes;

    private String title;

}