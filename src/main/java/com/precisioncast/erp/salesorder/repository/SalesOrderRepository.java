package com.precisioncast.erp.salesorder.repository;

import com.precisioncast.erp.salesorder.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    boolean existsByOrderNumber(String orderNumber);

    Optional<SalesOrder> findByOrderNumber(String orderNumber);
}
