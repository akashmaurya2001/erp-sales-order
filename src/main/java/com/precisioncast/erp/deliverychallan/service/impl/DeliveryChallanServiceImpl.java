package com.precisioncast.erp.deliverychallan.service.impl;

import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanRequestDto;
import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanResponseDto;
import com.precisioncast.erp.deliverychallan.entity.DeliveryChallan;
import com.precisioncast.erp.deliverychallan.repository.DeliveryChallanRepository;
import com.precisioncast.erp.deliverychallan.service.DeliveryChallanService;
import com.precisioncast.erp.dispatchnote.entity.DispatchNote;
import com.precisioncast.erp.dispatchnote.repository.DispatchNoteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryChallanServiceImpl implements DeliveryChallanService {

    private final DeliveryChallanRepository deliveryChallanRepository;
    private final DispatchNoteRepository dispatchNoteRepository;

    @Override
    public DeliveryChallanResponseDto createDeliveryChallan(DeliveryChallanRequestDto requestDto) {

        DispatchNote dispatchNote = dispatchNoteRepository.findById(requestDto.getDispatchId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Dispatch note not found with id: " + requestDto.getDispatchId()
                ));

        DeliveryChallan deliveryChallan = new DeliveryChallan();
        deliveryChallan.setDispatchId(dispatchNote.getDispatchId());
        deliveryChallan.setChallanDate(requestDto.getChallanDate());
        deliveryChallan.setTotalQuantity(requestDto.getTotalQuantity());
        deliveryChallan.setRemarks(requestDto.getRemarks());

        DeliveryChallan saved = deliveryChallanRepository.save(deliveryChallan);

        return mapToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryChallanResponseDto> getAllDeliveryChallans() {
        List<DeliveryChallan> challans = deliveryChallanRepository.findAll();
        List<DeliveryChallanResponseDto> responseDtos = new ArrayList<>();

        for (DeliveryChallan challan : challans) {
            responseDtos.add(mapToResponseDto(challan));
        }

        return responseDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryChallanResponseDto getDeliveryChallanById(Long challanId) {
        DeliveryChallan challan = deliveryChallanRepository.findById(challanId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Delivery challan not found with id: " + challanId
                ));

        return mapToResponseDto(challan);
    }

    @Override
    public void deleteDeliveryChallan(Long challanId) {
        DeliveryChallan challan = deliveryChallanRepository.findById(challanId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Delivery challan not found with id: " + challanId
                ));

        deliveryChallanRepository.delete(challan);
    }

    private DeliveryChallanResponseDto mapToResponseDto(DeliveryChallan challan) {
        return DeliveryChallanResponseDto.builder()
                .challanId(challan.getChallanId())
                .dispatchId(challan.getDispatchId())
                .challanDate(challan.getChallanDate())
                .totalQuantity(challan.getTotalQuantity())
                .remarks(challan.getRemarks())
                .build();
    }
}