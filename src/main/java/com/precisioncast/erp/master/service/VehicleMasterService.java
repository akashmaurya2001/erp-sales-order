package com.precisioncast.erp.master.service;

import com.precisioncast.erp.master.dto.VehicleMasterRequestDto;
import com.precisioncast.erp.master.dto.VehicleMasterResponseDto;

import java.util.List;

public interface VehicleMasterService {

    VehicleMasterResponseDto createVehicle(VehicleMasterRequestDto requestDto);

    List<VehicleMasterResponseDto> getAllVehicles();

    VehicleMasterResponseDto getVehicleById(Long vehicleId);

    void deleteVehicle(Long vehicleId);

}
