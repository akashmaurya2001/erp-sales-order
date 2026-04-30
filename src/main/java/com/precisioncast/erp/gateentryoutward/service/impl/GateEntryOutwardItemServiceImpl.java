package com.precisioncast.erp.gateentryoutward.service.impl;

import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardItemBulkRequestDto;
import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardItemRequestDto;
import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardItemResponseDto;
import com.precisioncast.erp.gateentryoutward.entity.GateEntryOutwardItem;
import com.precisioncast.erp.gateentryoutward.repository.GateEntryOutwardItemRepository;
import com.precisioncast.erp.gateentryoutward.repository.GateEntryOutwardRepository;
import com.precisioncast.erp.gateentryoutward.service.GateEntryOutwardItemService;
import com.precisioncast.erp.master.repository.ItemMasterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GateEntryOutwardItemServiceImpl implements GateEntryOutwardItemService {

    private final GateEntryOutwardItemRepository repository;
    private final GateEntryOutwardRepository outwardRepository;
    private final ItemMasterRepository itemMasterRepository;

    @Override
    public GateEntryOutwardItemResponseDto addItem(Long gateEntryOutwardId, Long itemId, BigDecimal qty, String batchUuid) {

        outwardRepository.findById(gateEntryOutwardId)
                .orElseThrow(() -> new EntityNotFoundException("Gate outward entry not found with id: " + gateEntryOutwardId));

        itemMasterRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));

        validateQty(qty);

        GateEntryOutwardItem item = new GateEntryOutwardItem();
        item.setUuid(UUID.randomUUID().toString());
        item.setOutwardId(gateEntryOutwardId);
        item.setItemCode(String.valueOf(itemId));
        item.setItemName("Item-" + itemId);
        item.setUom(null);
        item.setQty(qty);
        item.setRemarks(batchUuid);

        return map(repository.save(item));
    }

    @Override
    public List<GateEntryOutwardItemResponseDto> addBulkItems(Long gateEntryOutwardId, GateEntryOutwardItemBulkRequestDto requestDto) {

        List<GateEntryOutwardItemResponseDto> responseList = new ArrayList<>();

        for (GateEntryOutwardItemRequestDto dto : requestDto.getItems()) {
            responseList.add(addItem(gateEntryOutwardId, dto.getItemId(), dto.getQuantity(), dto.getBatchUuid()));
        }

        return responseList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GateEntryOutwardItemResponseDto> getItems(Long gateEntryOutwardId) {

        outwardRepository.findById(gateEntryOutwardId)
                .orElseThrow(() -> new EntityNotFoundException("Gate outward entry not found with id: " + gateEntryOutwardId));

        List<GateEntryOutwardItemResponseDto> responseList = new ArrayList<>();

        for (GateEntryOutwardItem item : repository.findByOutwardId(gateEntryOutwardId)) {
            responseList.add(map(item));
        }

        return responseList;
    }

    @Override
    public GateEntryOutwardItemResponseDto updateQty(Long outwardItemId, BigDecimal qty) {

        validateQty(qty);

        GateEntryOutwardItem item = get(outwardItemId);
        item.setQty(qty);

        return map(repository.save(item));
    }

    @Override
    public GateEntryOutwardItemResponseDto markRejected(Long outwardItemId, Long reasonId) {

        GateEntryOutwardItem item = get(outwardItemId);
        item.setRemarks("REJECTED_REASON_ID_" + reasonId);

        return map(repository.save(item));
    }

    @Override
    public void deleteItem(Long outwardItemId) {
        repository.delete(get(outwardItemId));
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportItems(Long gateEntryOutwardId) {

        StringBuilder csv = new StringBuilder();
        csv.append("OutwardItemId,OutwardId,ItemId,Qty,Remarks\n");

        for (GateEntryOutwardItem item : repository.findByOutwardId(gateEntryOutwardId)) {
            csv.append(item.getOutwardItemId()).append(",")
                    .append(item.getOutwardId()).append(",")
                    .append(item.getItemCode()).append(",")
                    .append(item.getQty()).append(",")
                    .append(item.getRemarks()).append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private GateEntryOutwardItem get(Long outwardItemId) {
        return repository.findById(outwardItemId)
                .orElseThrow(() -> new EntityNotFoundException("Outward item not found with id: " + outwardItemId));
    }

    private void validateQty(BigDecimal qty) {
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    private GateEntryOutwardItemResponseDto map(GateEntryOutwardItem item) {
        return GateEntryOutwardItemResponseDto.builder()
                .outwardItemId(item.getOutwardItemId())
                .gateEntryOutwardId(item.getOutwardId())
                .itemId(Long.valueOf(item.getItemCode()))
                .quantity(item.getQty())
                .batchUuid(item.getRemarks())
                .status(item.getRemarks() != null && item.getRemarks().startsWith("REJECTED") ? "REJECTED" : "ACTIVE")
                .reasonId(null)
                .build();
    }
}