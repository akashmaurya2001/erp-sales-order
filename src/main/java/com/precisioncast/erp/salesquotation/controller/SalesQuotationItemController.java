package com.precisioncast.erp.salesquotation.controller;

import com.precisioncast.erp.salesquotation.dto.SalesQuotationItemRequestDto;
import com.precisioncast.erp.salesquotation.dto.SalesQuotationItemResponseDto;
import com.precisioncast.erp.salesquotation.service.SalesQuotationItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/salesQuotationItems")
@RequiredArgsConstructor
public class SalesQuotationItemController {

    private final SalesQuotationItemService salesQuotationItemService;

    @PostMapping("/add/{quotationId}/{itemId}/{qty}")
    public ResponseEntity<SalesQuotationItemResponseDto> addQuotationItem(
            @PathVariable Long quotationId,
            @PathVariable("itemId") Long productId,
            @PathVariable BigDecimal qty
    ) {
        return ResponseEntity.ok(
                salesQuotationItemService.addQuotationItem(quotationId, productId, qty)
        );
    }

    @PostMapping("/addBulk/{quotationId}")
    public ResponseEntity<List<SalesQuotationItemResponseDto>> addBulkQuotationItems(
            @PathVariable Long quotationId,
            @Valid @RequestBody List<SalesQuotationItemRequestDto> items
    ) {
        return ResponseEntity.ok(
                salesQuotationItemService.addBulkQuotationItems(quotationId, items)
        );
    }

    @GetMapping("/list/{quotationId}")
    public ResponseEntity<List<SalesQuotationItemResponseDto>> getQuotationItems(
            @PathVariable Long quotationId
    ) {
        return ResponseEntity.ok(
                salesQuotationItemService.getQuotationItems(quotationId)
        );
    }

    @PatchMapping("/updateQty/{quotationItemId}/{qty}")
    public ResponseEntity<SalesQuotationItemResponseDto> updateQuotationItemQty(
            @PathVariable Long quotationItemId,
            @PathVariable BigDecimal qty
    ) {
        return ResponseEntity.ok(
                salesQuotationItemService.updateQuotationItemQty(quotationItemId, qty)
        );
    }

    @DeleteMapping("/delete/{quotationItemId}")
    public ResponseEntity<String> deleteQuotationItem(
            @PathVariable Long quotationItemId
    ) {
        salesQuotationItemService.deleteQuotationItem(quotationItemId);
        return ResponseEntity.ok("Sales quotation item deleted successfully");
    }
}