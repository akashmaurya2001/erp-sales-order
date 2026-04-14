package com.precisioncast.erp.master.controller;

import com.precisioncast.erp.master.dto.DriverMasterRequestDto;
import com.precisioncast.erp.master.dto.DriverMasterResponseDto;
import com.precisioncast.erp.master.service.DriverMasterService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverMasterController {

    private final DriverMasterService driverMasterService;

    @PostMapping
    public ResponseEntity<DriverMasterResponseDto> createDriver(
            @Valid @RequestBody DriverMasterRequestDto requestDto){
        return ResponseEntity.ok().body(driverMasterService.createDriver(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<DriverMasterResponseDto>> getAllDrivers() {
        return ResponseEntity.ok(driverMasterService.getAllDrivers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverMasterResponseDto> getDriverById(@PathVariable Long id){
        return ResponseEntity.ok(driverMasterService.getDriverById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDriver(@PathVariable Long id){
        driverMasterService.deleteDriver(id);
        return ResponseEntity.ok("Delete Driver Successfully");
    }
}
