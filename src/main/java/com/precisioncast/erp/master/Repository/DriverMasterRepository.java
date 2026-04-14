package com.precisioncast.erp.master.Repository;

import com.precisioncast.erp.master.entity.DriverMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverMasterRepository extends JpaRepository<DriverMaster, Long> {
}
