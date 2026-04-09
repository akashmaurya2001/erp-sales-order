package com.precisioncast.erp.deliverychallan.repository;

import com.precisioncast.erp.deliverychallan.entity.DeliveryChallan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryChallanRepository extends JpaRepository<DeliveryChallan, Long> {

    boolean existsByDispatchId(Long dispatchId);
}
