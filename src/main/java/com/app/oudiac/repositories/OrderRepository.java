package com.app.oudiac.repositories;

import com.app.oudiac.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByIdempotencyKey(String idempotencyKey);
    Optional<Order> findByOrderNumber(String orderNumber);

    Optional<Order> findByOrderId(String orderId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.updated_at DESC")
    Page<Order> findByUserId(@Param("userId") Long userId, Pageable pageable);
}
