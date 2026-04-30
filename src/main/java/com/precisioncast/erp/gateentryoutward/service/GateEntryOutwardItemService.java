package com.precisioncast.erp.gateentryoutward.service;

import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardItemBulkRequestDto;
import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardItemResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface GateEntryOutwardItemService {

    GateEntryOutwardItemResponseDto addItem(Long gateEntryOutwardId, Long itemId, BigDecimal qty, String batchUuid);

    List<GateEntryOutwardItemResponseDto> addBulkItems(Long gateEntryOutwardId, GateEntryOutwardItemBulkRequestDto requestDto);

    List<GateEntryOutwardItemResponseDto> getItems(Long gateEntryOutwardId);

    GateEntryOutwardItemResponseDto updateQty(Long outwardItemId, BigDecimal qty);

    GateEntryOutwardItemResponseDto markRejected(Long outwardItemId, Long reasonId);

    void deleteItem(Long outwardItemId);

    byte[] exportItems(Long gateEntryOutwardId);
}