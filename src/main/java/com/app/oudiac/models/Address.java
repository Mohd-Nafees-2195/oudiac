package com.app.oudiac.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Address extends BaseModel{
    @ManyToOne
    private User user;

    private String fullName;
    private String shippingAddress;
    private String city;
    private String state;
    private String pinCode;
    private String country;
    private String phoneNumber;
}
