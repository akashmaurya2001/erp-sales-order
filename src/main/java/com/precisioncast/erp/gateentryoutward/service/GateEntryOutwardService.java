package com.precisioncast.erp.gateentryoutward.service;

import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardRequestDto;
import com.precisioncast.erp.gateentryoutward.dto.GateEntryOutwardResponseDto;

import java.util.List;

public interface GateEntryOutwardService {

    GateEntryOutwardResponseDto createGateEntryOutward(GateEntryOutwardRequestDto requestDto);

    List<GateEntryOutwardResponseDto> getAllGateEntryOutward();

    GateEntryOutwardResponseDto getGateEntryOutwardById(Long outwardId);

    void deleteGateEntryOutward(Long outwardId);
}
