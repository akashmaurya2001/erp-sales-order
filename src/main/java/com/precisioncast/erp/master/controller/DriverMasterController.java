package com.precisioncast.erp.master.controller;

import com.precisioncast.erp.master.dto.DriverMasterBulkRequestDto;
import com.precisioncast.erp.master.dto.DriverMasterRequestDto;
import com.precisioncast.erp.master.dto.DriverMasterResponseDto;
import com.precisioncast.erp.master.service.DriverMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverMasterController {

    private final DriverMasterService service;

    @PostMapping("/create")
    public ResponseEntity<DriverMasterResponseDto> create(@RequestBody DriverMasterRequestDto dto) {
        return ResponseEntity.ok(service.createDriver(dto));
    }

    @PostMapping("/createBulk")
    public ResponseEntity<List<DriverMasterResponseDto>> createBulk(@RequestBody DriverMasterBulkRequestDto dto) {
        return ResponseEntity.ok(service.createBulkDrivers(dto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<DriverMasterResponseDto>> list() {
        return ResponseEntity.ok(service.getAllDrivers());
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<DriverMasterResponseDto> getById(@PathVariable Long driverId) {
        return ResponseEntity.ok(service.getDriverById(driverId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DriverMasterResponseDto>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) Boolean active) {

        return ResponseEntity.ok(service.searchDrivers(name, mobile, active));
    }

    @PatchMapping("/activate/{driverId}")
    public ResponseEntity<DriverMasterResponseDto> activate(@PathVariable Long driverId) {
        return ResponseEntity.ok(service.activateDriver(driverId));
    }

    @PatchMapping("/deactivate/{driverId}")
    public ResponseEntity<DriverMasterResponseDto> deactivate(@PathVariable Long driverId) {
        return ResponseEntity.ok(service.deactivateDriver(driverId));
    }

    @PutMapping("/update/{driverId}")
    public ResponseEntity<DriverMasterResponseDto> update(
            @PathVariable Long driverId,
            @RequestBody DriverMasterRequestDto dto) {

        return ResponseEntity.ok(service.updateDriver(driverId, dto));
    }

    @DeleteMapping("/delete/{driverId}")
    public ResponseEntity<String> delete(@PathVariable Long driverId) {
        service.deleteDriver(driverId);
        return ResponseEntity.ok("Driver deleted successfully");
    }

    @GetMapping("/export/{driverId}")
    public ResponseEntity<byte[]> export(@PathVariable Long driverId) {
        return ResponseEntity.ok(service.exportDriver(driverId));
    }

    @GetMapping("/tripHistory/{driverId}")
    public ResponseEntity<List<String>> tripHistory(
            @PathVariable Long driverId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {

        return ResponseEntity.ok(service.getTripHistory(driverId, from, to));
    }
}