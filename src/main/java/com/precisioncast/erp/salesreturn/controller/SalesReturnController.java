package com.precisioncast.erp.salesreturn.controller;

import com.precisioncast.erp.salesreturn.dto.SalesReturnRequestDto;
import com.precisioncast.erp.salesreturn.dto.SalesReturnResponseDto;
import com.precisioncast.erp.salesreturn.service.SalesReturnService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/sales-returns")
@RequiredArgsConstructor
public class SalesReturnController {

    private final SalesReturnService salesReturnService;

    @PostMapping
    public ResponseEntity<SalesReturnResponseDto> createSalesReturn(
            @Valid @RequestBody SalesReturnRequestDto requestDto) {
        return ResponseEntity.ok(salesReturnService.createSalesReturn(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<SalesReturnResponseDto>> getAllSalesReturns() {
        return ResponseEntity.ok(salesReturnService.getAllSalesReturns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesReturnResponseDto> getSalesReturnById(@PathVariable Long id) {
        return ResponseEntity.ok(salesReturnService.getSalesReturnById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSalesReturn(@PathVariable Long id) {
        salesReturnService.deleteSalesReturn(id);
        return ResponseEntity.ok("Sales return deleted successfully");
    }
}
