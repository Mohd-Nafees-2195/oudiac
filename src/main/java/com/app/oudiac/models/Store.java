package com.app.oudiac.models;

import com.app.oudiac.models.enums.StoreStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Store extends BaseModel{

    private String name;
    private String email;

    private String address;

    private String city;
    private String pincode;

    private Double latitude;

    private Double longitude;

    @ManyToOne
    private Admin manager;

    private String phone;
    @Enumerated(value = EnumType.STRING)
    private StoreStatus storeStatus;
    private String storeSku;
    private String storeImageUrl;

    @ManyToMany(mappedBy = "stores")
    List<ProductVariant> products;
//    ordersToday: 845,
//    revenueToday: "₹1.2L",
}
