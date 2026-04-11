package com.precisioncast.erp.receipt.repository;

import com.precisioncast.erp.receipt.entity.ArReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArReceiptRepository extends JpaRepository<ArReceipt, Long> {
}
