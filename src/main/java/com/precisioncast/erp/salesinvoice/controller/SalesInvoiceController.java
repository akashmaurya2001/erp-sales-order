package com.precisioncast.erp.salesinvoice.controller;

import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceRequestDto;
import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceResponseDto;
import com.precisioncast.erp.salesinvoice.service.SalesInvoiceService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/sales-invoices")
@RequiredArgsConstructor
public class SalesInvoiceController {

    private final SalesInvoiceService salesInvoiceService;

    @PostMapping
    public ResponseEntity<SalesInvoiceResponseDto> createSalesInvoice(
            @Valid @RequestBody SalesInvoiceRequestDto requestDto) {
        return ResponseEntity.ok(salesInvoiceService.createSalesInvoice(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<SalesInvoiceResponseDto>> getAllSalesInvoices() {
        return ResponseEntity.ok(salesInvoiceService.getAllSalesInvoices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesInvoiceResponseDto> getSalesInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(salesInvoiceService.getSalesInvoiceById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSalesInvoice(@PathVariable Long id) {
        salesInvoiceService.deleteSalesInvoice(id);
        return ResponseEntity.ok("Sales invoice deleted successfully");
    }
}