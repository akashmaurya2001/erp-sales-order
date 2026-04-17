package com.precisioncast.erp.deliveryschedule.controller;

import com.precisioncast.erp.deliveryschedule.dto.SalesOrderDeliveryScheduleRequestDto;
import com.precisioncast.erp.deliveryschedule.dto.SalesOrderDeliveryScheduleResponseDto;
import com.precisioncast.erp.deliveryschedule.service.SalesOrderDeliveryScheduleService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/delivery-schedules")
@RequiredArgsConstructor
public class SalesOrderDeliveryScheduleController {

    private final SalesOrderDeliveryScheduleService deliveryScheduleService;

    @PostMapping
    public ResponseEntity<SalesOrderDeliveryScheduleResponseDto> createSchedule(
            @Valid @RequestBody SalesOrderDeliveryScheduleRequestDto requestDto) {
        return ResponseEntity.ok(deliveryScheduleService.createSchedule(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<SalesOrderDeliveryScheduleResponseDto>> getAllSchedules() {
        return ResponseEntity.ok(deliveryScheduleService.getAllSchedules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrderDeliveryScheduleResponseDto> getScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryScheduleService.getScheduleById(id));
    }

    @GetMapping("/sales-order/{salesOrderId}")
    public ResponseEntity<List<SalesOrderDeliveryScheduleResponseDto>> getSchedulesBySalesOrderId(
            @PathVariable Long salesOrderId) {
        return ResponseEntity.ok(deliveryScheduleService.getSchedulesBySalesOrderId(salesOrderId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id) {
        deliveryScheduleService.deleteSchedule(id);
        return ResponseEntity.ok("Delivery schedule deleted successfully");
    }
}