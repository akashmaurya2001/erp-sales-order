package com.precisioncast.erp.gateentryoutward.service;

import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardRequestDto;
import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface GateEntryOutwardService {

    GateEntryOutwardResponseDto createOutward(Long vehicleId, Long driverId, Long salesOrderId, Long createdBy, GateEntryOutwardRequestDto requestDto);

    GateEntryOutwardResponseDto createManual(GateEntryOutwardRequestDto requestDto);

    List<GateEntryOutwardResponseDto> getAll();

    GateEntryOutwardResponseDto getById(Long gateEntryOutwardId);

    List<GateEntryOutwardResponseDto> search(Long vehicleId, Long driverId, Long salesOrderId, String status, LocalDateTime from, LocalDateTime to);

    GateEntryOutwardResponseDto markCompleted(Long gateEntryOutwardId);

    GateEntryOutwardResponseDto hold(Long gateEntryOutwardId);

    GateEntryOutwardResponseDto release(Long gateEntryOutwardId);

    GateEntryOutwardResponseDto cancel(Long gateEntryOutwardId);

    void delete(Long gateEntryOutwardId);

    byte[] export(Long gateEntryOutwardId);
}
