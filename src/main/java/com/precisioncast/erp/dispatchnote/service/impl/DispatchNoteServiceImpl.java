package com.precisioncast.erp.dispatchnote.service.impl;

import com.precisioncast.erp.dispatchnote.dto.DispatchNoteRequestDto;
import com.precisioncast.erp.dispatchnote.dto.DispatchNoteResponseDto;
import com.precisioncast.erp.dispatchnote.entity.DispatchNote;
import com.precisioncast.erp.dispatchnote.repository.DispatchNoteRepository;
import com.precisioncast.erp.dispatchnote.service.DispatchNoteService;
import com.precisioncast.erp.master.entity.DriverMaster;
import com.precisioncast.erp.master.entity.VehicleMaster;
import com.precisioncast.erp.master.repository.DriverMasterRepository;
import com.precisioncast.erp.master.repository.VehicleMasterRepository;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.repository.SalesOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DispatchNoteServiceImpl implements DispatchNoteService {

    private final DispatchNoteRepository repository;
    private final SalesOrderRepository salesOrderRepository;
    private final VehicleMasterRepository vehicleRepository;
    private final DriverMasterRepository driverRepository;

    @Override
    public DispatchNoteResponseDto createDispatchNote(Long salesOrderId, Long vehicleId, Long driverId, DispatchNoteRequestDto dto) {

        SalesOrder order = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Sales order not found"));

        if (!"CONFIRMED".equalsIgnoreCase(order.getOrderStatus())) {
            throw new IllegalStateException("Dispatch allowed only for CONFIRMED orders");
        }

        VehicleMaster vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

        DriverMaster driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        DispatchNote dispatch = new DispatchNote();
        dispatch.setSalesOrderId(order.getSalesOrderId());
        dispatch.setDispatchDate(dto.getDispatchDate());
        dispatch.setVehicleNo(vehicle.getVehicleNumber());
        dispatch.setDriverName(driver.getDriverName());
        dispatch.setRemarks(dto.getRemarks());
        dispatch.setStatus("CREATED");

        return map(repository.save(dispatch));
    }

    @Override
    public List<DispatchNoteResponseDto> getAllDispatchNotes() {
        return repository.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public DispatchNoteResponseDto getDispatchNoteById(Long dispatchId) {
        return map(get(dispatchId));
    }

    @Override
    public List<DispatchNoteResponseDto> searchDispatchNotes(Long customerId, Long vehicleId, LocalDate from, LocalDate to) {

        List<DispatchNote> list = repository.findAll();

        return list.stream().filter(d -> {
            boolean match = true;

            if (from != null && to != null) {
                match &= (d.getDispatchDate().isEqual(from) || d.getDispatchDate().isAfter(from)) &&
                        (d.getDispatchDate().isEqual(to) || d.getDispatchDate().isBefore(to));
            }

            return match;
        }).map(this::map).collect(Collectors.toList());
    }

    @Override
    public DispatchNoteResponseDto markLoaded(Long dispatchId) {
        DispatchNote d = get(dispatchId);

        if (!"CREATED".equalsIgnoreCase(d.getStatus())) {
            throw new IllegalStateException("Only CREATED can be LOADED");
        }

        d.setStatus("LOADED");
        return map(repository.save(d));
    }

    @Override
    public DispatchNoteResponseDto markOutForDelivery(Long dispatchId) {
        DispatchNote d = get(dispatchId);

        if (!"LOADED".equalsIgnoreCase(d.getStatus())) {
            throw new IllegalStateException("Only LOADED can be OUT_FOR_DELIVERY");
        }

        d.setStatus("OUT_FOR_DELIVERY");
        return map(repository.save(d));
    }

    @Override
    public DispatchNoteResponseDto completeDispatch(Long dispatchId) {
        DispatchNote d = get(dispatchId);

        if (!"OUT_FOR_DELIVERY".equalsIgnoreCase(d.getStatus())) {
            throw new IllegalStateException("Only OUT_FOR_DELIVERY can be COMPLETED");
        }

        d.setStatus("COMPLETED");
        return map(repository.save(d));
    }

    @Override
    public void deleteDispatchNote(Long dispatchId) {
        DispatchNote d = get(dispatchId);

        if ("COMPLETED".equalsIgnoreCase(d.getStatus())) {
            throw new IllegalStateException("Completed dispatch cannot be deleted");
        }

        repository.delete(d);
    }

    private DispatchNote get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dispatch not found"));
    }

    private DispatchNoteResponseDto map(DispatchNote d) {
        return DispatchNoteResponseDto.builder()
                .dispatchId(d.getDispatchId())
                .salesOrderId(d.getSalesOrderId())
                .dispatchDate(d.getDispatchDate())
                .vehicleNo(d.getVehicleNo())
                .driverName(d.getDriverName())
                .remarks(d.getRemarks())
                .status(d.getStatus())
                .build();
    }
}