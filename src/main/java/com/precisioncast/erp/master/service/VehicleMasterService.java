package com.precisioncast.erp.master.service;

import com.precisioncast.erp.master.dto.VehicleMasterBulkRequestDto;
import com.precisioncast.erp.master.dto.VehicleMasterRequestDto;
import com.precisioncast.erp.master.dto.VehicleMasterResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface VehicleMasterService {

    VehicleMasterResponseDto createVehicle(VehicleMasterRequestDto requestDto);

    List<VehicleMasterResponseDto> createBulkVehicles(VehicleMasterBulkRequestDto requestDto);

    List<VehicleMasterResponseDto> getAllVehicles();

    VehicleMasterResponseDto getVehicleById(Long vehicleId);

    List<VehicleMasterResponseDto> searchVehicles(String number, String type, Boolean active);

    VehicleMasterResponseDto activateVehicle(Long vehicleId);

    VehicleMasterResponseDto deactivateVehicle(Long vehicleId);

    VehicleMasterResponseDto updateVehicle(Long vehicleId, VehicleMasterRequestDto requestDto);

    void deleteVehicle(Long vehicleId);

    byte[] exportVehicle(Long vehicleId);

    List<String> getInOutHistory(Long vehicleId, LocalDate from, LocalDate to);

}
