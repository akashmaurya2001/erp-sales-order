package com.precisioncast.erp.master.service;

import com.precisioncast.erp.master.dto.DriverMasterBulkRequestDto;
import com.precisioncast.erp.master.dto.DriverMasterRequestDto;
import com.precisioncast.erp.master.dto.DriverMasterResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface DriverMasterService {

    DriverMasterResponseDto createDriver(DriverMasterRequestDto requestDto);

    List<DriverMasterResponseDto> createBulkDrivers(DriverMasterBulkRequestDto requestDto);

    List<DriverMasterResponseDto> getAllDrivers();

    DriverMasterResponseDto getDriverById(Long driverId);

    List<DriverMasterResponseDto> searchDrivers(String name, String mobile, Boolean active);

    DriverMasterResponseDto activateDriver(Long driverId);

    DriverMasterResponseDto deactivateDriver(Long driverId);

    DriverMasterResponseDto updateDriver(Long driverId, DriverMasterRequestDto requestDto);

    void deleteDriver(Long driverId);

    byte[] exportDriver(Long driverId);

    List<String> getTripHistory(Long driverId, LocalDate from, LocalDate to);
}