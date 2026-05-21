package com.precisioncast.erp.salesproductionplanmap.repository;

import com.precisioncast.erp.salesproductionplanmap.entity.SalesProductionPlanMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesProductionPlanMapRepository extends JpaRepository<SalesProductionPlanMap, Long> {

    List<SalesProductionPlanMap> findBySalesOrderId(Long salesOrderId);

    List<SalesProductionPlanMap> findByPlanId(Long PlanId);

    boolean existsBySalesOrderIdAndPlanId(Long salesOrderId, Long planId);

}
