package com.precisioncast.erp.transportchallan.service;

import com.precisioncast.erp.transportchallan.dto.TransportChallanManualRequestDto;
import com.precisioncast.erp.transportchallan.dto.TransportChallanRequestDto;
import com.precisioncast.erp.transportchallan.dto.TransportChallanResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface TransportChallanService {

    TransportChallanResponseDto create(Long vehicleId, Long driverId, Long destinationWarehouseId, Long createdBy, TransportChallanRequestDto requestDto);

    TransportChallanResponseDto createManual(TransportChallanManualRequestDto dto);

    List<TransportChallanResponseDto> getAll();

    TransportChallanResponseDto getById(Long transportChallanId);

    List<TransportChallanResponseDto> search(String vendor, Long vehicle, Long driver, String status, LocalDate from, LocalDate to);

    TransportChallanResponseDto update(Long transportChallanId, TransportChallanRequestDto dto);

    TransportChallanResponseDto updateStatus(Long transportChallanId, String status);

    TransportChallanResponseDto hold(Long transportChallanId);

    TransportChallanResponseDto release(Long transportChallanId);

    TransportChallanResponseDto cancel(Long transportChallanId);

    void delete(Long transportChallanId);

    byte[] export(Long transportChallanId);

}