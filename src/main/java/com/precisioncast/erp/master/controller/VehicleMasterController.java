package com.precisioncast.erp.master.controller;

import com.precisioncast.erp.master.dto.VehicleMasterBulkRequestDto;
import com.precisioncast.erp.master.dto.VehicleMasterRequestDto;
import com.precisioncast.erp.master.dto.VehicleMasterResponseDto;
import com.precisioncast.erp.master.service.VehicleMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleMasterController {

    private final VehicleMasterService service;

    @PostMapping("/create")
    public ResponseEntity<VehicleMasterResponseDto> create(@RequestBody VehicleMasterRequestDto dto) {
        return ResponseEntity.ok(service.createVehicle(dto));
    }

    @PostMapping("/createBulk")
    public ResponseEntity<List<VehicleMasterResponseDto>> createBulk(@RequestBody VehicleMasterBulkRequestDto dto) {
        return ResponseEntity.ok(service.createBulkVehicles(dto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<VehicleMasterResponseDto>> list() {
        return ResponseEntity.ok(service.getAllVehicles());
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleMasterResponseDto> getById(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(service.getVehicleById(vehicleId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<VehicleMasterResponseDto>> search(
            @RequestParam(required = false) String number,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean active) {

        return ResponseEntity.ok(service.searchVehicles(number, type, active));
    }

    @PatchMapping("/activate/{vehicleId}")
    public ResponseEntity<VehicleMasterResponseDto> activate(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(service.activateVehicle(vehicleId));
    }

    @PatchMapping("/deactivate/{vehicleId}")
    public ResponseEntity<VehicleMasterResponseDto> deactivate(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(service.deactivateVehicle(vehicleId));
    }

    @PutMapping("/update/{vehicleId}")
    public ResponseEntity<VehicleMasterResponseDto> update(
            @PathVariable Long vehicleId,
            @RequestBody VehicleMasterRequestDto dto) {

        return ResponseEntity.ok(service.updateVehicle(vehicleId, dto));
    }

    @DeleteMapping("/delete/{vehicleId}")
    public ResponseEntity<String> delete(@PathVariable Long vehicleId) {
        service.deleteVehicle(vehicleId);
        return ResponseEntity.ok("Vehicle deleted successfully");
    }

    @GetMapping("/export/{vehicleId}")
    public ResponseEntity<byte[]> export(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(service.exportVehicle(vehicleId));
    }

    @GetMapping("/inOutHistory/{vehicleId}")
    public ResponseEntity<List<String>> history(
            @PathVariable Long vehicleId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {

        return ResponseEntity.ok(service.getInOutHistory(vehicleId, from, to));
    }
}