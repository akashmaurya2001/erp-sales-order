package com.precisioncast.erp.master.service.impl;

import com.precisioncast.erp.master.repository.DriverMasterRepository;
import com.precisioncast.erp.master.dto.DriverMasterBulkRequestDto;
import com.precisioncast.erp.master.dto.DriverMasterRequestDto;
import com.precisioncast.erp.master.dto.DriverMasterResponseDto;
import com.precisioncast.erp.master.entity.DriverMaster;
import com.precisioncast.erp.master.service.DriverMasterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DriverMasterServiceImpl implements DriverMasterService {

    private final DriverMasterRepository repository;

    @Override
    public DriverMasterResponseDto createDriver(DriverMasterRequestDto dto) {

        repository.findByLicenseNumber(dto.getLicenseNumber())
                .ifPresent(d -> {
                    throw new IllegalStateException("Driver with same license already exists");
                });

        DriverMaster driver = new DriverMaster();
        driver.setUuid(UUID.randomUUID().toString());
        driver.setDriverName(dto.getDriverName());
        driver.setLicenseNumber(dto.getLicenseNumber());
        driver.setPhoneNumber(dto.getPhoneNumber());
        driver.setAddress(dto.getAddress());
        driver.setIsActive(true);
        driver.setCreatedAt(LocalDateTime.now());
        driver.setUpdatedAt(LocalDateTime.now());

        return map(repository.save(driver));
    }

    @Override
    public List<DriverMasterResponseDto> createBulkDrivers(DriverMasterBulkRequestDto requestDto) {
        List<DriverMasterResponseDto> responseList = new ArrayList<>();

        for (DriverMasterRequestDto dto : requestDto.getDrivers()) {
            responseList.add(createDriver(dto));
        }

        return responseList;
    }

    @Override
    public List<DriverMasterResponseDto> getAllDrivers() {
        List<DriverMasterResponseDto> list = new ArrayList<>();

        for (DriverMaster d : repository.findAll()) {
            if (Boolean.TRUE.equals(d.getIsActive())) {
                list.add(map(d));
            }
        }

        return list;
    }

    @Override
    public DriverMasterResponseDto getDriverById(Long driverId) {
        return map(get(driverId));
    }

    @Override
    public List<DriverMasterResponseDto> searchDrivers(String name, String mobile, Boolean active) {

        List<DriverMasterResponseDto> list = new ArrayList<>();

        for (DriverMaster d : repository.findAll()) {

            boolean match = true;

            if (name != null && !d.getDriverName().toLowerCase().contains(name.toLowerCase())) {
                match = false;
            }

            if (mobile != null && !d.getPhoneNumber().contains(mobile)) {
                match = false;
            }

            if (active != null && !d.getIsActive().equals(active)) {
                match = false;
            }

            if (match) {
                list.add(map(d));
            }
        }

        return list;
    }

    @Override
    public DriverMasterResponseDto activateDriver(Long driverId) {
        DriverMaster d = get(driverId);
        d.setIsActive(true);
        d.setUpdatedAt(LocalDateTime.now());
        return map(repository.save(d));
    }

    @Override
    public DriverMasterResponseDto deactivateDriver(Long driverId) {
        DriverMaster d = get(driverId);
        d.setIsActive(false);
        d.setUpdatedAt(LocalDateTime.now());
        return map(repository.save(d));
    }

    @Override
    public DriverMasterResponseDto updateDriver(Long driverId, DriverMasterRequestDto dto) {

        DriverMaster driver = get(driverId);

        repository.findByLicenseNumber(dto.getLicenseNumber())
                .filter(d -> !d.getDriverId().equals(driverId))
                .ifPresent(d -> {
                    throw new IllegalStateException("License already used by another driver");
                });

        driver.setDriverName(dto.getDriverName());
        driver.setLicenseNumber(dto.getLicenseNumber());
        driver.setPhoneNumber(dto.getPhoneNumber());
        driver.setAddress(dto.getAddress());
        driver.setUpdatedAt(LocalDateTime.now());

        return map(repository.save(driver));
    }

    @Override
    public void deleteDriver(Long driverId) {
        DriverMaster driver = get(driverId);
        driver.setIsActive(false);
        repository.save(driver);
    }

    @Override
    public byte[] exportDriver(Long driverId) {
        return ("Driver Export ID: " + driverId).getBytes();
    }

    @Override
    public List<String> getTripHistory(Long driverId, LocalDate from, LocalDate to) {
        List<String> list = new ArrayList<>();
        list.add("Trip data will be implemented later");
        return list;
    }

    private DriverMaster get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));
    }

    private DriverMasterResponseDto map(DriverMaster d) {
        return DriverMasterResponseDto.builder()
                .driverId(d.getDriverId())
                .uuid(d.getUuid())
                .driverName(d.getDriverName())
                .licenseNumber(d.getLicenseNumber())
                .phoneNumber(d.getPhoneNumber())
                .address(d.getAddress())
                .isActive(d.getIsActive())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }
}