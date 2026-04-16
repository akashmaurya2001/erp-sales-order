package com.precisioncast.erp.deliveryschedule.service;

import com.precisioncast.erp.deliveryschedule.dto.SalesOrderDeliveryScheduleRequestDto;
import com.precisioncast.erp.deliveryschedule.dto.SalesOrderDeliveryScheduleResponseDto;

import java.util.List;

public interface SalesOrderDeliveryScheduleService {

    SalesOrderDeliveryScheduleResponseDto createSchedule(SalesOrderDeliveryScheduleRequestDto requestDto);

    List<SalesOrderDeliveryScheduleResponseDto> getAllSchedules();

    SalesOrderDeliveryScheduleResponseDto getScheduleById(Long scheduleId);

    List<SalesOrderDeliveryScheduleResponseDto> getSchedulesBySalesOrderId(Long salesOrderId);

    void deleteSchedule(Long scheduleId);

}
