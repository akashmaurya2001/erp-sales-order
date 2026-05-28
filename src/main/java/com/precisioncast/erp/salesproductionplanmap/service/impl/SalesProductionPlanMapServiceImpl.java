package com.precisioncast.erp.salesproductionplanmap.service.impl;

import com.precisioncast.erp.salesproductionplanmap.dto.SalesProductionPlanMapRequestDto;
import com.precisioncast.erp.salesproductionplanmap.dto.SalesProductionPlanMapResponseDto;
import com.precisioncast.erp.salesproductionplanmap.entity.SalesProductionPlanMap;
import com.precisioncast.erp.salesproductionplanmap.repository.SalesProductionPlanMapRepository;
import com.precisioncast.erp.salesproductionplanmap.service.SalesProductionPlanMapService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesProductionPlanMapServiceImpl implements SalesProductionPlanMapService {

    private final SalesProductionPlanMapRepository repository;

    @Override
    public SalesProductionPlanMapResponseDto create(
            SalesProductionPlanMapRequestDto requestDto) {

        boolean exists = repository.existsBySalesOrderIdAndPlanId(
                requestDto.getSalesOrderId(),
                requestDto.getPlanId()
        );

        if (exists) {
            throw new RuntimeException(
                    "Mapping already exists"
            );
        }

        SalesProductionPlanMap entity =
                SalesProductionPlanMap.builder()
                        .salesOrderId(requestDto.getSalesOrderId())
                        .planId(requestDto.getPlanId())
                        .build();

        entity = repository.save(entity);

        return mapToResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesProductionPlanMapResponseDto> getAll() {

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SalesProductionPlanMapResponseDto getById(Long id) {

        return mapToResponse(get(id));
    }

    @Override
    @Transactional
    public SalesProductionPlanMapResponseDto update(
            Long id,
            SalesProductionPlanMapRequestDto requestDto) {

        SalesProductionPlanMap entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Map Not Found"));

        entity.setSalesOrderId(requestDto.getSalesOrderId());
        entity.setPlanId(requestDto.getPlanId());

        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesProductionPlanMapResponseDto> getBySalesOrderId(
            Long salesOrderId) {

        return repository.findBySalesOrderId(salesOrderId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesProductionPlanMapResponseDto> getByPlanId(
            Long planId) {

        return repository.findByPlanId(planId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {

        SalesProductionPlanMap entity = get(id);

        repository.delete(entity);
    }

    private SalesProductionPlanMap get(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "SalesProductionPlanMap not found with id : "
                                        + id
                        )
                );
    }

    private SalesProductionPlanMapResponseDto mapToResponse(
            SalesProductionPlanMap entity) {

        return SalesProductionPlanMapResponseDto.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .salesOrderId(entity.getSalesOrderId())
                .planId(entity.getPlanId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}