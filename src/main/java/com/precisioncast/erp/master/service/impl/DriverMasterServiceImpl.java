package com.precisioncast.erp.master.service.impl;

import com.precisioncast.erp.master.repository.DriverMasterRepository;
import com.precisioncast.erp.master.dto.DriverMasterRequestDto;
import com.precisioncast.erp.master.dto.DriverMasterResponseDto;
import com.precisioncast.erp.master.entity.DriverMaster;
import com.precisioncast.erp.master.service.DriverMasterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DriverMasterServiceImpl implements DriverMasterService {

    private final DriverMasterRepository driverMasterRepository;

    @Override
    public DriverMasterResponseDto createDriver(DriverMasterRequestDto requestDto) {
        DriverMaster driver = new DriverMaster();
        driver.setUuid(requestDto.getUuid());
        driver.setDriverName(requestDto.getDriverName());
        driver.setLicenseNumber(requestDto.getLicenseNumber());
        driver.setPhoneNumber(requestDto.getPhoneNumber());
        driver.setAddress(requestDto.getAddress());
        driver.setIsActive(requestDto.getIsActive() != null ? requestDto.getIsActive() : true);

        DriverMaster saved =  driverMasterRepository.save(driver);
        return mapToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriverMasterResponseDto> getAllDrivers() {
        List<DriverMasterResponseDto> responseDto = new ArrayList<>();
        for (DriverMaster driver : driverMasterRepository.findAll()) {
            responseDto.add(mapToResponseDto(driver));
        }
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public DriverMasterResponseDto getDriverById(Long driverId) {
        DriverMaster driver = driverMasterRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with id: " + driverId));
        return mapToResponseDto(driver);
    }

    @Override
    public void deleteDriver(Long driverId) {
        DriverMaster driver = driverMasterRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with id: " + driverId));
        driverMasterRepository.delete(driver);
    }

    private DriverMasterResponseDto mapToResponseDto(DriverMaster driver) {
        return DriverMasterResponseDto.builder()
                .driverId(driver.getDriverId())
                .uuid(driver.getUuid())
                .driverName(driver.getDriverName())
                .licenseNumber(driver.getLicenseNumber())
                .phoneNumber(driver.getPhoneNumber())
                .address(driver.getAddress())
                .isActive(driver.getIsActive())
                .createdAt(driver.getCreatedAt())
                .updatedAt(driver.getUpdatedAt())
                .build();

    }

}
