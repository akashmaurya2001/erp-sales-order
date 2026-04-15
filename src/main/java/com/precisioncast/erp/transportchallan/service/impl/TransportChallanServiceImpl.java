package com.precisioncast.erp.transportchallan.service.impl;

import com.precisioncast.erp.transportchallan.dto.TransportChallanRequestDto;
import com.precisioncast.erp.transportchallan.dto.TransportChallanResponseDto;
import com.precisioncast.erp.transportchallan.entity.TransportChallan;
import com.precisioncast.erp.transportchallan.repository.TransportChallanRepository;
import com.precisioncast.erp.transportchallan.service.TransportChallanService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransportChallanServiceImpl implements TransportChallanService {

    private final TransportChallanRepository transportChallanRepository;

    @Override
    public TransportChallanResponseDto createTransportChallan(TransportChallanRequestDto requestDto) {
        TransportChallan challan = new TransportChallan();
        challan.setUuid(requestDto.getUuid());
        challan.setChallanNumber(requestDto.getChallanNumber());
        challan.setVehicleId(requestDto.getVehicleId());
        challan.setDriverId(requestDto.getDriverId());
        challan.setSourceLocation(requestDto.getSourceLocation());
        challan.setDestinationLocation(requestDto.getDestinationLocation());
        challan.setChallanDate(requestDto.getChallanDate());
        challan.setTotalWeight(requestDto.getTotalWeight());
        challan.setRemarks(requestDto.getRemarks());

        TransportChallan saved = transportChallanRepository.save(challan);
        return mapToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransportChallanResponseDto> getAllTransportChallans() {
        List<TransportChallanResponseDto> responseDto = new ArrayList<>();
        for (TransportChallan challan : transportChallanRepository.findAll()) {
            responseDto.add(mapToResponseDto(challan));
        }
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public TransportChallanResponseDto getTransportChallanById(Long challanId) {
        TransportChallan challan = transportChallanRepository.findById(challanId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Transport challan not found with id: " + challanId
                ));
        return mapToResponseDto(challan);
    }

    @Override
    public void deleteTransportChallan(Long challanId) {
        TransportChallan challan = transportChallanRepository.findById(challanId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Transport challan not found with id: " + challanId
                ));
        transportChallanRepository.delete(challan);
    }

    private TransportChallanResponseDto mapToResponseDto(TransportChallan challan) {
        return TransportChallanResponseDto.builder()
                .challanId(challan.getChallanId())
                .uuid(challan.getUuid())
                .challanNumber(challan.getChallanNumber())
                .vehicleId(challan.getVehicleId())
                .driverId(challan.getDriverId())
                .sourceLocation(challan.getSourceLocation())
                .destinationLocation(challan.getDestinationLocation())
                .challanDate(challan.getChallanDate())
                .totalWeight(challan.getTotalWeight())
                .remarks(challan.getRemarks())
                .createdAt(challan.getCreatedAt())
                .updatedAt(challan.getUpdatedAt())
                .build();
    }
}