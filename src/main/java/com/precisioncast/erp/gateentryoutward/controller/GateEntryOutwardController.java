package com.precisioncast.erp.gateentryoutward.controller;

import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardRequestDto;
import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardResponseDto;
import com.precisioncast.erp.gateentryoutward.service.GateEntryOutwardService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/gate-outward")
@RequiredArgsConstructor
public class GateEntryOutwardController {

    private final GateEntryOutwardService gateEntryOutwardService;

    @PostMapping
    public ResponseEntity<GateEntryOutwardResponseDto> createGateEntryOutward(
            @Valid  @RequestBody GateEntryOutwardRequestDto requestDto) {
        return ResponseEntity.ok(gateEntryOutwardService.createGateEntryOutward(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<GateEntryOutwardResponseDto>> getAllGateEntryOutward() {
        return ResponseEntity.ok(gateEntryOutwardService.getAllGateEntryOutward());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GateEntryOutwardResponseDto> getGateEntryOutwardById(@PathVariable Long id) {
        return ResponseEntity.ok(gateEntryOutwardService.getGateEntryOutwardById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGateEntryOutward(@PathVariable Long id) {
        gateEntryOutwardService.deleteGateEntryOutward(id);
        return ResponseEntity.ok("Gate entry outward deleted successfully");
    }

}
