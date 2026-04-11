package com.precisioncast.erp.receipt.service;

import com.precisioncast.erp.receipt.dto.ArReceiptRequestDto;
import com.precisioncast.erp.receipt.dto.ArReceiptResponseDto;

import java.util.List;

public interface ArReceiptService {

    ArReceiptResponseDto createReceipt(ArReceiptRequestDto requestDto);

    List<ArReceiptResponseDto> getAllReceipts();

    ArReceiptResponseDto getReceiptById(Long receiptId);

    void deleteReceiptById(Long receiptId);

}
