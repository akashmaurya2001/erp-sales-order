package com.precisioncast.erp.gateentryoutward.repository;

import com.precisioncast.erp.gateentryoutward.entity.GateEntryOutward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GateEntryOutwardRepository extends JpaRepository<GateEntryOutward, Long> {

    List<GateEntryOutward> findByStatusIgnoreCase(String status);

    List<GateEntryOutward> findByVehicleId(Long vehicleId);

    List<GateEntryOutward> findByDriverId(Long driverId);

    List<GateEntryOutward> findByCustomerId(Long customerId);
}
