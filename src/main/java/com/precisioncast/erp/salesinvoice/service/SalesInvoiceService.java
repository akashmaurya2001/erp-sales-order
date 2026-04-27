package com.precisioncast.erp.salesinvoice.service;

import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceRequestDto;
import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface SalesInvoiceService {

    SalesInvoiceResponseDto createInvoice(Long salesOrderId, Long deliveryId, SalesInvoiceRequestDto requestDto);

    SalesInvoiceResponseDto createManualInvoice(SalesInvoiceRequestDto requestDto);

    List<SalesInvoiceResponseDto> getAllInvoices();

    SalesInvoiceResponseDto getInvoiceById(Long invoiceId);

    List<SalesInvoiceResponseDto> searchInvoices(Long customerId, LocalDate from, LocalDate to, String status);

    SalesInvoiceResponseDto updateInvoice(Long invoiceId, SalesInvoiceRequestDto requestDto);

    SalesInvoiceResponseDto postInvoice(Long invoiceId);

    SalesInvoiceResponseDto cancelInvoice(Long invoiceId);

    SalesInvoiceResponseDto markPaid(Long invoiceId);

    void deleteInvoice(Long invoiceId);

    byte[] exportInvoice(Long invoiceId);
}
