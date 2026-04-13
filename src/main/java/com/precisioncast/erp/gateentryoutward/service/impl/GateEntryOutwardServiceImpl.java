package com.precisioncast.erp.gateentryoutward.service.impl;

import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardItemRequestDto;
import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardItemResponseDto;
import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardRequestDto;
import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardResponseDto;
import com.precisioncast.erp.gateentryoutward.entity.GateEntryOutward;
import com.precisioncast.erp.gateentryoutward.entity.GateEntryOutwardItem;
import com.precisioncast.erp.gateentryoutward.repository.GateEntryOutwardItemRepository;
import com.precisioncast.erp.gateentryoutward.repository.GateEntryOutwardRepository;
import com.precisioncast.erp.gateentryoutward.service.GateEntryOutwardService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GateEntryOutwardServiceImpl implements GateEntryOutwardService {

    private final GateEntryOutwardRepository gateEntryOutwardRepository;
    private final GateEntryOutwardItemRepository gateEntryOutwardItemRepository;

    @Override
    public GateEntryOutwardResponseDto createGateEntryOutward(GateEntryOutwardRequestDto requestDto) {

        GateEntryOutward outward = new GateEntryOutward();
        outward.setUuid(requestDto.getUuid());
        outward.setGateEntryNumber(requestDto.getGateEntryNumber());
        outward.setEntryDateTime(requestDto.getEntryDateTime());
        outward.setVehicleId(requestDto.getVehicleId());
        outward.setDriverId(requestDto.getDriverId());
        outward.setCustomerId(requestDto.getCustomerId());
        outward.setSoNumber(requestDto.getSoNumber());
        outward.setDcNumber(requestDto.getDcNumber());
        outward.setTransportName(requestDto.getTransportName());
        outward.setRemarks(requestDto.getRemarks());
        outward.setStatus(
                requestDto.getStatus() != null && !requestDto.getStatus().isBlank()
                        ? requestDto.getStatus()
                        : "PENDING"
        );

        GateEntryOutward savedOutward = gateEntryOutwardRepository.save(outward);

        List<GateEntryOutwardItem> savedItems = new ArrayList<>();

        for (GateEntryOutwardItemRequestDto itemDto : requestDto.getItems()) {
            GateEntryOutwardItem item = new GateEntryOutwardItem();
            item.setUuid(null);
            item.setOutwardId(savedOutward.getOutwardId());
            item.setItemCode(itemDto.getItemCode());
            item.setItemName(itemDto.getItemName());
            item.setUom(itemDto.getUom());
            item.setQty(itemDto.getQty());
            item.setRemarks(itemDto.getRemarks());

            savedItems.add(gateEntryOutwardItemRepository.save(item));
        }

        return mapToResponseDto(savedOutward, savedItems);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GateEntryOutwardResponseDto> getAllGateEntryOutward() {
        List<GateEntryOutward> outwardList = gateEntryOutwardRepository.findAll();
        List<GateEntryOutwardResponseDto> responseDtos = new ArrayList<>();

        for (GateEntryOutward outward : outwardList) {
            List<GateEntryOutwardItem> items =
                    gateEntryOutwardItemRepository.findByOutwardId(outward.getOutwardId());

            responseDtos.add(mapToResponseDto(outward, items));
        }

        return responseDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public GateEntryOutwardResponseDto getGateEntryOutwardById(Long outwardId) {
        GateEntryOutward outward = gateEntryOutwardRepository.findById(outwardId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Gate entry outward not found with id: " + outwardId
                ));

        List<GateEntryOutwardItem> items =
                gateEntryOutwardItemRepository.findByOutwardId(outward.getOutwardId());

        return mapToResponseDto(outward, items);
    }

    @Override
    public void deleteGateEntryOutward(Long outwardId) {
        GateEntryOutward outward = gateEntryOutwardRepository.findById(outwardId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Gate entry outward not found with id: " + outwardId
                ));

        List<GateEntryOutwardItem> items =
                gateEntryOutwardItemRepository.findByOutwardId(outwardId);

        gateEntryOutwardItemRepository.deleteAll(items);
        gateEntryOutwardRepository.delete(outward);
    }

    private GateEntryOutwardResponseDto mapToResponseDto(
            GateEntryOutward outward,
            List<GateEntryOutwardItem> items
    ) {
        List<GateEntryOutwardItemResponseDto> itemResponseDtos = new ArrayList<>();

        for (GateEntryOutwardItem item : items) {
            GateEntryOutwardItemResponseDto itemDto = GateEntryOutwardItemResponseDto.builder()
                    .outwardItemId(item.getOutwardItemId())
                    .uuid(item.getUuid())
                    .outwardId(item.getOutwardId())
                    .itemCode(item.getItemCode())
                    .itemName(item.getItemName())
                    .uom(item.getUom())
                    .qty(item.getQty())
                    .remarks(item.getRemarks())
                    .createdAt(item.getCreatedAt())
                    .updatedAt(item.getUpdatedAt())
                    .build();

            itemResponseDtos.add(itemDto);
        }

        return GateEntryOutwardResponseDto.builder()
                .outwardId(outward.getOutwardId())
                .uuid(outward.getUuid())
                .gateEntryNumber(outward.getGateEntryNumber())
                .entryDateTime(outward.getEntryDateTime())
                .vehicleId(outward.getVehicleId())
                .driverId(outward.getDriverId())
                .customerId(outward.getCustomerId())
                .soNumber(outward.getSoNumber())
                .dcNumber(outward.getDcNumber())
                .transportName(outward.getTransportName())
                .remarks(outward.getRemarks())
                .status(outward.getStatus())
                .createdAt(outward.getCreatedAt())
                .updatedAt(outward.getUpdatedAt())
                .items(itemResponseDtos)
                .build();
    }
}