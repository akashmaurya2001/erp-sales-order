package com.precisioncast.erp.gateentryoutward.controller;

import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardRequestDto;
import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardResponseDto;
import com.precisioncast.erp.gateentryoutward.service.GateEntryOutwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/gate/outward")
@RequiredArgsConstructor
public class GateEntryOutwardController {

    private final GateEntryOutwardService service;

    @PostMapping("/create/{vehicleId}/{driverId}/{salesOrderId}/{createdBy}")
    public ResponseEntity<GateEntryOutwardResponseDto> create(
            @PathVariable Long vehicleId,
            @PathVariable Long driverId,
            @PathVariable Long salesOrderId,
            @PathVariable Long createdBy,
            @RequestBody GateEntryOutwardRequestDto requestDto) {
        return ResponseEntity.ok(service.createOutward(vehicleId, driverId, salesOrderId, createdBy, requestDto));
    }

    @PostMapping("/createManual")
    public ResponseEntity<GateEntryOutwardResponseDto> createManual(
            @RequestBody GateEntryOutwardRequestDto requestDto) {
            return ResponseEntity.ok(service.createManual(requestDto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<GateEntryOutwardResponseDto>> list() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{gateEntryOutwardId}")
    public ResponseEntity<GateEntryOutwardResponseDto> getById
            (@PathVariable Long gateEntryOutwardId) {
        return ResponseEntity.ok(service.getById(gateEntryOutwardId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<GateEntryOutwardResponseDto>> search(
            @RequestParam(required = false) Long vehicle,
            @RequestParam(required = false) Long driver,
            @RequestParam(required = false) Long order,
            @RequestParam(required = false) String status,
            @RequestParam(required = false)LocalDateTime from,
            @RequestParam(required = false)LocalDateTime to) {
        return ResponseEntity.ok(service.search(vehicle, driver, order, status, from, to));
    }

    @PatchMapping("/markCompleted/{gateEntryOutwardId}")
    public ResponseEntity<GateEntryOutwardResponseDto> markCompleted(
            @PathVariable Long gateEntryOutwardId) {
        return ResponseEntity.ok(service.markCompleted(gateEntryOutwardId));
    }

    @PatchMapping("/hold/{gateEntryOutwardId}")
    public ResponseEntity<GateEntryOutwardResponseDto> hold(
            @PathVariable Long gateEntryOutwardId) {
        return ResponseEntity.ok(service.hold(gateEntryOutwardId));
    }

    @PatchMapping("/release/{gateEntryOutwardId}")
    public ResponseEntity<GateEntryOutwardResponseDto> release(
            @PathVariable Long gateEntryOutwardId) {
        return ResponseEntity.ok(service.release(gateEntryOutwardId));
    }

    @PatchMapping("/cancel/{gateEntryOutwardId}")
    public ResponseEntity<GateEntryOutwardResponseDto> cancel(
            @PathVariable Long gateEntryOutwardId) {
        return ResponseEntity.ok(service.cancel(gateEntryOutwardId));
    }

    @DeleteMapping("/delete/{gateEntryOutwardId}")
    public ResponseEntity<String> delete(
            @PathVariable Long gateEntryOutwardId) {
        service.delete(gateEntryOutwardId);
        return ResponseEntity.ok("Gate outward entry deleted successfully.");
    }

    @GetMapping("/export/{gateEntryOutwardId}")
    public ResponseEntity<byte[]> export(
            @PathVariable Long gateEntryOutwardId) {

        byte[] file = service.export(gateEntryOutwardId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename_outward_" + gateEntryOutwardId + ".csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
    }
}
