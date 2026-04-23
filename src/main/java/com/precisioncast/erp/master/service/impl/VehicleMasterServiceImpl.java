package com.precisioncast.erp.master.service.impl;

import com.precisioncast.erp.master.dto.VehicleMasterRequestDto;
import com.precisioncast.erp.master.dto.VehicleMasterResponseDto;
import com.precisioncast.erp.master.entity.VehicleMaster;
import com.precisioncast.erp.master.repository.VehicleMasterRepository;
import com.precisioncast.erp.master.service.VehicleMasterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleMasterServiceImpl implements VehicleMasterService {

    private final VehicleMasterRepository vehicleMasterRepository;

    @Override
    public VehicleMasterResponseDto createVehicle(VehicleMasterRequestDto requestDto) {
        VehicleMaster vehicle =  new VehicleMaster();
        vehicle.setUuid(requestDto.getUuid());
        vehicle.setVehicleNumber(requestDto.getVehicleNumber());
        vehicle.setVehicleType(requestDto.getVehicleType());
        vehicle.setCapacity(requestDto.getCapacity());
        vehicle.setOwnerName(requestDto.getOwnerName());
        vehicle.setContactNumber(requestDto.getContactNumber());
        vehicle.setIsActive(requestDto.getIsActive() !=null ? requestDto.getIsActive() : true);

        VehicleMaster saved = vehicleMasterRepository.save(vehicle);
        return mapToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleMasterResponseDto> getAllVehicles() {
        List<VehicleMasterResponseDto> responseDto = new ArrayList<>();
        for (VehicleMaster vehicle  : vehicleMasterRepository.findAll()) {
            responseDto.add(mapToResponseDto(vehicle));
        }
        return responseDto;
    }
    @Override
    @Transactional(readOnly = true)
    public VehicleMasterResponseDto getVehicleById(Long vehicleId) {
        VehicleMaster vehicle = vehicleMasterRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + vehicleId));
        return mapToResponseDto(vehicle);
    }

    @Override
    public void deleteVehicle(Long vehicleId) {
        VehicleMaster vehicle = vehicleMasterRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + vehicleId));
        vehicleMasterRepository.delete(vehicle);
    }

    private VehicleMasterResponseDto mapToResponseDto(VehicleMaster vehicle) {
        return VehicleMasterResponseDto.builder()
                .vehicleId(vehicle.getVehicleId())
                .uuid(vehicle.getUuid())
                .vehicleNumber(vehicle.getVehicleNumber())
                .vehicleType(vehicle.getVehicleType())
                .capacity(vehicle.getCapacity())
                .ownerName(vehicle.getOwnerName())
                .contactNumber(vehicle.getContactNumber())
                .isActive(vehicle.getIsActive())
                .createdAt(vehicle.getCreatedAt())
                .updatedAt(vehicle.getUpdatedAt())
                .build();
    }
}
