package com.precisioncast.erp.transportchallan.repository;

import com.precisioncast.erp.transportchallan.entity.TransportChallan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransportChallanRepository extends JpaRepository<TransportChallan, Long> {

    List<TransportChallan> findByVendorContainingIgnoreCase(String vendor);

    List<TransportChallan> findByVehicleId(Long vehicleId);

    List<TransportChallan> findByDriverId(Long driverId);

    List<TransportChallan> findByStatusIgnoreCase(String status);

    List<TransportChallan> findByChallanDateBetween(LocalDate from, LocalDate to);
}
