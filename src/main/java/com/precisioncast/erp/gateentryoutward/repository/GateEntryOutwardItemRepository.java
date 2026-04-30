package com.precisioncast.erp.gateentryoutward.repository;

import com.precisioncast.erp.gateentryoutward.entity.GateEntryOutwardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GateEntryOutwardItemRepository extends JpaRepository<GateEntryOutwardItem, Long> {

    List<GateEntryOutwardItem> findByOutwardId(Long outwardId);
}
