package com.app.oudiac.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_types")
@Getter
@Setter
public class ProductType extends BaseModel{

    private String name; //Attar ,Perfume
    @Column(name = "code",unique = true)
    private String code;
}
