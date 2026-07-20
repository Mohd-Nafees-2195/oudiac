package com.app.oudiac.repositories;

import com.app.oudiac.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    void deleteByCode(String code);

    Optional<Category> findByCode(String code);
}
