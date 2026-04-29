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
        vehicleMasterRepository.findById(vehicleId).orElseThrow(() -> new
                EntityNotFoundException("Vehicle not found with id " + vehicleId));

        driverMasterRepository.findById(driverId).orElseThrow(() -> new
                EntityNotFoundException("Driver not found with id " + driverId));

        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException("SalesOrder not found with id " + salesOrderId));

        if (!"CONFIRMED".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Gate outward entry can be created only for CONFIRMED sales order");
        }

        GateEntryOutward outward = new GateEntryOutward();
        outward.setUuid(UUID.randomUUID().toString());
        outward.setGateEntryNumber(requestDto.getGateEntryNumber() != null ? requestDto.getGateEntryNumber()
                : "OUT-" + System.currentTimeMillis());

        outward.setEntryDateTime(requestDto.getEntryDateTime() != null ? requestDto.getEntryDateTime()
                : LocalDateTime.now());

        outward.setVehicleId(vehicleId);
        outward.setDriverId(driverId);

        outward.setCustomerId(requestDto.getCustomerId() != null ? requestDto.getCustomerId()
                : salesOrder.getCustomerId());

        outward.setSoNumber(requestDto.getSoNumber() != null ? requestDto.getSoNumber()
                : String.valueOf(salesOrder.getSalesOrderId()));
        outward.setDcNumber(requestDto.getDcNumber());
        outward.setTransportName(requestDto.getTransportName());
        outward.setRemarks(requestDto.getRemarks());
        outward.setStatus("CREATED");

        return map(repository.save(outward));

    }

}