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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesOrderDeliveryScheduleServiceImpl implements SalesOrderDeliveryScheduleService {

    private final SalesOrderDeliveryScheduleRepository scheduleRepository;
    private final SalesOrderRepository salesOrderRepository;

    @Override
    public SalesOrderDeliveryScheduleResponseDto createSchedule(SalesOrderDeliveryScheduleRequestDto requestDto) {

        SalesOrder salesOrder = salesOrderRepository.findById(requestDto.getSalesOrderId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order not found with id: " + requestDto.getSalesOrderId()
                ));

        SalesOrderDeliverySchedule schedule = new SalesOrderDeliverySchedule();
        schedule.setSalesOrderId(salesOrder.getSalesOrderId());
        schedule.setDeliveryDate(requestDto.getDeliveryDate());
        schedule.setQuantity(requestDto.getQuantity());
        schedule.setStatus(
                requestDto.getStatus() != null && !requestDto.getStatus().isBlank()
                        ? requestDto.getStatus()
                        : "PENDING"
        );

        SalesOrderDeliverySchedule saved = scheduleRepository.save(schedule);
        return mapToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDeliveryScheduleResponseDto> getAllSchedules() {
        List<SalesOrderDeliveryScheduleResponseDto> responseDto = new ArrayList<>();

        for (SalesOrderDeliverySchedule schedule : scheduleRepository.findAll()) {
            responseDto.add(mapToResponseDto(schedule));
        }

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public SalesOrderDeliveryScheduleResponseDto getScheduleById(Long scheduleId) {
        SalesOrderDeliverySchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Delivery schedule not found with id: " + scheduleId
                ));

        return mapToResponseDto(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDeliveryScheduleResponseDto> getSchedulesBySalesOrderId(Long salesOrderId) {
        List<SalesOrderDeliveryScheduleResponseDto> responseDto = new ArrayList<>();

        for (SalesOrderDeliverySchedule schedule : scheduleRepository.findBySalesOrderId(salesOrderId)) {
            responseDto.add(mapToResponseDto(schedule));
        }

        return responseDto;
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        SalesOrderDeliverySchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Delivery schedule not found with id: " + scheduleId
                ));

        scheduleRepository.delete(schedule);
    }

    private SalesOrderDeliveryScheduleResponseDto mapToResponseDto(SalesOrderDeliverySchedule schedule) {
        return SalesOrderDeliveryScheduleResponseDto.builder()
                .scheduleId(schedule.getScheduleId())
                .salesOrderId(schedule.getSalesOrderId())
                .deliveryDate(schedule.getDeliveryDate())
                .quantity(schedule.getQuantity())
                .status(schedule.getStatus())
                .build();
    }
}
