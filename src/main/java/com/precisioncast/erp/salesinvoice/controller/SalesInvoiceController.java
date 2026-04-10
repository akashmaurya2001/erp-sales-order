package com.precisioncast.erp.salesinvoice.controller;

import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceRequestDto;
import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceResponseDto;
import com.precisioncast.erp.salesinvoice.service.SalesInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-invoices")
@RequiredArgsConstructor
public class SalesInvoiceController {

    private final SalesInvoiceService salesInvoiceService;

    @PostMapping
    public ResponseEntity<SalesInvoiceResponseDto> createSalesInvoice(
            @Valid @RequestBody SalesInvoiceRequestDto requestDto){
        SalesInvoiceResponseDto response = salesInvoiceService.createSalesInvoice(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SalesInvoiceResponseDto>> getAllSalesInvoices(){
        List<SalesInvoiceResponseDto> response = salesInvoiceService.getAllSalesInvoices();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesInvoiceResponseDto> getSalesInvoiceById(@PathVariable Long id){
        SalesInvoiceResponseDto response = salesInvoiceService.getSalesInvoiceById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSalesInvoice(@PathVariable Long id){
        salesInvoiceService.deleteSalesInvoice(id);
        return ResponseEntity.ok("Sales Invoice deleted successfully with id: " + id);
    }
}
