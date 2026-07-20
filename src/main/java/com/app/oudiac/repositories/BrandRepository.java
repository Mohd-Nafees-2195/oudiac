package com.app.oudiac.repositories;

import com.app.oudiac.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Long> {
    Optional<Brand> findByCode(String code);
    void deleteByCode(String code);
}
