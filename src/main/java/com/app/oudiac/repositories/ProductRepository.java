package com.app.oudiac.repositories;

import com.app.oudiac.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Override
    Page<Product> findAll(Pageable pageable);

    Optional<Product> findByCode(String code);

//    Optional<Product> findByCode(String code);
}
