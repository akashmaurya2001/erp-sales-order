package com.precisioncast.erp.receipt.repository;

import com.precisioncast.erp.receipt.entity.ArReceiptAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArReceiptAllocationRepository extends JpaRepository<ArReceiptAllocation, Long> {
}
