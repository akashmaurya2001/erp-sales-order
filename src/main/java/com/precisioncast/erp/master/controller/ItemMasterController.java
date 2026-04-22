package com.precisioncast.erp.master.controller;

import com.precisioncast.erp.master.dto.ItemMasterBulkRequestDto;
import com.precisioncast.erp.master.dto.ItemMasterRequestDto;
import com.precisioncast.erp.master.dto.ItemMasterResponseDto;
import com.precisioncast.erp.master.service.ItemMasterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/master/items")
@RequiredArgsConstructor
public class ItemMasterController {

    private final ItemMasterService itemMasterService;

    @PostMapping("/create")
    public ResponseEntity<ItemMasterResponseDto> createItem(
            @Valid @RequestBody ItemMasterRequestDto requestDto) {
        return ResponseEntity.ok(itemMasterService.createItem(requestDto));
    }

    @PostMapping("/createBulk")
    public ResponseEntity<List<ItemMasterResponseDto>> createBulkItems(
            @Valid @RequestBody ItemMasterBulkRequestDto requestDto) {
        return ResponseEntity.ok(itemMasterService.createBulkItems(requestDto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ItemMasterResponseDto>> getAllItems() {
        return ResponseEntity.ok(itemMasterService.getAllItems());
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemMasterResponseDto> getItemById(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemMasterService.getItemById(itemId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemMasterResponseDto>> searchItems(
            @RequestParam(required = false) String itemCode,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.ok(
                itemMasterService.searchItems(itemCode, itemName, categoryId, active)
        );
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<ItemMasterResponseDto> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemMasterRequestDto requestDto) {
        return ResponseEntity.ok(itemMasterService.updateItem(itemId, requestDto));
    }

    @PatchMapping("/activate/{itemId}")
    public ResponseEntity<ItemMasterResponseDto> activateItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemMasterService.activateItem(itemId));
    }

    @PatchMapping("/deactivate/{itemId}")
    public ResponseEntity<ItemMasterResponseDto> deactivateItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemMasterService.deactivateItem(itemId));
    }

    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable Long itemId) {
        itemMasterService.deleteItem(itemId);
        return ResponseEntity.ok("Item deleted successfully");
    }

    @GetMapping("/export/{itemId}")
    public ResponseEntity<byte[]> exportItem(@PathVariable Long itemId) {
        byte[] fileData = itemMasterService.exportItem(itemId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=item_" + itemId + ".csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileData);
    }

    @PostMapping("/importCsv")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok("Import CSV API placeholder added. Final import logic will be implemented after base item master testing.");
    }
}