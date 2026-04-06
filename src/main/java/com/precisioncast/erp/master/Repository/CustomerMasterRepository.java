package com.precisioncast.erp.master.Repository;

import com.precisioncast.erp.master.entity.CustomerMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMasterRepository extends JpaRepository<CustomerMaster, Long> {
}
