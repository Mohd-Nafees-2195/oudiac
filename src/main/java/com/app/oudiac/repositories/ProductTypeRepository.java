package com.app.oudiac.repositories;

import com.app.oudiac.models.ProductType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductTypeRepository extends JpaRepository<ProductType,Long> {

    Optional<ProductType> findByCode(@NotBlank String code);
    void deleteByCode(String code);
}
