package com.precisioncast.erp.transportchallan.service.impl;

import com.precisioncast.erp.master.repository.DriverMasterRepository;
import com.precisioncast.erp.master.repository.VehicleMasterRepository;
import com.precisioncast.erp.transportchallan.dto.TransportChallanManualRequestDto;
import com.precisioncast.erp.transportchallan.dto.TransportChallanRequestDto;
import com.precisioncast.erp.transportchallan.dto.TransportChallanResponseDto;
import com.precisioncast.erp.transportchallan.entity.TransportChallan;
import com.precisioncast.erp.transportchallan.repository.TransportChallanRepository;
import com.precisioncast.erp.transportchallan.service.TransportChallanService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TransportChallanServiceImpl implements TransportChallanService {

    private final TransportChallanRepository repository;
    private final VehicleMasterRepository vehicleRepository;
    private final DriverMasterRepository driverRepository;

    @Override
    public TransportChallanResponseDto create(Long vehicleId, Long driverId, Long destinationWarehouseId, Long createdBy, TransportChallanRequestDto dto) {

        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + vehicleId));

        driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with id: " + driverId));

        TransportChallan challan = new TransportChallan();
        challan.setUuid(UUID.randomUUID().toString());
        challan.setVehicleId(vehicleId);
        challan.setDriverId(driverId);
        challan.setDestinationWarehouseId(destinationWarehouseId);
        challan.setCreatedBy(createdBy);
        challan.setChallanNumber(dto.getChallanNumber() != null ? dto.getChallanNumber() : "TC-" + System.currentTimeMillis());
        challan.setVendor(dto.getVendor());
        challan.setSourceLocation(dto.getSourceLocation());
        challan.setDestinationLocation(dto.getDestinationLocation());
        challan.setChallanDate(dto.getChallanDate() != null ? dto.getChallanDate() : LocalDate.now());
        challan.setTotalWeight(dto.getTotalWeight());
        challan.setStatus("CREATED");
        challan.setRemarks(dto.getRemarks());

        return map(repository.save(challan));
    }

    @Override
    public TransportChallanResponseDto createManual(TransportChallanManualRequestDto dto) {

        if (dto.getVehicleId() != null) {
            vehicleRepository.findById(dto.getVehicleId())
                    .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + dto.getVehicleId()));
        }

        if (dto.getDriverId() != null) {
            driverRepository.findById(dto.getDriverId())
                    .orElseThrow(() -> new EntityNotFoundException("Driver not found with id: " + dto.getDriverId()));
        }

        TransportChallan challan = new TransportChallan();
        challan.setUuid(UUID.randomUUID().toString());
        challan.setVehicleId(dto.getVehicleId());
        challan.setDriverId(dto.getDriverId());
        challan.setDestinationWarehouseId(dto.getDestinationWarehouseId());
        challan.setCreatedBy(dto.getCreatedBy());
        challan.setChallanNumber(dto.getChallanNumber() != null ? dto.getChallanNumber() : "TC-MANUAL-" + System.currentTimeMillis());
        challan.setVendor(dto.getVendor());
        challan.setSourceLocation(dto.getSourceLocation());
        challan.setDestinationLocation(dto.getDestinationLocation());
        challan.setChallanDate(dto.getChallanDate() != null ? dto.getChallanDate() : LocalDate.now());
        challan.setTotalWeight(dto.getTotalWeight());
        challan.setStatus("CREATED");
        challan.setRemarks(dto.getRemarks());

        return map(repository.save(challan));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransportChallanResponseDto> getAll() {
        List<TransportChallanResponseDto> list = new ArrayList<>();
        for (TransportChallan challan : repository.findAll()) {
            list.add(map(challan));
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public TransportChallanResponseDto getById(Long transportChallanId) {
        return map(get(transportChallanId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransportChallanResponseDto> search(String vendor, Long vehicle, Long driver, String status, LocalDate from, LocalDate to) {
        List<TransportChallanResponseDto> list = new ArrayList<>();

        for (TransportChallan challan : repository.findAll()) {
            boolean match = true;

            if (vendor != null && !vendor.isBlank()) {
                match = challan.getVendor() != null && challan.getVendor().toLowerCase().contains(vendor.toLowerCase());
            }

            if (vehicle != null && !vehicle.equals(challan.getVehicleId())) {
                match = false;
            }

            if (driver != null && !driver.equals(challan.getDriverId())) {
                match = false;
            }

            if (status != null && !status.isBlank()) {
                match = challan.getStatus() != null && challan.getStatus().equalsIgnoreCase(status);
            }

            if (from != null && (challan.getChallanDate() == null || challan.getChallanDate().isBefore(from))) {
                match = false;
            }

            if (to != null && (challan.getChallanDate() == null || challan.getChallanDate().isAfter(to))) {
                match = false;
            }

            if (match) {
                list.add(map(challan));
            }
        }

        return list;
    }

    @Override
    public TransportChallanResponseDto update(Long transportChallanId, TransportChallanRequestDto dto) {
        TransportChallan challan = get(transportChallanId);

        if ("COMPLETED".equalsIgnoreCase(challan.getStatus()) || "CANCELLED".equalsIgnoreCase(challan.getStatus())) {
            throw new IllegalStateException("Completed or cancelled transport challan cannot be updated");
        }

        challan.setChallanNumber(dto.getChallanNumber() != null ? dto.getChallanNumber() : challan.getChallanNumber());
        challan.setVendor(dto.getVendor() != null ? dto.getVendor() : challan.getVendor());
        challan.setSourceLocation(dto.getSourceLocation() != null ? dto.getSourceLocation() : challan.getSourceLocation());
        challan.setDestinationLocation(dto.getDestinationLocation() != null ? dto.getDestinationLocation() : challan.getDestinationLocation());
        challan.setChallanDate(dto.getChallanDate() != null ? dto.getChallanDate() : challan.getChallanDate());
        challan.setTotalWeight(dto.getTotalWeight() != null ? dto.getTotalWeight() : challan.getTotalWeight());
        challan.setRemarks(dto.getRemarks() != null ? dto.getRemarks() : challan.getRemarks());

        return map(repository.save(challan));
    }

    @Override
    public TransportChallanResponseDto updateStatus(Long transportChallanId, String status) {
        TransportChallan challan = get(transportChallanId);

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status is required");
        }

        challan.setStatus(status.toUpperCase());
        return map(repository.save(challan));
    }

    @Override
    public TransportChallanResponseDto hold(Long transportChallanId) {
        TransportChallan challan = get(transportChallanId);

        if (!"CREATED".equalsIgnoreCase(challan.getStatus()) && !"RELEASED".equalsIgnoreCase(challan.getStatus())) {
            throw new IllegalStateException("Only CREATED or RELEASED transport challan can be put on HOLD");
        }

        challan.setStatus("HOLD");
        return map(repository.save(challan));
    }

    @Override
    public TransportChallanResponseDto release(Long transportChallanId) {
        TransportChallan challan = get(transportChallanId);

        if (!"HOLD".equalsIgnoreCase(challan.getStatus())) {
            throw new IllegalStateException("Only HOLD transport challan can be released");
        }

        challan.setStatus("RELEASED");
        return map(repository.save(challan));
    }

    @Override
    public TransportChallanResponseDto cancel(Long transportChallanId) {
        TransportChallan challan = get(transportChallanId);

        if ("COMPLETED".equalsIgnoreCase(challan.getStatus())) {
            throw new IllegalStateException("Completed transport challan cannot be cancelled");
        }

        challan.setStatus("CANCELLED");
        return map(repository.save(challan));
    }

    @Override
    public void delete(Long transportChallanId) {
        TransportChallan challan = get(transportChallanId);

        if ("COMPLETED".equalsIgnoreCase(challan.getStatus()) || "CANCELLED".equalsIgnoreCase(challan.getStatus())) {
            throw new IllegalStateException("Completed or cancelled transport challan cannot be deleted");
        }

        repository.delete(challan);
    }
    @Override
    @Transactional(readOnly = true)
    public byte[] export(Long transportChallanId) {
        TransportChallan c = get(transportChallanId);

        String csv = "ChallanId,UUID,ChallanNumber,VehicleId,DriverId,DestinationWarehouseId,CreatedBy,Vendor,Source,Destination,ChallanDate,TotalWeight,Status,Remarks\n"
                + c.getChallanId() + ","
                + c.getUuid() + ","
                + c.getChallanNumber() + ","
                + c.getVehicleId() + ","
                + c.getDriverId() + ","
                + c.getDestinationWarehouseId() + ","
                + c.getCreatedBy() + ","
                + c.getVendor() + ","
                + c.getSourceLocation() + ","
                + c.getDestinationLocation() + ","
                + c.getChallanDate() + ","
                + c.getTotalWeight() + ","
                + c.getStatus() + ","
                + c.getRemarks();

        return csv.getBytes(StandardCharsets.UTF_8);
    }

    private TransportChallan get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transport challan not found with id: " + id));
    }

    private TransportChallanResponseDto map(TransportChallan c) {
        return TransportChallanResponseDto.builder()
                .challanId(c.getChallanId())
                .uuid(c.getUuid())
                .challanNumber(c.getChallanNumber())
                .vehicleId(c.getVehicleId())
                .driverId(c.getDriverId())
                .destinationWarehouseId(c.getDestinationWarehouseId())
                .createdBy(c.getCreatedBy())
                .vendor(c.getVendor())
                .sourceLocation(c.getSourceLocation())
                .destinationLocation(c.getDestinationLocation())
                .challanDate(c.getChallanDate())
                .totalWeight(c.getTotalWeight())
                .status(c.getStatus())
                .remarks(c.getRemarks())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}