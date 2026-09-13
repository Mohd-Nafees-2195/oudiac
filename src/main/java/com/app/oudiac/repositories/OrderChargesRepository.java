package com.app.oudiac.repositories;

import com.app.oudiac.models.OrderCharges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;

@Repository
public interface OrderChargesRepository extends JpaRepository<OrderCharges,Long> {
    @Query("SELECT c FROM OrderCharges c WHERE c.isDeleted = :isDeleted")
    OrderCharges getCharges(@Param("isDeleted") Boolean isDeleted);
}
