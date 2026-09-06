package com.app.oudiac.repositories;

import com.app.oudiac.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByIdempotencyKey(String idempotencyKey);
    Optional<Order> findByOrderNumber(String orderNumber);

    Optional<Order> findByOrderId(String orderId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.updated_at DESC")
    Page<Order> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT o FROM Order o ORDER BY o.updated_at DESC")
    Page<Order> findAll(Pageable pageable);

//    @Query("""
//    SELECT o
//    FROM Order o
//    WHERE o.created_at = :today
//    ORDER BY o.created_at DESC
//""")
//    Page<Order> findCurrDayOrders(
//            @Param("today") Date today,
//            Pageable pageable
//    );

    @Query("""
    SELECT o
    FROM Order o
    WHERE o.created_at >= :startOfDay
      AND o.created_at < :startOfNextDay
    ORDER BY o.created_at DESC
""")
    Page<Order> findCurrDayOrders(
            @Param("startOfDay") Date startOfDay,
            @Param("startOfNextDay") Date startOfNextDay,
            Pageable pageable
    );

}
