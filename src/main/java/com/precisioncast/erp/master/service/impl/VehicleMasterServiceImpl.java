package com.precisioncast.erp.master.service.impl;

import com.precisioncast.erp.master.repository.VehicleMasterRepository;
import com.precisioncast.erp.master.dto.VehicleMasterBulkRequestDto;
import com.precisioncast.erp.master.dto.VehicleMasterRequestDto;
import com.precisioncast.erp.master.dto.VehicleMasterResponseDto;
import com.precisioncast.erp.master.entity.VehicleMaster;
import com.precisioncast.erp.master.service.VehicleMasterService;
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
public class VehicleMasterServiceImpl implements VehicleMasterService {

    private final VehicleMasterRepository repository;

    @Override
    public VehicleMasterResponseDto createVehicle(VehicleMasterRequestDto dto) {

        repository.findByVehicleNumber(dto.getVehicleNumber())
                .ifPresent(v -> {
                    throw new IllegalStateException("Vehicle with same number already exists");
                });

        VehicleMaster vehicle = new VehicleMaster();
        vehicle.setVehicleNumber(dto.getVehicleNumber());
        vehicle.setUuid(UUID.randomUUID().toString());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setCapacity(dto.getCapacity());
        vehicle.setOwnerName(dto.getOwnerName());
        vehicle.setContactNumber(dto.getContactNumber());
        vehicle.setIsActive(true);
        vehicle.setCreatedAt(LocalDateTime.now());
        vehicle.setUpdatedAt(LocalDateTime.now());

        return map(repository.save(vehicle));
    }

    @Override
    public List<VehicleMasterResponseDto> createBulkVehicles(VehicleMasterBulkRequestDto requestDto) {
        List<VehicleMasterResponseDto> list = new ArrayList<>();

        for (VehicleMasterRequestDto dto : requestDto.getVehicles()) {
            list.add(createVehicle(dto));
        }

        return list;
    }

    @Override
    public List<VehicleMasterResponseDto> getAllVehicles() {
        List<VehicleMasterResponseDto> list = new ArrayList<>();

        for (VehicleMaster v : repository.findAll()) {
            if (Boolean.TRUE.equals(v.getIsActive())) {
                list.add(map(v));
            }
        }

        return list;
    }

    @Override
    public VehicleMasterResponseDto getVehicleById(Long vehicleId) {
        return map(get(vehicleId));
    }

    @Override
    public List<VehicleMasterResponseDto> searchVehicles(String number, String type, Boolean active) {

        List<VehicleMasterResponseDto> list = new ArrayList<>();

        for (VehicleMaster v : repository.findAll()) {

            boolean match = true;

            if (number != null && !v.getVehicleNumber().toLowerCase().contains(number.toLowerCase())) {
                match = false;
            }

            if (type != null && !v.getVehicleType().toLowerCase().contains(type.toLowerCase())) {
                match = false;
            }

            if (active != null && !v.getIsActive().equals(active)) {
                match = false;
            }

            if (match) {
                list.add(map(v));
            }
        }

        return list;
    }

    @Override
    public VehicleMasterResponseDto activateVehicle(Long vehicleId) {
        VehicleMaster v = get(vehicleId);
        v.setIsActive(true);
        v.setUpdatedAt(LocalDateTime.now());
        return map(repository.save(v));
    }

    @Override
    public VehicleMasterResponseDto deactivateVehicle(Long vehicleId) {
        VehicleMaster v = get(vehicleId);
        v.setIsActive(false);
        v.setUpdatedAt(LocalDateTime.now());
        return map(repository.save(v));
    }

    @Override
    public VehicleMasterResponseDto updateVehicle(Long vehicleId, VehicleMasterRequestDto dto) {

        VehicleMaster vehicle = get(vehicleId);

        repository.findByVehicleNumber(dto.getVehicleNumber())
                .filter(v -> !v.getVehicleId().equals(vehicleId))
                .ifPresent(v -> {
                    throw new IllegalStateException("Vehicle number already used");
                });

        vehicle.setVehicleNumber(dto.getVehicleNumber());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setCapacity(dto.getCapacity());
        vehicle.setOwnerName(dto.getOwnerName());
        vehicle.setContactNumber(dto.getContactNumber());
        vehicle.setUpdatedAt(LocalDateTime.now());

        return map(repository.save(vehicle));
    }

    @Override
    public void deleteVehicle(Long vehicleId) {
        VehicleMaster v = get(vehicleId);
        v.setIsActive(false);
        repository.save(v);
    }

    @Override
    public byte[] exportVehicle(Long vehicleId) {
        return ("Vehicle Export ID: " + vehicleId).getBytes();
    }

    @Override
    public List<String> getInOutHistory(Long vehicleId, LocalDate from, LocalDate to) {
        List<String> list = new ArrayList<>();
        list.add("Vehicle in/out history will be implemented later");
        return list;
    }

    private VehicleMaster get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));
    }

    private VehicleMasterResponseDto map(VehicleMaster v) {
        return VehicleMasterResponseDto.builder()
                .vehicleId(v.getVehicleId())
                .uuid(v.getUuid())
                .vehicleNumber(v.getVehicleNumber())
                .vehicleType(v.getVehicleType())
                .capacity(v.getCapacity())
                .ownerName(v.getOwnerName())
                .contactNumber(v.getContactNumber())
                .isActive(v.getIsActive())
                .createdAt(v.getCreatedAt())
                .updatedAt(v.getUpdatedAt())
                .build();
    }
}