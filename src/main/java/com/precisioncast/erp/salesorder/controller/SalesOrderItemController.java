package com.precisioncast.erp.salesorder.controller;

import com.precisioncast.erp.salesorder.dto.SalesOrderItemResponseDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderItemBulkRequestDto;
import com.precisioncast.erp.salesorder.service.SalesOrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/salesOrderItems")
@RequiredArgsConstructor
public class SalesOrderItemController {

    private final SalesOrderItemService salesOrderItemService;

    @PostMapping("/add/{salesOrderId}/{productId}/{qty}")
    public ResponseEntity<SalesOrderItemResponseDto> addSalesOrderItem(
            @PathVariable Long salesOrderId,
            @PathVariable Long productId,
            @PathVariable BigDecimal qty
    ) {
        return ResponseEntity.ok(
                salesOrderItemService.addSalesOrderItem(salesOrderId, productId, qty)
        );
    }

    @PostMapping("/addBulk/{salesOrderId}")
    public ResponseEntity<List<SalesOrderItemResponseDto>> addBulkSalesOrderItems(
            @PathVariable Long salesOrderId,
            @Valid @RequestBody SalesOrderItemBulkRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                salesOrderItemService.addBulkSalesOrderItem(salesOrderId, requestDto)
        );
    }

    @GetMapping("/list/{salesOrderId}")
    public ResponseEntity<List<SalesOrderItemResponseDto>> getSalesOrderItems(
            @PathVariable Long salesOrderId
    ) {
        return ResponseEntity.ok(
                salesOrderItemService.getSalesOrderItems(salesOrderId)
        );
    }

    @PatchMapping("/updateQty/{salesOrderItemId}/{qty}")
    public ResponseEntity<SalesOrderItemResponseDto> updateSalesOrderItemQty(
            @PathVariable Long salesOrderItemId,
            @PathVariable BigDecimal qty
    ) {
        return ResponseEntity.ok(
                salesOrderItemService.updateSalesOrderItemQty(salesOrderItemId, qty)
        );
    }

    @PatchMapping("/cancelItem/{salesOrderItemId}")
    public ResponseEntity<SalesOrderItemResponseDto> cancelSalesOrderItem(
            @PathVariable Long salesOrderItemId
    ) {
        return ResponseEntity.ok(
                salesOrderItemService.cancelSalesOrderItem(salesOrderItemId)
        );
    }

    @DeleteMapping("/delete/{salesOrderItemId}")
    public ResponseEntity<String> deleteSalesOrderItem(
            @PathVariable Long salesOrderItemId
    ) {
        salesOrderItemService.deleteSalesOrderItem(salesOrderItemId);
        return ResponseEntity.ok("Sales order item deleted successfully");
    }
}