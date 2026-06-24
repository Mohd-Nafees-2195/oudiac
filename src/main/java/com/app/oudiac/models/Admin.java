package com.app.oudiac.models;

import com.app.oudiac.models.enums.EmailStatus;
import com.app.oudiac.models.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admin_tbl")
@Getter
@Setter
public class Admin extends BaseModel{
    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobileNumber;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private EmailStatus emailStatus;

    @Enumerated(value = EnumType.STRING)
    private Role role;
}
