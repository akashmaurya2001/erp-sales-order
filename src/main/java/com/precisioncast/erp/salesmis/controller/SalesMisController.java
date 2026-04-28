package com.precisioncast.erp.salesmis.controller;

import com.precisioncast.erp.salesmis.service.SalesMisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/salesMIS")
@RequiredArgsConstructor
public class SalesMisController {

    private final SalesMisService service;

    @GetMapping("/dashboard/salesCount")
    public ResponseEntity<Long> salesCount() {
        return ResponseEntity.ok(service.getSalesCount());
    }

    @GetMapping("/dashboard/invoiceCount")
    public ResponseEntity<Long> invoiceCount() {
        return ResponseEntity.ok(service.getInvoiceCount());
    }

    @GetMapping("/dashboard/dispatchCount")
    public ResponseEntity<Long> dispatchCount() {
        return ResponseEntity.ok(service.getDispatchCount());
    }

    @GetMapping("/dashboard/salesReturnCount")
    public ResponseEntity<Long> salesReturnCount() {
        return ResponseEntity.ok(service.getSalesReturnCount());
    }

    @GetMapping("/dashboard/revenueToday")
    public ResponseEntity<BigDecimal> revenueToday() {
        return ResponseEntity.ok(service.getRevenueToday());
    }

    @GetMapping("/dashboard/revenueRange")
    public ResponseEntity<BigDecimal> revenueRange(
            @RequestParam(required = false)LocalDate from,
            @RequestParam(required = false)LocalDate to) {
        return ResponseEntity.ok(service.getRevenueRange(from, to));
    }

    @GetMapping("/export/salesSnapshot/{month}/{year}")
    public ResponseEntity<byte[]> exportSalesSnapshot(
            @PathVariable Integer month,
            @PathVariable Integer year) {
        byte[] file = service.exportSalesSnapshot(month, year);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=sales_snapshot_" + month + "_" + year + ".csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
    }

}
