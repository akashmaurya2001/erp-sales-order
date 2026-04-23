package com.precisioncast.erp.salesquotation.controller;

import com.precisioncast.erp.salesquotation.dto.SalesQuotationItemRequestDto;
import com.precisioncast.erp.salesquotation.dto.SalesQuotationItemResponseDto;
import com.precisioncast.erp.salesquotation.service.SalesQuotationItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/salesQuotationItems")
@RequiredArgsConstructor
@Validated
public class SalesQuotationItemController {

    private final SalesQuotationItemService salesQuotationItemService;

    @PostMapping("/add/{quotationId}/{itemId}/{qty}")
    public ResponseEntity<SalesQuotationItemResponseDto> addQuotationItem(
            @PathVariable
            @Positive(message = "Quotation id must be greater than 0")
            Long quotationId,

            @PathVariable("itemId")
            @Positive(message = "Item id must be greater than 0")
            Long productId,

            @PathVariable
            @Positive(message = "Quantity must be greater than 0")
            BigDecimal qty
    ) {
        return ResponseEntity.ok(
                salesQuotationItemService.addQuotationItem(quotationId, productId, qty)
        );
    }

    @PostMapping("/addBulk/{quotationId}")
    public ResponseEntity<List<SalesQuotationItemResponseDto>> addBulkQuotationItems(
            @PathVariable
            @Positive(message = "Quotation id must be greater than 0")
            Long quotationId,

            @RequestBody
            @NotEmpty(message = "At least one quotation item is required")
            List<@Valid SalesQuotationItemRequestDto> items
    ) {
        return ResponseEntity.ok(
                salesQuotationItemService.addBulkQuotationItems(quotationId, items)
        );
    }

    @GetMapping("/list/{quotationId}")
    public ResponseEntity<List<SalesQuotationItemResponseDto>> getQuotationItems(
            @PathVariable
            @Positive(message = "Quotation id must be greater than 0")
            Long quotationId
    ) {
        return ResponseEntity.ok(
                salesQuotationItemService.getQuotationItems(quotationId)
        );
    }

    @PatchMapping("/updateQty/{quotationItemId}/{qty}")
    public ResponseEntity<SalesQuotationItemResponseDto> updateQuotationItemQty(
            @PathVariable
            @Positive(message = "Quotation item id must be greater than 0")
            Long quotationItemId,

            @PathVariable
            @Positive(message = "Quantity must be greater than 0")
            BigDecimal qty
    ) {
        return ResponseEntity.ok(
                salesQuotationItemService.updateQuotationItemQty(quotationItemId, qty)
        );
    }

    @DeleteMapping("/delete/{quotationItemId}")
    public ResponseEntity<String> deleteQuotationItem(
            @PathVariable
            @Positive(message = "Quotation item id must be greater than 0")
            Long quotationItemId
    ) {
        salesQuotationItemService.deleteQuotationItem(quotationItemId);
        return ResponseEntity.ok("Sales quotation item deleted successfully");
    }
}