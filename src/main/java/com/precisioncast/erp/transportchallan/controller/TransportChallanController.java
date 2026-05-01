package com.precisioncast.erp.transportchallan.controller;

import com.precisioncast.erp.transportchallan.dto.TransportChallanManualRequestDto;
import com.precisioncast.erp.transportchallan.dto.TransportChallanRequestDto;
import com.precisioncast.erp.transportchallan.dto.TransportChallanResponseDto;
import com.precisioncast.erp.transportchallan.service.TransportChallanService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transportChallan")
@RequiredArgsConstructor
public class TransportChallanController {

    private final TransportChallanService service;

    @PostMapping("/create/{vehicleId}/{driverId}/{destinationWarehouseId}/{createdBy}")
    public ResponseEntity<TransportChallanResponseDto> create(
            @PathVariable Long vehicleId,
            @PathVariable Long driverId,
            @PathVariable Long destinationWarehouseId,
            @PathVariable Long createdBy,
            @RequestBody TransportChallanRequestDto dto) {
        return ResponseEntity.ok(service.create(vehicleId, driverId, destinationWarehouseId, createdBy, dto));
    }

    @PostMapping("/createManual")
    public ResponseEntity<TransportChallanResponseDto> createManual(
            @RequestBody TransportChallanManualRequestDto dto) {
        return ResponseEntity.ok(service.createManual(dto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<TransportChallanResponseDto>> list() {
        return ResponseEntity.ok(service.getAll());
    }
    @GetMapping("/{transportChallanId}")
    public ResponseEntity<TransportChallanResponseDto> getById(
            @PathVariable Long transportChallanId) {
        return ResponseEntity.ok(service.getById(transportChallanId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TransportChallanResponseDto>> search(
            @RequestParam(required = false) String vendor,
            @RequestParam(required = false) Long vehicle,
            @RequestParam(required = false) Long driver,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(service.search(vendor, vehicle, driver, status, from, to));
    }

    @PutMapping("/update/{transportChallanId}")
    public ResponseEntity<TransportChallanResponseDto> update(
            @PathVariable Long transportChallanId,
            @RequestBody TransportChallanRequestDto dto) {

        return ResponseEntity.ok(service.update(transportChallanId, dto));
    }

    @PatchMapping("/status/{transportChallanId}/{status}")
    public ResponseEntity<TransportChallanResponseDto> updateStatus(
            @PathVariable Long transportChallanId,
            @PathVariable String status) {

        return ResponseEntity.ok(service.updateStatus(transportChallanId, status));
    }

    @PatchMapping("/hold{transportChallanId}")
    public ResponseEntity<TransportChallanResponseDto> hold(
            @PathVariable Long transportChallanId) {

        return ResponseEntity.ok(service.hold(transportChallanId));
    }

    @PatchMapping("/release/{transportChallanId}")
    public ResponseEntity<TransportChallanResponseDto> release(
            @PathVariable Long transportChallanId) {

        return ResponseEntity.ok(service.release(transportChallanId));
    }

    @PatchMapping("/cancel/{transportChallanId}")
    public ResponseEntity<TransportChallanResponseDto> cancel(
            @PathVariable Long transportChallanId) {

        return ResponseEntity.ok(service.cancel(transportChallanId));
    }

    @DeleteMapping("/delete{transportChallanId}")
    public ResponseEntity<String> delete(
            @PathVariable Long transportChallanId) {

        service.delete(transportChallanId);
        return ResponseEntity.ok("Transport Challan deleted successfully");
    }

   @GetMapping("/export/{transportChallanId}")
    public ResponseEntity<byte[]> export(
            @PathVariable Long transportChallanId) {

        return ResponseEntity.ok(service.export(transportChallanId));
   }

}