package com.precisioncast.erp.salesproductionplanmap.controller;

import com.precisioncast.erp.salesproductionplanmap.dto.SalesProductionPlanMapRequestDto;
import com.precisioncast.erp.salesproductionplanmap.dto.SalesProductionPlanMapResponseDto;
import com.precisioncast.erp.salesproductionplanmap.service.SalesProductionPlanMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salesProductionPlanMap")
@RequiredArgsConstructor
public class SalesProductionPlanMapController {

    private final SalesProductionPlanMapService service;

    @PostMapping("/create")
    public ResponseEntity<SalesProductionPlanMapResponseDto> create(
            @RequestBody SalesProductionPlanMapRequestDto requestDto) {

        return ResponseEntity.ok(service.create(requestDto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<SalesProductionPlanMapResponseDto>> getAll() {

        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesProductionPlanMapResponseDto> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/salesOrder/{salesOrderId}")
    public ResponseEntity<List<SalesProductionPlanMapResponseDto>> getBySalesOrderId(
            @PathVariable Long salesOrderId) {

        return ResponseEntity.ok(service.getBySalesOrderId(salesOrderId));
    }

    @GetMapping("/plan/{planId}")
    public ResponseEntity<List<SalesProductionPlanMapResponseDto>> getByPlanId(
            @PathVariable Long planId) {

        return ResponseEntity.ok(service.getByPlanId(planId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SalesProductionPlanMapResponseDto> update(
            @PathVariable Long id,
            @RequestBody SalesProductionPlanMapRequestDto requestDto) {

        return ResponseEntity.ok(service.update(id, requestDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok("Sales Production Plan Map Deleted Successfully");
    }
}