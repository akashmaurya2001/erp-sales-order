package com.precisioncast.erp.deliveryschedule.controller;

import com.precisioncast.erp.deliveryschedule.dto.SalesOrderDeliveryScheduleRequestDto;
import com.precisioncast.erp.deliveryschedule.dto.SalesOrderDeliveryScheduleResponseDto;
import com.precisioncast.erp.deliveryschedule.service.SalesOrderDeliveryScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/salesOrderDelivery")
@RequiredArgsConstructor
public class SalesOrderDeliveryScheduleController {

    private final SalesOrderDeliveryScheduleService service;

    @PostMapping("/createSchedule/{salesOrderId}")
    public ResponseEntity<SalesOrderDeliveryScheduleResponseDto> createSchedule(
            @PathVariable Long salesOrderId,
            @Valid @RequestBody SalesOrderDeliveryScheduleRequestDto requestDto
    ) {
        return ResponseEntity.ok(service.createSchedule(salesOrderId, requestDto));
    }

    @PostMapping("/reschedule/{scheduleId}/{deliveryDate}")
    public ResponseEntity<SalesOrderDeliveryScheduleResponseDto> reschedule(
            @PathVariable Long scheduleId,
            @PathVariable String deliveryDate
    ) {
        return ResponseEntity.ok(
                service.reschedule(scheduleId, LocalDate.parse(deliveryDate))
        );
    }

    @GetMapping("/list/{salesOrderId}")
    public ResponseEntity<List<SalesOrderDeliveryScheduleResponseDto>> list(
            @PathVariable Long salesOrderId
    ) {
        return ResponseEntity.ok(service.getSchedulesBySalesOrderId(salesOrderId));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<SalesOrderDeliveryScheduleResponseDto> getById(
            @PathVariable Long scheduleId
    ) {
        return ResponseEntity.ok(service.getScheduleById(scheduleId));
    }

    @GetMapping("/pendingDeliveries")
    public ResponseEntity<List<SalesOrderDeliveryScheduleResponseDto>> pending() {
        return ResponseEntity.ok(service.getPendingDeliveries());
    }

    @PatchMapping("/markDispatched/{scheduleId}")
    public ResponseEntity<SalesOrderDeliveryScheduleResponseDto> markDispatched(
            @PathVariable Long scheduleId
    ) {
        return ResponseEntity.ok(service.markDispatched(scheduleId));
    }

    @PatchMapping("/markDelivered/{scheduleId}")
    public ResponseEntity<SalesOrderDeliveryScheduleResponseDto> markDelivered(
            @PathVariable Long scheduleId
    ) {
        return ResponseEntity.ok(service.markDelivered(scheduleId));
    }

    @DeleteMapping("/delete/{scheduleId}")
    public ResponseEntity<String> delete(@PathVariable Long scheduleId) {
        service.deleteSchedule(scheduleId);
        return ResponseEntity.ok("Schedule deleted successfully");
    }
}