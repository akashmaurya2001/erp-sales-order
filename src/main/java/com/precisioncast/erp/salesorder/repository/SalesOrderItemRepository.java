package com.precisioncast.erp.salesorder.repository;

import com.precisioncast.erp.salesorder.entity.SalesOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderItemRepository extends JpaRepository<SalesOrderItem, Long> {

    List<SalesOrderItem> findBySalesOrder_SalesOrderId(Long salesOrderId);
}
