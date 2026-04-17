package com.precisioncast.erp.salesquotation.service;

import com.precisioncast.erp.salesquotation.dto.SalesQuotationItemRequestDto;
import com.precisioncast.erp.salesquotation.dto.SalesQuotationItemResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface SalesQuotationItemService {

    SalesQuotationItemResponseDto addQuotationItem(Long quotationId, Long productId, BigDecimal qty);

    List<SalesQuotationItemResponseDto> addBulkQuotationItems(Long quotationId, List<SalesQuotationItemRequestDto> items);

    List<SalesQuotationItemResponseDto> getQuotationItems(Long quotationId);

    SalesQuotationItemResponseDto updateQuotationItemQty(Long quotationItemId, BigDecimal qty);

    void deleteQuotationItem(Long quotationItemId);
}