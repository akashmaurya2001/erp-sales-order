package com.precisioncast.erp.salesproductionplanmap.service;

import com.precisioncast.erp.salesproductionplanmap.dto.SalesProductionPlanMapRequestDto;
import com.precisioncast.erp.salesproductionplanmap.dto.SalesProductionPlanMapResponseDto;

import java.util.List;

public interface SalesProductionPlanMapService {

    SalesProductionPlanMapResponseDto create(
            SalesProductionPlanMapRequestDto requestDto
    );

    List<SalesProductionPlanMapResponseDto> getAll();

    SalesProductionPlanMapResponseDto getById(Long id);

    SalesProductionPlanMapResponseDto update(Long id, SalesProductionPlanMapRequestDto requestDto);

    List<SalesProductionPlanMapResponseDto> getBySalesOrderId(Long salesOrderId);

    List<SalesProductionPlanMapResponseDto> getByPlanId(Long planId);

    void delete(Long id);
}
