package com.precisioncast.erp.salesquotation.service;

import com.precisioncast.erp.salesquotation.dto.SalesQuotationRequestDto;
import com.precisioncast.erp.salesquotation.dto.SalesQuotationResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface SalesQuotationService {

    SalesQuotationResponseDto createQuotation(SalesQuotationRequestDto requestDto);

    List<SalesQuotationResponseDto> getAllQuotations();

    SalesQuotationResponseDto getQuotationById(Long quotationId);

    List<SalesQuotationResponseDto> searchQuotations(
            Long quotationId, Long customerId, Long itemId, String status, LocalDate quotationDate, LocalDate fromDate, LocalDate toDate);

    SalesQuotationResponseDto updateQuotation(Long quotationId, SalesQuotationRequestDto requestDto);

    SalesQuotationResponseDto approveQuotation(Long quotationId);

    SalesQuotationResponseDto rejectQuotation(Long quotationId);

    SalesQuotationResponseDto activateQuotation(Long quotationId);

    SalesQuotationResponseDto deactivateQuotation(Long quotationId);

    void deleteQuotation(Long quotationId);

    byte[] exportQuotation(Long quotationId);
}