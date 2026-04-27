package com.precisioncast.erp.salesinvoice.controller;

import com.precisioncast.erp.salesinvoice.dto.InvoiceItemBulkRequestDto;
import com.precisioncast.erp.salesinvoice.dto.InvoiceItemResponseDto;
import com.precisioncast.erp.salesinvoice.service.InvoiceItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/invoiceItems")
@RequiredArgsConstructor
public class InvoiceItemController {

    private final InvoiceItemService service;

    @PostMapping("/add/{invoiceId}/{itemId}/{qty}/{price}")
    public ResponseEntity<InvoiceItemResponseDto> add(
            @PathVariable Long invoiceId,
            @PathVariable Long itemId,
            @PathVariable BigDecimal qty,
            @PathVariable BigDecimal price) {
        return ResponseEntity.ok(service.addItem(invoiceId, itemId, qty, price));
    }

    @PostMapping("/addBulk/{invoiceId}")
    public ResponseEntity<List<InvoiceItemResponseDto>> addBulk(
            @PathVariable Long invoiceId,
            @RequestBody InvoiceItemBulkRequestDto dto) {
        return ResponseEntity.ok(service.addBulkItems(invoiceId, dto));
    }

    @GetMapping("/list/{invoiceId}")
    public ResponseEntity<List<InvoiceItemResponseDto>> list(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(service.getItemsByInvoice(invoiceId));
    }

    @PatchMapping("/updateQty/{invoiceItemId}/{qty}")
    public ResponseEntity<InvoiceItemResponseDto> updateQty(
            @PathVariable Long invoiceItemId,
            @PathVariable BigDecimal qty) {
        return ResponseEntity.ok(service.updateQty(invoiceItemId, qty));
    }

    @PatchMapping("/updatePrice/{invoiceItemId}/{price}")
    public ResponseEntity<InvoiceItemResponseDto> updatePrice(
            @PathVariable Long invoiceItemId,
            @PathVariable BigDecimal price) {
        return ResponseEntity.ok(service.updatePrice(invoiceItemId, price));
    }

    @DeleteMapping("/delete/{invoiceItemId}")
    public ResponseEntity<String> delete(@PathVariable Long invoiceItemId) {
        service.deleteItem(invoiceItemId);
        return ResponseEntity.ok("Invoice item deleted successfully");
    }
}
