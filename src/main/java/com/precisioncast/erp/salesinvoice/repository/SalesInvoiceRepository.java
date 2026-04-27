package com.precisioncast.erp.salesinvoice.repository;

import com.precisioncast.erp.salesinvoice.entity.SalesInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesInvoiceRepository extends JpaRepository<SalesInvoice,Long> {

    List<SalesInvoice> findBySalesOrderId(Long salesOrderId);

    List<SalesInvoice> findByStatusIgnoreCase(String status);
}
