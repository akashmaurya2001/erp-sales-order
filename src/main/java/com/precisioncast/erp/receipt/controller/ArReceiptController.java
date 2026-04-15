package com.precisioncast.erp.receipt.controller;

import com.precisioncast.erp.receipt.dto.ArReceiptRequestDto;
import com.precisioncast.erp.receipt.dto.ArReceiptResponseDto;
import com.precisioncast.erp.receipt.service.ArReceiptService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ArReceiptController {

    private final ArReceiptService arReceiptService;

    @PostMapping
    public ResponseEntity<ArReceiptResponseDto> createArReceipt(
            @Valid @RequestBody ArReceiptRequestDto requestDto) {
        return ResponseEntity.ok(arReceiptService.createReceipt(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<ArReceiptResponseDto>> getAllArReceipts() {
        return ResponseEntity.ok(arReceiptService.getAllReceipts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArReceiptResponseDto> getArReceiptById(@PathVariable Long id) {
        return ResponseEntity.ok(arReceiptService.getReceiptById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArReceiptById(@PathVariable Long id) {
        arReceiptService.deleteReceiptById(id);
        return ResponseEntity.ok(" Receipt deleted successfully");
    }
}
