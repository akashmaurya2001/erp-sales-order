package com.precisioncast.erp.salesquotation.repository;

import com.precisioncast.erp.salesquotation.entity.SalesQuotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesQuotationRepository extends JpaRepository<SalesQuotation,Long> {
}
