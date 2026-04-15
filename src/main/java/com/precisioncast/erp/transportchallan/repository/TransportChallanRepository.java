package com.precisioncast.erp.transportchallan.repository;

import com.precisioncast.erp.transportchallan.entity.TransportChallan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportChallanRepository extends JpaRepository<TransportChallan, Long> {
}
