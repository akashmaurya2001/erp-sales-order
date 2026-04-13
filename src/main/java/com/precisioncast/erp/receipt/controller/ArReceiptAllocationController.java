package com.precisioncast.erp.receipt.controller;

import com.precisioncast.erp.receipt.dto.ArReceiptAllocationRequestDto;
import com.precisioncast.erp.receipt.dto.ArReceiptAllocationResponseDto;
import com.precisioncast.erp.receipt.service.ArReceiptAllocationService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/receipt-allocations")
@RequiredArgsConstructor
public class ArReceiptAllocationController {

    private final ArReceiptAllocationService allocationService;

    @PostMapping
    public ResponseEntity<ArReceiptAllocationResponseDto> createAllocation(
            @Valid @RequestBody ArReceiptAllocationRequestDto requestDto) {
        return ResponseEntity.ok(allocationService.createAllocation(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<ArReceiptAllocationResponseDto>> getAllAllocations() {
        return ResponseEntity.ok(allocationService.getAllAllocations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArReceiptAllocationResponseDto> getAllocationById(@PathVariable Long id) {
        return ResponseEntity.ok(allocationService.getAllocationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAllocation(@PathVariable Long id) {
        allocationService.deleteAllocation(id);
        return ResponseEntity.ok("Receipt allocation deleted successfully");
    }
}