package com.precisioncast.erp.receipt.service;

import com.precisioncast.erp.receipt.dto.ArReceiptAllocationRequestDto;
import com.precisioncast.erp.receipt.dto.ArReceiptAllocationResponseDto;

import java.util.List;

public interface ArReceiptAllocationService {

    ArReceiptAllocationResponseDto createAllocation(ArReceiptAllocationRequestDto requestDto);

    List<ArReceiptAllocationResponseDto> getAllAllocations();

    ArReceiptAllocationResponseDto getAllocationById(Long allocationId);

    void deleteAllocation(Long allocationId);
}
