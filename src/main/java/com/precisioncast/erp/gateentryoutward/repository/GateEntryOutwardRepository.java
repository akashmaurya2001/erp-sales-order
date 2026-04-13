package com.precisioncast.erp.gateentryoutward.repository;

import com.precisioncast.erp.gateentryoutward.entity.GateEntryOutward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GateEntryOutwardRepository extends JpaRepository<GateEntryOutward, Long> {
}
