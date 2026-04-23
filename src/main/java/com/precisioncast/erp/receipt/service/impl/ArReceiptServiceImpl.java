package com.precisioncast.erp.receipt.service.impl;

import com.precisioncast.erp.master.repository.CustomerMasterRepository;
import com.precisioncast.erp.receipt.dto.ArReceiptRequestDto;
import com.precisioncast.erp.receipt.dto.ArReceiptResponseDto;
import com.precisioncast.erp.receipt.entity.ArReceipt;
import com.precisioncast.erp.receipt.repository.ArReceiptRepository;
import com.precisioncast.erp.receipt.service.ArReceiptService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ArReceiptServiceImpl implements ArReceiptService {

    private final ArReceiptRepository arReceiptRepository;
    private final CustomerMasterRepository customerMasterRepository;

    @Override
    public ArReceiptResponseDto createReceipt(ArReceiptRequestDto requestDto) {

        customerMasterRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with id: " + requestDto.getCustomerId()
                ));

        ArReceipt receipt = new ArReceipt();
        receipt.setReceiptNo(requestDto.getReceiptNo());
        receipt.setReceiptDate(requestDto.getReceiptDate());
        receipt.setCustomerId(requestDto.getCustomerId());
        receipt.setReceiptAmount(requestDto.getReceiptAmount());
        receipt.setPaymentMode(requestDto.getPaymentMode());
        receipt.setReferenceNo(requestDto.getReferenceNo());

        ArReceipt saved = arReceiptRepository.save(receipt);
        return mapToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArReceiptResponseDto> getAllReceipts() {
        List<ArReceipt> receipts = arReceiptRepository.findAll();
        List<ArReceiptResponseDto> responseDto = new ArrayList<>();

        for (ArReceipt receipt : receipts) {
            responseDto.add(mapToResponseDto(receipt));
        }

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ArReceiptResponseDto getReceiptById(Long receiptId) {
        ArReceipt receipt = arReceiptRepository.findById(receiptId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Receipt not found with id: " + receiptId
                ));

        return mapToResponseDto(receipt);
    }

    @Override
    public void deleteReceiptById(Long receiptId) {
        ArReceipt receipt = arReceiptRepository.findById(receiptId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Receipt not found with id: " + receiptId
                ));

        arReceiptRepository.delete(receipt);
    }

    private ArReceiptResponseDto mapToResponseDto(ArReceipt receipt) {
        return ArReceiptResponseDto.builder()
                .arReceiptId(receipt.getArReceiptId())
                .receiptNo(receipt.getReceiptNo())
                .receiptDate(receipt.getReceiptDate())
                .customerId(receipt.getCustomerId())
                .receiptAmount(receipt.getReceiptAmount())
                .paymentMode(receipt.getPaymentMode())
                .referenceNo(receipt.getReferenceNo())
                .createdAt(receipt.getCreatedAt())
                .build();
    }
}