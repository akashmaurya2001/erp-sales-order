package com.precisioncast.erp.salesorder.controller;

import com.precisioncast.erp.salesorder.dto.SalesOrderRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderResponseDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderUpdateRequestDto;
import com.precisioncast.erp.salesorder.service.SalesOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(
        name = "Sales Order Module",
        description = "APIs related to sales order creation, retrieval, & management."
)
@RestController
@RequestMapping("/api/salesOrder")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping("/create")
    public ResponseEntity<SalesOrderResponseDto> createSalesOrder(
            @Valid @RequestBody SalesOrderRequestDto requestDto) {
        return ResponseEntity.ok(salesOrderService.createSalesOrder(requestDto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<SalesOrderResponseDto>> getAllSalesOrders() {
        return ResponseEntity.ok(salesOrderService.getAllSalesOrders());
    }

    @GetMapping("/{salesOrderId}")
    public ResponseEntity<SalesOrderResponseDto> getSalesOrderById(
            @PathVariable Long salesOrderId) {
        return ResponseEntity.ok(salesOrderService.getSalesOrderById(salesOrderId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SalesOrderResponseDto>> searchSalesOrders(
            @RequestParam(required = false) Long customer,
            @RequestParam(required = false) Long item,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(
                salesOrderService.searchSalesOrders(customer, item, status, from, to)
        );
    }

    @PutMapping("/update/{salesOrderId}")
    public ResponseEntity<SalesOrderResponseDto> updateSalesOrder(
            @PathVariable Long salesOrderId,
            @Valid @RequestBody SalesOrderUpdateRequestDto requestDto) {
        return ResponseEntity.ok(salesOrderService.updateSalesOrder(salesOrderId, requestDto));
    }

    @PatchMapping("/status/{salesOrderId}/{status}")
    public ResponseEntity<SalesOrderResponseDto> updateSalesOrderStatus(
            @PathVariable Long salesOrderId,
            @PathVariable String status) {
        return ResponseEntity.ok(salesOrderService.updateSalesOrderStatus(salesOrderId, status));
    }

    @PatchMapping("/verifyCustomer/{salesOrderId}")
    public ResponseEntity<SalesOrderResponseDto> verifyCustomer(
            @PathVariable Long salesOrderId) {
        return ResponseEntity.ok(salesOrderService.verifyCustomer(salesOrderId));
    }

    @PatchMapping("/markUrgent/{salesOrderId}")
    public ResponseEntity<SalesOrderResponseDto> markUrgent(
            @PathVariable Long salesOrderId) {
        return ResponseEntity.ok(salesOrderService.markUrgent(salesOrderId));
    }

    @PatchMapping("/markHold/{salesOrderId}")
    public ResponseEntity<SalesOrderResponseDto> markHold(
            @PathVariable Long salesOrderId) {
        return ResponseEntity.ok(salesOrderService.markHold(salesOrderId));
    }

    @DeleteMapping("/delete/{salesOrderId}")
    public ResponseEntity<String> deleteSalesOrder(
            @PathVariable Long salesOrderId) {
        salesOrderService.deleteSalesOrder(salesOrderId);
        return ResponseEntity.ok("Sales order deleted successfully");
    }
}