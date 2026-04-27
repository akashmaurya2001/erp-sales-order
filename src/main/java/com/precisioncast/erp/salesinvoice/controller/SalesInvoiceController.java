package com.precisioncast.erp.salesinvoice.controller;

import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceRequestDto;
import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceResponseDto;
import com.precisioncast.erp.salesinvoice.service.SalesInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class SalesInvoiceController {

    private final SalesInvoiceService service;

    @PostMapping("/create/{salesOrderId}/{deliveryId}")
    public ResponseEntity<SalesInvoiceResponseDto> create(
            @PathVariable Long salesOrderId,
            @PathVariable Long deliveryId,
            @RequestBody SalesInvoiceRequestDto dto) {
        return ResponseEntity.ok(service.createInvoice(salesOrderId, deliveryId, dto));
    }

    @PostMapping("/createManual")
    public ResponseEntity<SalesInvoiceResponseDto> createManual(
            @RequestBody SalesInvoiceRequestDto dto) {
        return ResponseEntity.ok(service.createManualInvoice(dto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<SalesInvoiceResponseDto>> list() {
        return ResponseEntity.ok(service.getAllInvoices());
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<SalesInvoiceResponseDto> get(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(service.getInvoiceById(invoiceId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SalesInvoiceResponseDto>> search(
            @RequestParam(required = false) Long customer,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.searchInvoices(customer,from,to,status));
    }

    @PutMapping("/update/{invoiceId}")
    public ResponseEntity<SalesInvoiceResponseDto> update(
            @PathVariable Long invoiceId,
            @RequestBody SalesInvoiceRequestDto dto) {
        return ResponseEntity.ok(service.updateInvoice(invoiceId, dto));
    }

    @PatchMapping("/post/{invoiceId}")
    public ResponseEntity<SalesInvoiceResponseDto> post(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(service.postInvoice(invoiceId));
    }

    @PatchMapping("/cancel/{invoiceId}")
    public ResponseEntity<SalesInvoiceResponseDto> cancel(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(service.cancelInvoice(invoiceId));
    }

    @PatchMapping("/markPaid/{invoiceId}")
    public ResponseEntity<SalesInvoiceResponseDto> markPaid(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(service.markPaid(invoiceId));
    }

    @DeleteMapping("/delete/{invoiceId}")
    public ResponseEntity<String> delete(@PathVariable Long invoiceId) {
        service.deleteInvoice(invoiceId);
        return ResponseEntity.ok("Invoice deleted successfully");
    }

    @GetMapping("/export/{invoiceId}")
    public ResponseEntity<byte[]> export(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(service.exportInvoice(invoiceId));
    }
}