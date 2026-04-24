package com.precisioncast.erp.deliveryschedule.service;

import com.precisioncast.erp.deliveryschedule.dto.SalesOrderDeliveryScheduleRequestDto;
import com.precisioncast.erp.deliveryschedule.dto.SalesOrderDeliveryScheduleResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface SalesOrderDeliveryScheduleService {

    SalesOrderDeliveryScheduleResponseDto createSchedule(Long salesOrderId, SalesOrderDeliveryScheduleRequestDto requestDto);

    SalesOrderDeliveryScheduleResponseDto reschedule(Long scheduleId, LocalDate deliveryDate);

    List<SalesOrderDeliveryScheduleResponseDto> getSchedulesBySalesOrderId(Long salesOrderId);

    SalesOrderDeliveryScheduleResponseDto getScheduleById(Long scheduleId);

    List<SalesOrderDeliveryScheduleResponseDto> getPendingDeliveries();

    SalesOrderDeliveryScheduleResponseDto markDispatched(Long scheduleId);

    SalesOrderDeliveryScheduleResponseDto markDelivered(Long scheduleId);

    void deleteSchedule(Long scheduleId);
}