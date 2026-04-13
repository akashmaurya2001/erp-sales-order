package com.precisioncast.erp.gateentryoutward.repository;

import com.precisioncast.erp.gateentryoutward.entity.GateEntryOutwardItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GateEntryOutwardItemRepository extends JpaRepository<GateEntryOutwardItem, Long> {

    List<GateEntryOutwardItem> findByOutwardId(Long outwardId);
}
