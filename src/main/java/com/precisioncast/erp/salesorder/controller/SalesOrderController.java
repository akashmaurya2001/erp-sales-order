package com.precisioncast.erp.salesorder.controller;

import com.precisioncast.erp.salesorder.dto.SalesOrderRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderResponseDto;
import com.precisioncast.erp.salesorder.service.SalesOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping
    public ResponseEntity<SalesOrderResponseDto> createSalesOrder(
            @Valid @RequestBody SalesOrderRequestDto requestDto) {
        SalesOrderResponseDto response = salesOrderService.createSalesOrder(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SalesOrderResponseDto>> getAllSalesOrders() {
        List<SalesOrderResponseDto> response = salesOrderService.getAllSalesOrders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrderResponseDto> getSalesOrderById(@PathVariable Long id) {
        SalesOrderResponseDto response = salesOrderService.getSalesOrderById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSalesOrderById(@PathVariable Long id) {
        salesOrderService.deleteSalesOrder(id);
        return ResponseEntity.ok("SalesOrder deleted successfully with id: " + id);
    }

}
