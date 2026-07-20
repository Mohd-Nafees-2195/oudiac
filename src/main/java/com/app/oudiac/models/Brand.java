package com.app.oudiac.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Brand extends BaseModel {

    private String name;
    @Column(unique = true)
    private String code;
}
