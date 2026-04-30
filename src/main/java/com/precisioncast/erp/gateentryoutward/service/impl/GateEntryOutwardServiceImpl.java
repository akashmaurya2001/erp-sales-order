package com.precisioncast.erp.gateentryoutward.service.impl;

import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardRequestDto;
import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardResponseDto;
import com.precisioncast.erp.gateentryoutward.entity.GateEntryOutward;
import com.precisioncast.erp.gateentryoutward.repository.GateEntryOutwardRepository;
import com.precisioncast.erp.gateentryoutward.service.GateEntryOutwardService;
import com.precisioncast.erp.master.repository.DriverMasterRepository;
import com.precisioncast.erp.master.repository.VehicleMasterRepository;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.repository.SalesOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GateEntryOutwardServiceImpl implements GateEntryOutwardService {

    private final GateEntryOutwardRepository repository;
    private final VehicleMasterRepository vehicleMasterRepository;
    private final DriverMasterRepository driverMasterRepository;
    private final SalesOrderRepository salesOrderRepository;

    @Override
    public GateEntryOutwardResponseDto createOutward(Long vehicleId, Long driverId, Long salesOrderId, Long createdBy, GateEntryOutwardRequestDto requestDto) {
        vehicleMasterRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + vehicleId));

        driverMasterRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with id: " + driverId));

        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Sales order not found with id: " + salesOrderId));

        if (!"CONFIRMED".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Gate outward entry can be created only for CONFIRMED sales order");
        }

        GateEntryOutward outward = new GateEntryOutward();
        outward.setUuid(UUID.randomUUID().toString());
        outward.setGateEntryNumber(
                requestDto.getGateEntryNumber() != null ? requestDto.getGateEntryNumber() : "OUT-" + System.currentTimeMillis()
        );
        outward.setEntryDateTime(
                requestDto.getEntryDateTime() != null ? requestDto.getEntryDateTime() : LocalDateTime.now()
        );
        outward.setCustomerId(salesOrder.getCustomerId());
        outward.setSoNumber(String.valueOf(salesOrder.getSalesOrderId()));
        outward.setVehicleId(vehicleId);
        outward.setDriverId(driverId);
        outward.setDcNumber(requestDto.getDcNumber());
        outward.setTransportName(requestDto.getTransportName());
        outward.setRemarks(requestDto.getRemarks());
        outward.setStatus("CREATED");

        return map(repository.save(outward));
    }

    @Override
    public GateEntryOutwardResponseDto createManual(GateEntryOutwardRequestDto requestDto) {
        GateEntryOutward outward = new GateEntryOutward();
        outward.setUuid(UUID.randomUUID().toString());
        outward.setGateEntryNumber(
                requestDto.getGateEntryNumber() != null ? requestDto.getGateEntryNumber() : "OUT-MANUAL-" + System.currentTimeMillis()
        );
        outward.setEntryDateTime(
                requestDto.getEntryDateTime() != null ? requestDto.getEntryDateTime() : LocalDateTime.now()
        );
        outward.setVehicleId(null);
        outward.setDriverId(null);
        outward.setDcNumber(requestDto.getDcNumber());
        outward.setTransportName(requestDto.getTransportName());
        outward.setRemarks(requestDto.getRemarks());
        outward.setStatus("CREATED");

        return map(repository.save(outward));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GateEntryOutwardResponseDto> getAll() {
        List<GateEntryOutwardResponseDto> list = new ArrayList<>();
        for (GateEntryOutward outward : repository.findAll()) {
            list.add(map(outward));
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public GateEntryOutwardResponseDto getById(Long gateEntryOutwardId) {
        return map(get(gateEntryOutwardId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GateEntryOutwardResponseDto> search(Long vehicleId, Long driverId, Long salesOrderId, String status, LocalDateTime from, LocalDateTime to) {
        List<GateEntryOutwardResponseDto> list = new ArrayList<>();

        for (GateEntryOutward outward : repository.findAll()) {
            boolean match = true;

            if (vehicleId != null && !vehicleId.equals(outward.getVehicleId())) {
                match = false;
            }

            if (driverId != null && !driverId.equals(outward.getDriverId())) {
                match = false;
            }

            if (salesOrderId != null && (outward.getSoNumber() == null || !outward.getSoNumber().equals(String.valueOf(salesOrderId)))) {
                match = false;
            }

            if (status != null && !status.isBlank()) {
                match = outward.getStatus() != null && outward.getStatus().equalsIgnoreCase(status);
            }

            if (from != null && (outward.getEntryDateTime() == null || outward.getEntryDateTime().isBefore(from))) {
                match = false;
            }

            if (to != null && (outward.getEntryDateTime() == null || outward.getEntryDateTime().isAfter(to))) {
                match = false;
            }

            if (match) {
                list.add(map(outward));
            }
        }

        return list;
    }

    @Override
    public GateEntryOutwardResponseDto markCompleted(Long gateEntryOutwardId) {
        GateEntryOutward outward = get(gateEntryOutwardId);

        if ("CANCELLED".equalsIgnoreCase(outward.getStatus())) {
            throw new IllegalStateException("Cancelled gate outward entry cannot be completed");
        }

        outward.setStatus("COMPLETED");
        return map(repository.save(outward));
    }

    @Override
    public GateEntryOutwardResponseDto hold(Long gateEntryOutwardId) {
        GateEntryOutward outward = get(gateEntryOutwardId);

        if (!"CREATED".equalsIgnoreCase(outward.getStatus())) {
            throw new IllegalStateException("Only CREATED gate outward entry can be put on HOLD");
        }

        outward.setStatus("HOLD");
        return map(repository.save(outward));
    }

    @Override
    public GateEntryOutwardResponseDto release(Long gateEntryOutwardId) {
        GateEntryOutward outward = get(gateEntryOutwardId);

        if (!"HOLD".equalsIgnoreCase(outward.getStatus())) {
            throw new IllegalStateException("Only HOLD gate outward entry can be released");
        }

        outward.setStatus("CREATED");
        return map(repository.save(outward));
    }

    @Override
    public GateEntryOutwardResponseDto cancel(Long gateEntryOutwardId) {
        GateEntryOutward outward = get(gateEntryOutwardId);

        if ("COMPLETED".equalsIgnoreCase(outward.getStatus())) {
            throw new IllegalStateException("Completed gate outward entry cannot be cancelled");
        }

        outward.setStatus("CANCELLED");
        return map(repository.save(outward));
    }

    @Override
    public void delete(Long gateEntryOutwardId) {
        GateEntryOutward outward = get(gateEntryOutwardId);

        if ("COMPLETED".equalsIgnoreCase(outward.getStatus()) || "CANCELLED".equalsIgnoreCase(outward.getStatus())) {
            throw new IllegalStateException("Completed or cancelled gate outward entry cannot be deleted");
        }

        repository.delete(outward);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] export(Long gateEntryOutwardId) {
        GateEntryOutward outward = get(gateEntryOutwardId);

        String csv = "Outward ID,UUID,Gate Entry No,Entry DateTime,Vehicle ID,Driver ID,Customer ID,SO Number,DC Number,Transport,Status,Remarks\n"
                + outward.getOutwardId() + ","
                + outward.getUuid() + ","
                + outward.getGateEntryNumber() + ","
                + outward.getEntryDateTime() + ","
                + outward.getVehicleId() + ","
                + outward.getDriverId() + ","
                + outward.getCustomerId() + ","
                + outward.getSoNumber() + ","
                + outward.getDcNumber() + ","
                + outward.getTransportName() + ","
                + outward.getStatus() + ","
                + outward.getRemarks();

        return csv.getBytes(StandardCharsets.UTF_8);
    }

    private GateEntryOutward get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gate outward entry not found with id: " + id));
    }

    private GateEntryOutwardResponseDto map(GateEntryOutward outward) {
        return GateEntryOutwardResponseDto.builder()
                .outwardId(outward.getOutwardId())
                .uuid(outward.getUuid())
                .gateEntryNumber(outward.getGateEntryNumber())
                .entryDateTime(outward.getEntryDateTime())
                .vehicleId(outward.getVehicleId())
                .driverId(outward.getDriverId())
                .customerId(outward.getCustomerId())
                .soNumber(outward.getSoNumber())
                .dcNumber(outward.getDcNumber())
                .transportName(outward.getTransportName())
                .remarks(outward.getRemarks())
                .status(outward.getStatus())
                .createdAt(outward.getCreatedAt())
                .updatedAt(outward.getUpdatedAt())
                .build();
    }
}