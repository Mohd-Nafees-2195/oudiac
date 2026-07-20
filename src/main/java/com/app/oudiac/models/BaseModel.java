package com.app.oudiac.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Hibernate will automatically set this when the row is created
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date created_at;

    // Hibernate will automatically update this whenever the row changes
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updated_at;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false; // Defaults to false for new records
}
