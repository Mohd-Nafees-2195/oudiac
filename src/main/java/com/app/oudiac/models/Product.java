package com.app.oudiac.models;

import com.app.oudiac.models.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Product extends BaseModel{
    private String name;
    private String description;
    private String imageUrl;

    @Column(unique=true,nullable=false)
    private String code;

    @ManyToOne
    private Brand brand;

    @ManyToOne
    private Category category;

    @ManyToOne
    private ProductType productType;

    private String fragranceFamily;

    @Enumerated(value = EnumType.STRING)
    private ProductStatus productStatus;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<ProductVariant> productVariants;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "store_product",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "store_id")
    )
    List<Store> stores;
}
