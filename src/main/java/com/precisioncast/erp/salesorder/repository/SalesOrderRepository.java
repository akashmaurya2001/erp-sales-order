package com.precisioncast.erp.salesorder.repository;

import com.precisioncast.erp.salesorder.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

}