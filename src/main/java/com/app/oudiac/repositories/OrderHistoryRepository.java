package com.app.oudiac.repositories;

import com.app.oudiac.models.Order;
import com.app.oudiac.models.OrderHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory,Long> {

    List<OrderHistory> findByOrderId(Long orderId);
//    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.updated_at DESC")
//    Page<Order> findByUserId(@Param("userId") Long userId, Pageable pageable);

}
