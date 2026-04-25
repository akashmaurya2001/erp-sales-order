package com.precisioncast.erp.salesorder.controller;

import com.precisioncast.erp.salesorder.dto.SalesOrderItemBulkRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderItemResponseDto;
import com.precisioncast.erp.salesorder.service.SalesOrderItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/salesOrderItems")
@RequiredArgsConstructor
@Validated
public class SalesOrderItemController {

    private final SalesOrderItemService salesOrderItemService;

    @PostMapping("/add/{salesOrderId}/{itemId}/{qty}")
    public ResponseEntity<SalesOrderItemResponseDto> addSalesOrderItem(
            @PathVariable
            @Positive(message = "Sales order id must be greater than 0")
            Long salesOrderId,

            @PathVariable("itemId")
            @Positive(message = "Item id must be greater than 0")
            Long productId,

            @PathVariable
            @Positive(message = "Qty should not be Zero")
            BigDecimal qty
    ) {
        return ResponseEntity.ok(
                salesOrderItemService.addSalesOrderItem(salesOrderId, productId, qty)
        );
    }

    @PostMapping("/addBulk/{salesOrderId}")
    public ResponseEntity<List<SalesOrderItemResponseDto>> addBulkSalesOrderItems(
            @PathVariable
            @Positive(message = "Sales order id must be greater than 0")
            Long salesOrderId,

            @Valid @RequestBody SalesOrderItemBulkRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                salesOrderItemService.addBulkSalesOrderItem(salesOrderId, requestDto)
        );
    }

    @GetMapping("/list/{salesOrderId}")
    public ResponseEntity<List<SalesOrderItemResponseDto>> getSalesOrderItems(
            @PathVariable
            @Positive(message = "Sales order id must be greater than 0")
            Long salesOrderId
    ) {
        return ResponseEntity.ok(
                salesOrderItemService.getSalesOrderItems(salesOrderId)
        );
    }

    @PatchMapping("/updateQty/{salesOrderItemId}/{qty}")
    public ResponseEntity<SalesOrderItemResponseDto> updateSalesOrderItemQty(
            @PathVariable
            @Positive(message = "Sales order item id must be greater than 0")
            Long salesOrderItemId,

            @PathVariable
            @Positive(message = "Qty should not be Zero")
            BigDecimal qty
    ) {
        return ResponseEntity.ok(
                salesOrderItemService.updateSalesOrderItemQty(salesOrderItemId, qty)
        );
    }

    @PatchMapping("/cancelItem/{salesOrderItemId}")
    public ResponseEntity<SalesOrderItemResponseDto> cancelSalesOrderItem(
            @PathVariable
            @Positive(message = "Sales order item id must be greater than 0")
            Long salesOrderItemId
    ) {
        return ResponseEntity.ok(
                salesOrderItemService.cancelSalesOrderItem(salesOrderItemId)
        );
    }

    @DeleteMapping("/delete/{salesOrderItemId}")
    public ResponseEntity<String> deleteSalesOrderItem(
            @PathVariable
            @Positive(message = "Sales order item id must be greater than 0")
            Long salesOrderItemId
    ) {
        salesOrderItemService.deleteSalesOrderItem(salesOrderItemId);
        return ResponseEntity.ok("Sales order item deleted successfully");
    }
}