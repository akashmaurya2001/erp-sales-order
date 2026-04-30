package com.precisioncast.erp.gateentryoutward.controller;

import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardItemBulkRequestDto;
import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardItemResponseDto;
import com.precisioncast.erp.gateentryoutward.service.GateEntryOutwardItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/gate/outwardItems")
@RequiredArgsConstructor
public class GateEntryOutwardItemController {

    private final GateEntryOutwardItemService service;

    @PostMapping("/add/{gateEntryOutwardId}/{itemId}/{qty}/{batchUuid}")
    public ResponseEntity<GateEntryOutwardItemResponseDto> add(
            @PathVariable Long gateEntryOutwardId,
            @PathVariable Long itemId,
            @PathVariable BigDecimal qty,
            @PathVariable String batchUuid) {
        return ResponseEntity.ok(service.addItem(gateEntryOutwardId,itemId, qty, batchUuid));
    }

    @PostMapping("/addBulk/{gateEntryOutwardId}")
    public ResponseEntity<List<GateEntryOutwardItemResponseDto>> addBulk(
            @PathVariable Long gateEntryOutwardId,
            @RequestBody GateEntryOutwardItemBulkRequestDto requestDto) {
        return ResponseEntity.ok(service.addBulkItems(gateEntryOutwardId, requestDto));
    }

    @GetMapping("/list/{gateEntryOutwardId}")
    public ResponseEntity<List<GateEntryOutwardItemResponseDto>> list(
            @PathVariable Long gateEntryOutwardId) {
        return ResponseEntity.ok(service.getItems(gateEntryOutwardId));
    }

    @PatchMapping("/updateQty/{outwardItemId}/{qty}")
    public ResponseEntity<GateEntryOutwardItemResponseDto> updateQty(
            @PathVariable Long outwardItemId,
            @PathVariable BigDecimal qty){
        return ResponseEntity.ok(service.updateQty(outwardItemId, qty));
    }

    @PatchMapping("/markRejected/{outwardItemId}/{reasonId}")
    public ResponseEntity<GateEntryOutwardItemResponseDto> markRejected(
            @PathVariable Long outwardItemId,
            @PathVariable Long reasonId) {
        return ResponseEntity.ok(service.markRejected(outwardItemId, reasonId));
    }

    @DeleteMapping("/delete/{outwardItemId}")
    public ResponseEntity<String> delete(@PathVariable Long outwardItemId) {
        service.deleteItem(outwardItemId);
        return ResponseEntity.ok("Gate outward item deleted successfully");
    }

    @GetMapping("/export/{gateEntryOutwardId}")
    public ResponseEntity<byte[]> export(@PathVariable Long gateEntryOutwardId) {
        byte[] file = service.exportItems(gateEntryOutwardId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=gate_outward_items_" + gateEntryOutwardId + ".csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
    }
}
