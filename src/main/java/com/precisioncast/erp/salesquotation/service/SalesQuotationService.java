package com.precisioncast.erp.salesquotation.service;

import com.precisioncast.erp.salesquotation.dto.SalesQuotationRequestDto;
import com.precisioncast.erp.salesquotation.dto.SalesQuotationResponseDto;

import java.util.List;

public interface SalesQuotationService {

    SalesQuotationResponseDto createQuotation(SalesQuotationRequestDto requestDto);

    List<SalesQuotationResponseDto> getAllQuotations();

    SalesQuotationResponseDto getQuotationById(Long quotationId);

    List<SalesQuotationResponseDto> searchQuotations(Long customerId, Long itemId, String status);

    SalesQuotationResponseDto updateQuotation(Long quotationId, SalesQuotationRequestDto requestDto);

    SalesQuotationResponseDto approveQuotation(Long quotationId);

    SalesQuotationResponseDto rejectQuotation(Long quotationId);

    SalesQuotationResponseDto activateQuotation(Long quotationId);

    SalesQuotationResponseDto deactivateQuotation(Long quotationId);

    void deleteQuotation(Long quotationId);

    byte[] exportQuotation(Long quotationId);
}