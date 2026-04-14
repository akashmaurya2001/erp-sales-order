package com.precisioncast.erp.master.service;

import com.precisioncast.erp.master.dto.DriverMasterRequestDto;
import com.precisioncast.erp.master.dto.DriverMasterResponseDto;

import java.util.List;

public interface DriverMasterService {

    DriverMasterResponseDto createDriver(DriverMasterRequestDto requestDto);

    List<DriverMasterResponseDto> getAllDrivers();

    DriverMasterResponseDto getDriverById(Long driverId);

    void deleteDriver(Long driverId);
}
