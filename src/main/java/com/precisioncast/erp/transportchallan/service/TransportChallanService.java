package com.precisioncast.erp.transportchallan.service;

import com.precisioncast.erp.transportchallan.dto.TransportChallanRequestDto;
import com.precisioncast.erp.transportchallan.dto.TransportChallanResponseDto;

import java.util.List;

public interface TransportChallanService {

    TransportChallanResponseDto createTransportChallan(TransportChallanRequestDto requestDto);

    List<TransportChallanResponseDto> getAllTransportChallans();

    TransportChallanResponseDto getTransportChallanById(Long challanId);

    void deleteTransportChallan(Long challanId);
}
