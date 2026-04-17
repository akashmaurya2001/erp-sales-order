package com.precisioncast.erp.salesquotation.repository;

import com.precisioncast.erp.salesquotation.entity.SalesQuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesQuotationItemRepository extends JpaRepository<SalesQuotationItem, Long> {

    List<SalesQuotationItem> findBySalesQuotation_QuotationId(Long quotationId);
}