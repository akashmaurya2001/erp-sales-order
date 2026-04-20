package com.precisioncast.erp.salesquotation.controller;

import com.precisioncast.erp.salesquotation.dto.SalesQuotationRequestDto;
import com.precisioncast.erp.salesquotation.dto.SalesQuotationResponseDto;
import com.precisioncast.erp.salesquotation.service.SalesQuotationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salesQuotation")
@RequiredArgsConstructor
@Validated
public class SalesQuotationController {

    private final SalesQuotationService salesQuotationService;

    @PostMapping("/create")
    public ResponseEntity<SalesQuotationResponseDto> createQuotation(
            @Valid @RequestBody SalesQuotationRequestDto requestDto) {
        return ResponseEntity.ok(salesQuotationService.createQuotation(requestDto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<SalesQuotationResponseDto>> getAllQuotations() {
        return ResponseEntity.ok(salesQuotationService.getAllQuotations());
    }


    @GetMapping("/{quotationId}")
    public ResponseEntity<SalesQuotationResponseDto> getQuotationById(
            @PathVariable @Positive(message = "Quotation id must be greater then 0")
            Long quotationId) {
        return ResponseEntity.ok(salesQuotationService.getQuotationById(quotationId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SalesQuotationResponseDto>> searchQuotations(
            @RequestParam(required = false) Long customer,
            @RequestParam(required = false) Long item,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(
                salesQuotationService.searchQuotations(customer, item, status)
        );
    }


    @PutMapping("/update/{quotationId}")
    public ResponseEntity<?> updateQuotation(
            @PathVariable
            @Positive(message = "Quotation id must be greater than 0")
            Long quotationId,
            @RequestBody SalesQuotationRequestDto requestDto) {
        return ResponseEntity.ok(salesQuotationService.updateQuotation(quotationId, requestDto));
    }


    @PatchMapping("/approve/{quotationId}")
    public ResponseEntity<SalesQuotationResponseDto> approveQuotation(
            @PathVariable @Positive(message = "Quotation id must be greater than 0")
            Long quotationId) {
        return ResponseEntity.ok(salesQuotationService.approveQuotation(quotationId));
    }

    @PatchMapping("/reject/{quotationId}")
    public ResponseEntity<SalesQuotationResponseDto> rejectQuotation(
            @PathVariable @Positive(message = "Quotation id must be greater than 0")
            Long quotationId) {
        return ResponseEntity.ok(salesQuotationService.rejectQuotation(quotationId));
    }

    @PatchMapping("/activate/{quotationId}")
    public ResponseEntity<SalesQuotationResponseDto> activateQuotation(
            @PathVariable @Positive(message = "Quotation id must be greater than 0")
            Long quotationId) {
        return ResponseEntity.ok(salesQuotationService.activateQuotation(quotationId));
    }

    @PatchMapping("/deactivate/{quotationId}")
    public ResponseEntity<SalesQuotationResponseDto> deactivateQuotation(
            @PathVariable @Positive(message = "Quotation id must be greater than 0")
            Long quotationId) {
        return ResponseEntity.ok(salesQuotationService.deactivateQuotation(quotationId));
    }

    @DeleteMapping("/delete/{quotationId}")
    public ResponseEntity<String> deleteQuotation(
            @PathVariable @Positive(message = "Quotation id must be greater than 0")
            Long quotationId) {
        salesQuotationService.deleteQuotation(quotationId);
        return ResponseEntity.ok("Sales quotation deleted successfully");
    }

    @GetMapping("/export/{quotationId}")
    public ResponseEntity<byte[]> exportQuotation(
            @PathVariable @Positive(message = "Quotation id must be greater than 0")
            Long quotationId) {
        byte[] fileData = salesQuotationService.exportQuotation(quotationId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=quotation_" + quotationId + ".csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileData);
    }
}