package com.precisioncast.erp.deliveryschedule.service.impl;

import com.precisioncast.erp.deliveryschedule.dto.SalesOrderDeliveryScheduleRequestDto;
import com.precisioncast.erp.deliveryschedule.dto.SalesOrderDeliveryScheduleResponseDto;
import com.precisioncast.erp.deliveryschedule.entity.SalesOrderDeliverySchedule;
import com.precisioncast.erp.deliveryschedule.repository.SalesOrderDeliveryScheduleRepository;
import com.precisioncast.erp.deliveryschedule.service.SalesOrderDeliveryScheduleService;
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
public class SalesOrderDeliveryScheduleServiceImpl implements SalesOrderDeliveryScheduleService {

    private final SalesOrderDeliveryScheduleRepository repository;
    private final SalesOrderRepository salesOrderRepository;

    @Override
    public SalesOrderDeliveryScheduleResponseDto createSchedule(Long salesOrderId,
                                                                SalesOrderDeliveryScheduleRequestDto requestDto) {

        SalesOrder order = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Sales order not found"));


        if ("CANCELLED".equalsIgnoreCase(order.getOrderStatus())) {
            throw new IllegalStateException("Cannot create schedule for cancelled order");
        }

        SalesOrderDeliverySchedule schedule = new SalesOrderDeliverySchedule();
        schedule.setSalesOrderId(order.getSalesOrderId());
        schedule.setDeliveryDate(requestDto.getDeliveryDate());
        schedule.setQuantity(requestDto.getQuantity());
        schedule.setStatus("PENDING");

        return mapToDto(repository.save(schedule));
    }

    @Override
    public SalesOrderDeliveryScheduleResponseDto reschedule(Long scheduleId, LocalDate deliveryDate) {

        SalesOrderDeliverySchedule schedule = getEntity(scheduleId);


        if ("DELIVERED".equalsIgnoreCase(schedule.getStatus())) {
            throw new IllegalStateException("Delivered schedule cannot be rescheduled");
        }

        schedule.setDeliveryDate(deliveryDate);

        return mapToDto(repository.save(schedule));
    }

    @Override
    public List<SalesOrderDeliveryScheduleResponseDto> getSchedulesBySalesOrderId(Long salesOrderId) {
        return repository.findBySalesOrderId(salesOrderId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SalesOrderDeliveryScheduleResponseDto getScheduleById(Long scheduleId) {
        return mapToDto(getEntity(scheduleId));
    }

    @Override
    public List<SalesOrderDeliveryScheduleResponseDto> getPendingDeliveries() {
        return repository.findByStatusIgnoreCase("PENDING")
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SalesOrderDeliveryScheduleResponseDto markDispatched(Long scheduleId) {

        SalesOrderDeliverySchedule schedule = getEntity(scheduleId);


        if (!"PENDING".equalsIgnoreCase(schedule.getStatus())) {
            throw new IllegalStateException("Only PENDING schedules can be dispatched");
        }

        schedule.setStatus("DISPATCHED");

        return mapToDto(repository.save(schedule));
    }

    @Override
    public SalesOrderDeliveryScheduleResponseDto markDelivered(Long scheduleId) {

        SalesOrderDeliverySchedule schedule = getEntity(scheduleId);


        if (!"DISPATCHED".equalsIgnoreCase(schedule.getStatus())) {
            throw new IllegalStateException("Only DISPATCHED schedules can be delivered");
        }

        schedule.setStatus("DELIVERED");

        return mapToDto(repository.save(schedule));
    }

    @Override
    public void deleteSchedule(Long scheduleId) {

        SalesOrderDeliverySchedule schedule = getEntity(scheduleId);


        if ("DELIVERED".equalsIgnoreCase(schedule.getStatus())) {
            throw new IllegalStateException("Delivered schedule cannot be deleted");
        }

        repository.delete(schedule);
    }

    // 🔹 Helper methods

    private SalesOrderDeliverySchedule getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + id));
    }

    private SalesOrderDeliveryScheduleResponseDto mapToDto(SalesOrderDeliverySchedule s) {
        return SalesOrderDeliveryScheduleResponseDto.builder()
                .scheduleId(s.getScheduleId())
                .salesOrderId(s.getSalesOrderId())
                .deliveryDate(s.getDeliveryDate())
                .quantity(s.getQuantity())
                .status(s.getStatus())
                .build();
    }
}