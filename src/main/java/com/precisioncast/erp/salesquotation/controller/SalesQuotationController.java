package com.precisioncast.erp.salesquotation.controller;

import com.precisioncast.erp.common.exception.InvalidOperationException;
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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
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
    public ResponseEntity<Map<String, Object>> searchQuotations(
            @RequestParam Map<String, String> params
    ) {
        Set<String> allowedParams = Set.of(
                "quotationId",
                "customer", "customerId",
                "item", "itemId",
                "status",
                "quotationDate",
                "validityDate"
        );

        for (String key : params.keySet()) {
            if (!allowedParams.contains(key)) {
                throw new InvalidOperationException("Invalid search parameter: " + key);
            }
        }

        Long quotationId = parseLongParam(params.get("quotationId"), "quotationId");

        Long customer = parseLongParam(params.get("customer"), "customer");
        Long customerId = parseLongParam(params.get("customerId"), "customerId");
        if (customer != null && customerId != null && !customer.equals(customerId)) {
            throw new InvalidOperationException("customer and customerId must have the same value");
        }
        Long finalCustomerId = customerId != null ? customerId : customer;

        Long item = parseLongParam(params.get("item"), "item");
        Long itemId = parseLongParam(params.get("itemId"), "itemId");
        if (item != null && itemId != null && !item.equals(itemId)) {
            throw new InvalidOperationException("item and itemId must have the same value");
        }
        Long finalItemId = itemId != null ? itemId : item;

        String status = params.get("status");

        LocalDate quotationDate = parseDateParam(params.get("quotationDate"), "quotationDate");
        LocalDate validityDate = parseDateParam(params.get("validityDate"), "validityDate");

        List<SalesQuotationResponseDto> allResults = salesQuotationService.getAllQuotations();

        List<SalesQuotationResponseDto> filteredResults = allResults.stream()
                .filter(q -> quotationId == null || quotationId.equals(q.getQuotationId()))
                .filter(q -> finalCustomerId == null || finalCustomerId.equals(q.getCustomerId()))
                .filter(q -> status == null || status.isBlank()
                        || (q.getStatus() != null
                        && q.getStatus().toLowerCase().startsWith(status.trim().toLowerCase())))
                .filter(q -> quotationDate == null
                        || (q.getQuotationDate() != null && q.getQuotationDate().isEqual(quotationDate)))
                .filter(q -> validityDate == null
                        || (q.getValidityDate() != null && q.getValidityDate().isEqual(validityDate)))
                .filter(q -> {
                    if (finalItemId == null) {
                        return true;
                    }
                    if (q.getItems() == null) {
                        return false;
                    }
                    return q.getItems().stream().anyMatch(itemDto ->
                            finalItemId.equals(itemDto.getProductId()));
                })
                .toList();

        Map<String, Object> response = new LinkedHashMap<>();

        if (filteredResults.isEmpty()) {
            response.put("message", "No quotation found for the given search criteria");
            response.put("count", 0);
            response.put("data", filteredResults);
            return ResponseEntity.ok(response);
        }

        response.put("message", "Quotation search result fetched successfully");
        response.put("count", filteredResults.size());
        response.put("data", filteredResults);

        return ResponseEntity.ok(response);
    }

    private Long parseLongParam(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            Long parsed = Long.parseLong(value);
            if (parsed <= 0) {
                throw new InvalidOperationException(fieldName + " must be greater than 0");
            }
            return parsed;
        } catch (NumberFormatException ex) {
            throw new InvalidOperationException(fieldName + " must be a valid number");
        }
    }

    private LocalDate parseDateParam(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            throw new InvalidOperationException(fieldName + " must be in yyyy-MM-dd format");
        }
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