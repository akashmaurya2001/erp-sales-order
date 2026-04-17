package com.precisioncast.erp.transportchallan.controller;

import com.precisioncast.erp.transportchallan.dto.TransportChallanRequestDto;
import com.precisioncast.erp.transportchallan.dto.TransportChallanResponseDto;
import com.precisioncast.erp.transportchallan.service.TransportChallanService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/transport-challans")
@RequiredArgsConstructor
public class TransportChallanController {

    private final TransportChallanService transportChallanService;

    @PostMapping
    public ResponseEntity<TransportChallanResponseDto> createTransportChallan(
            @Valid @RequestBody TransportChallanRequestDto requestDto) {
        return ResponseEntity.ok(transportChallanService.createTransportChallan(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<TransportChallanResponseDto>> getAllTransportChallans() {
        return ResponseEntity.ok(transportChallanService.getAllTransportChallans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportChallanResponseDto> getTransportChallanById(@PathVariable Long id) {
        return ResponseEntity.ok(transportChallanService.getTransportChallanById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransportChallan(@PathVariable Long id) {
        transportChallanService.deleteTransportChallan(id);
        return ResponseEntity.ok("Transport challan deleted successfully");
    }
}