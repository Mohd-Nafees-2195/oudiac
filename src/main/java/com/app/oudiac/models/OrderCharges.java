package com.app.oudiac.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_charges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCharges extends BaseModel{

        @Column(updatable = false) //Dont update for history, just mark isDeleted as false and always fetch isDeleted=true
        private BigDecimal gst;

        @Column(updatable = false)
        private BigDecimal shippingFee;

        @Column(updatable = false)
        private BigDecimal freeShippingThreshold; //If amount exceeded this threshold then shipping fee is 0
}
