package com.precisioncast.erp.deliverychallan.controller;

import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanRequestDto;
import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanResponseDto;
import com.precisioncast.erp.deliverychallan.service.DeliveryChallanService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/delivery-challans")
@RequiredArgsConstructor
public class DeliveryChallanController {

    private final DeliveryChallanService deliveryChallanService;

    @PostMapping
    public ResponseEntity<DeliveryChallanResponseDto> createDeliveryChallan(
            @Valid @RequestBody DeliveryChallanRequestDto requestDto) {
        return ResponseEntity.ok(deliveryChallanService.createDeliveryChallan(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<DeliveryChallanResponseDto>> getAllDeliveryChallans() {
        return ResponseEntity.ok(deliveryChallanService.getAllDeliveryChallans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryChallanResponseDto> getDeliveryChallanById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryChallanService.getDeliveryChallanById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDeliveryChallan(@PathVariable Long id) {
        deliveryChallanService.deleteDeliveryChallan(id);
        return ResponseEntity.ok("Delivery challan deleted successfully");
    }
}