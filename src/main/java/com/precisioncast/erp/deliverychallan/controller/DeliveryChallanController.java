package com.precisioncast.erp.deliverychallan.controller;

import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanResponseDto;
import com.precisioncast.erp.deliverychallan.service.DeliveryChallanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveryChallan")
@RequiredArgsConstructor
public class DeliveryChallanController {

    private final DeliveryChallanService service;

    @PostMapping("/create/{salesOrderId}/{dispatchId}")
    public ResponseEntity<DeliveryChallanResponseDto> createChallan(
            @PathVariable Long salesOrderId,
            @PathVariable Long dispatchId
    ) {
        return ResponseEntity.ok(service.createChallan(salesOrderId, dispatchId));
    }

    @GetMapping("/{challanId}")
    public ResponseEntity<DeliveryChallanResponseDto> getChallanById(
            @PathVariable Long challanId
    ) {
        return ResponseEntity.ok(service.getChallanById(challanId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<DeliveryChallanResponseDto>> getAllChallans() {
        return ResponseEntity.ok(service.getAllChallans());
    }

    @PatchMapping("/complete/{challanId}")
    public ResponseEntity<DeliveryChallanResponseDto> completeChallan(
            @PathVariable Long challanId
    ) {
        return ResponseEntity.ok(service.completeChallan(challanId));
    }

    @DeleteMapping("/delete/{challanId}")
    public ResponseEntity<String> deleteChallan(
            @PathVariable Long challanId
    ) {
        service.deleteChallan(challanId);
        return ResponseEntity.ok("Delivery challan deleted successfully");
    }
}