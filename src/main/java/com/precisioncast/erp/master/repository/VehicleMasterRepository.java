package com.precisioncast.erp.master.repository;

import com.precisioncast.erp.master.entity.VehicleMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleMasterRepository extends JpaRepository<VehicleMaster, Long> {

    Optional<VehicleMaster> findByVehicleNumber(String vehicleNumber);
}
