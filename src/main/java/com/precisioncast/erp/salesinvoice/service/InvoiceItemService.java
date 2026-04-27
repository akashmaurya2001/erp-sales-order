package com.precisioncast.erp.salesinvoice.service;

import com.precisioncast.erp.salesinvoice.dto.InvoiceItemBulkRequestDto;
import com.precisioncast.erp.salesinvoice.dto.InvoiceItemResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceItemService {

    InvoiceItemResponseDto addItem(Long invoiceId, Long itemId, BigDecimal qty, BigDecimal price);

    List<InvoiceItemResponseDto> addBulkItems(Long invoiceId, InvoiceItemBulkRequestDto requestDto);

    List<InvoiceItemResponseDto> getItemsByInvoice(Long invoiceId);

    InvoiceItemResponseDto updateQty(Long invoiceItemId, BigDecimal qty);

    InvoiceItemResponseDto updatePrice(Long invoiceItemId, BigDecimal price);

    void deleteItem(Long invoiceItemId);
}