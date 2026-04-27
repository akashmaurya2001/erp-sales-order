package com.precisioncast.erp.salesinvoice.repository;

import com.precisioncast.erp.salesinvoice.entity.InvoiceItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItems, Long> {

    List<InvoiceItems> findByInvoiceId(Long invoiceId);

    List<InvoiceItems> findByProductId(Long productId);

}
