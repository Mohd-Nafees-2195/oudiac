package com.app.oudiac.models;

import com.app.oudiac.models.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
public class ProductVariant extends BaseModel {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    private String variantType; //100ml , 50ml,

    private BigDecimal sellingPrice;
    private BigDecimal MRP;
    private Long stock;

    @Column(unique = true, nullable = false)
    private String sku;  //Stock keeping unit

}
