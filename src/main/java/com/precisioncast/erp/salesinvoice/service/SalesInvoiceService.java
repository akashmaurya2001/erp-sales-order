package com.precisioncast.erp.salesinvoice.service;

import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceRequestDto;
import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceResponseDto;

import java.util.List;

public interface SalesInvoiceService {

    SalesInvoiceResponseDto createSalesInvoice(SalesInvoiceRequestDto requestDto);

    List<SalesInvoiceResponseDto> getAllSalesInvoices();

    SalesInvoiceResponseDto getSalesInvoiceById(Long invoiceId);

    void deleteSalesInvoice(Long invoiceId);
}
