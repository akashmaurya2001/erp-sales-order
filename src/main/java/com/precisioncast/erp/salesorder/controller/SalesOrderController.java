package com.precisioncast.erp.salesorder.controller;

import com.precisioncast.erp.salesorder.dto.SalesOrderRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderResponseDto;
import com.precisioncast.erp.salesorder.service.SalesOrderService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(
        name = "Sales Order Module",
        description = "APIs related to sales order creation, retrieval, & management."
)

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping
    public ResponseEntity<SalesOrderResponseDto> createSalesOrder(
            @Valid @RequestBody SalesOrderRequestDto requestDto) {
        return ResponseEntity.ok(salesOrderService.createSalesOrder(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<SalesOrderResponseDto>> getAllSalesOrders() {
        return ResponseEntity.ok(salesOrderService.getAllSalesOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrderResponseDto> getSalesOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(salesOrderService.getSalesOrderById(id));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<SalesOrderResponseDto> confirmSalesOrder(@PathVariable Long id) {
        return ResponseEntity.ok(salesOrderService.confirmSalesOrder(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<SalesOrderResponseDto> cancelSalesOrder(@PathVariable Long id) {
        return ResponseEntity.ok(salesOrderService.cancelSalesOrder(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSalesOrder(@PathVariable Long id) {
        salesOrderService.deleteSalesOrder(id);
        return ResponseEntity.ok("Sales order deleted successfully");
    }
}