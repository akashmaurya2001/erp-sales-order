package com.precisioncast.erp.master.repository;

import com.precisioncast.erp.master.entity.DriverMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverMasterRepository extends JpaRepository<DriverMaster, Long> {

    Optional<DriverMaster> findByLicenseNumber(String licenseNumber);
}
