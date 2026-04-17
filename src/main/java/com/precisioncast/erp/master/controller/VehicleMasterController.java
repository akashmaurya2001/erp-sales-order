package com.precisioncast.erp.master.controller;

import com.precisioncast.erp.master.dto.VehicleMasterRequestDto;
import com.precisioncast.erp.master.dto.VehicleMasterResponseDto;
import com.precisioncast.erp.master.service.VehicleMasterService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleMasterController {

    private final VehicleMasterService vehicleMasterService;

    @PostMapping
    public ResponseEntity<VehicleMasterResponseDto> createVehicle(
            @Valid @RequestBody VehicleMasterRequestDto requestDto){
        return ResponseEntity.ok(vehicleMasterService.createVehicle(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<VehicleMasterResponseDto>> getAllVehicles() {
        return ResponseEntity.ok(vehicleMasterService.getAllVehicles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleMasterResponseDto> getVehicleById(@PathVariable Long id){
        return ResponseEntity.ok(vehicleMasterService.getVehicleById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long id){
        vehicleMasterService.deleteVehicle(id);
        return ResponseEntity.ok("Vehicle deleted successfully");
    }
}
