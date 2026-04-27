package com.precisioncast.erp.salesreturn.controller;

import com.precisioncast.erp.salesreturn.dto.SalesReturnResponseDto;
import com.precisioncast.erp.salesreturn.service.SalesReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salesReturn")
@RequiredArgsConstructor
public class SalesReturnController {

    private final SalesReturnService service;

    @PostMapping("/create/{invoiceId}/{customerId}")
    public ResponseEntity<SalesReturnResponseDto> create(
            @PathVariable Long invoiceId,
            @PathVariable Long customerId) {

        return ResponseEntity.ok(service.createSalesReturn(invoiceId, customerId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<SalesReturnResponseDto>> list() {
        return ResponseEntity.ok(service.getAllSalesReturns());
    }

    @GetMapping("/{salesReturnId}")
    public ResponseEntity<SalesReturnResponseDto> get(
            @PathVariable Long salesReturnId) {

        return ResponseEntity.ok(service.getSalesReturnById(salesReturnId));
    }

    @PatchMapping("/approve/{salesReturnId}")
    public ResponseEntity<SalesReturnResponseDto> approve(
            @PathVariable Long salesReturnId) {

        return ResponseEntity.ok(service.approveSalesReturn(salesReturnId));
    }

    @PatchMapping("/reject/{salesReturnId}")
    public ResponseEntity<SalesReturnResponseDto> reject(
            @PathVariable Long salesReturnId) {

        return ResponseEntity.ok(service.rejectSalesReturn(salesReturnId));
    }

    @DeleteMapping("/delete/{salesReturnId}")
    public ResponseEntity<String> delete(
            @PathVariable Long salesReturnId) {

        service.deleteSalesReturn(salesReturnId);
        return ResponseEntity.ok("Sales return deleted successfully");
    }
}