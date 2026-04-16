package com.precisioncast.erp.deliveryschedule.repository;

import com.precisioncast.erp.deliveryschedule.entity.SalesOrderDeliverySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderDeliveryScheduleRepository extends JpaRepository<SalesOrderDeliverySchedule,Long> {

    List<SalesOrderDeliverySchedule> findBySalesOrderId(Long salesOrderId);
}
