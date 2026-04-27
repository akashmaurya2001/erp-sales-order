package com.precisioncast.erp.salesreturn.controller;

import com.precisioncast.erp.salesreturn.dto.SalesReturnItemBulkRequestDto;
import com.precisioncast.erp.salesreturn.dto.SalesReturnItemResponseDto;
import com.precisioncast.erp.salesreturn.service.SalesReturnItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/salesReturnItems")
@RequiredArgsConstructor
public class SalesReturnItemController {

    private final SalesReturnItemService service;

    @PostMapping("/add/{salesReturnId}/{itemId}/{qty}/{reasonId}")
    public ResponseEntity<SalesReturnItemResponseDto> add(
            @PathVariable Long salesReturnId,
            @PathVariable Long itemId,
            @PathVariable BigDecimal qty,
            @PathVariable Long reasonId) {

        return ResponseEntity.ok(service.addItem(salesReturnId, itemId, qty, reasonId));
    }

    @PostMapping("/addBulk/{salesReturnId}")
    public ResponseEntity<List<SalesReturnItemResponseDto>> addBulk(
            @PathVariable Long salesReturnId,
            @RequestBody SalesReturnItemBulkRequestDto dto) {

        return ResponseEntity.ok(service.addBulkItems(salesReturnId, dto));
    }

    @GetMapping("/list/{salesReturnId}")
    public ResponseEntity<List<SalesReturnItemResponseDto>> list(
            @PathVariable Long salesReturnId) {

        return ResponseEntity.ok(service.getItemsByReturnId(salesReturnId));
    }

    @PatchMapping("/updateQty/{salesReturnItemId}/{qty}")
    public ResponseEntity<SalesReturnItemResponseDto> updateQty(
            @PathVariable Long salesReturnItemId,
            @PathVariable BigDecimal qty) {

        return ResponseEntity.ok(service.updateQty(salesReturnItemId, qty));
    }

    @DeleteMapping("/delete/{salesReturnItemId}")
    public ResponseEntity<String> delete(
            @PathVariable Long salesReturnItemId) {

        service.deleteItem(salesReturnItemId);
        return ResponseEntity.ok("Sales return item deleted successfully");
    }
}