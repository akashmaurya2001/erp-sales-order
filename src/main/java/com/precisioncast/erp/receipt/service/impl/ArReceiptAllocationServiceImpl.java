package com.precisioncast.erp.receipt.service.impl;

import com.precisioncast.erp.receipt.dto.ArReceiptAllocationRequestDto;
import com.precisioncast.erp.receipt.dto.ArReceiptAllocationResponseDto;
import com.precisioncast.erp.receipt.entity.ArReceipt;
import com.precisioncast.erp.receipt.entity.ArReceiptAllocation;
import com.precisioncast.erp.receipt.repository.ArReceiptAllocationRepository;
import com.precisioncast.erp.receipt.repository.ArReceiptRepository;
import com.precisioncast.erp.receipt.service.ArReceiptAllocationService;
import com.precisioncast.erp.salesinvoice.entity.SalesInvoice;
import com.precisioncast.erp.salesinvoice.repository.SalesInvoiceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ArReceiptAllocationServiceImpl implements ArReceiptAllocationService {

    private final ArReceiptAllocationRepository allocationRepository;
    private final ArReceiptRepository arReceiptRepository;
    private final SalesInvoiceRepository salesInvoiceRepository;

    @Override
    public ArReceiptAllocationResponseDto createAllocation(ArReceiptAllocationRequestDto requestDto) {

        ArReceipt receipt = arReceiptRepository.findById(requestDto.getArReceiptId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Receipt not found with id: " + requestDto.getArReceiptId()
                ));

        SalesInvoice invoice = salesInvoiceRepository.findById(requestDto.getArInvoiceId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Invoice not found with id: " + requestDto.getArInvoiceId()
                ));

        ArReceiptAllocation allocation = new ArReceiptAllocation();
        allocation.setArReceiptId(receipt.getArReceiptId());
        allocation.setArInvoiceId(invoice.getInvoiceId());
        allocation.setAllocatedAmount(requestDto.getAllocatedAmount());

        ArReceiptAllocation saved = allocationRepository.save(allocation);
        return mapToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArReceiptAllocationResponseDto> getAllAllocations() {
        List<ArReceiptAllocation> allocations = allocationRepository.findAll();
        List<ArReceiptAllocationResponseDto> responseDto = new ArrayList<>();

        for (ArReceiptAllocation allocation : allocations) {
            responseDto.add(mapToResponseDto(allocation));
        }

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ArReceiptAllocationResponseDto getAllocationById(Long allocationId) {
        ArReceiptAllocation allocation = allocationRepository.findById(allocationId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Allocation not found with id: " + allocationId
                ));

        return mapToResponseDto(allocation);
    }

    @Override
    public void deleteAllocation(Long allocationId) {
        ArReceiptAllocation allocation = allocationRepository.findById(allocationId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Allocation not found with id: " + allocationId
                ));

        allocationRepository.delete(allocation);
    }

    private ArReceiptAllocationResponseDto mapToResponseDto(ArReceiptAllocation allocation) {
        return ArReceiptAllocationResponseDto.builder()
                .receiptAllocationId(allocation.getReceiptAllocationId())
                .arReceiptId(allocation.getArReceiptId())
                .arInvoiceId(allocation.getArInvoiceId())
                .allocatedAmount(allocation.getAllocatedAmount())
                .allocatedAt(allocation.getAllocatedAt())
                .build();
    }
}