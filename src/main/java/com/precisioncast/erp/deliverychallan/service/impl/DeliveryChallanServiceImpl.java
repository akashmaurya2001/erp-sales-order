package com.precisioncast.erp.deliverychallan.service.impl;

import com.precisioncast.erp.common.exception.InvalidOperationException;
import com.precisioncast.erp.common.exception.ResourceNotFoundException;
import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanRequestDto;
import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanResponseDto;
import com.precisioncast.erp.deliverychallan.entity.DeliveryChallan;
import com.precisioncast.erp.deliverychallan.mapper.DeliveryChallanMapper;
import com.precisioncast.erp.deliverychallan.repository.DeliveryChallanRepository;
import com.precisioncast.erp.deliverychallan.service.DeliveryChallanService;
import com.precisioncast.erp.dispatchnote.repository.DispatchNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryChallanServiceImpl implements DeliveryChallanService {

    private final DeliveryChallanRepository deliveryChallanRepository;
    private final DeliveryChallanMapper deliveryChallanMapper;
    private final DispatchNoteRepository dispatchNoteRepository;

    @Override
    public DeliveryChallanResponseDto createDeliveryChallan(DeliveryChallanRequestDto requestDto) {

        if (!dispatchNoteRepository.existsById(requestDto.getDispatchId())) {
            throw new ResourceNotFoundException("Dispatch Note Not Found with id: " + requestDto.getDispatchId());
        }

        if (deliveryChallanRepository.existsByDispatchId(requestDto.getDispatchId())) {
            throw new InvalidOperationException(
                    "Delivery challan Already Exists for dispatch id: " + requestDto.getDispatchId()
            );
        }

        DeliveryChallan deliveryChallan = deliveryChallanMapper.toEntity(requestDto);

        DeliveryChallan savedDeliveryChallan = deliveryChallanRepository.save(deliveryChallan);

        return  deliveryChallanMapper.toResponseDto(savedDeliveryChallan);
    }

    @Override
    public List<DeliveryChallanResponseDto> getAllDeliveryChallans() {
        return deliveryChallanRepository.findAll()
                .stream()
                .map(deliveryChallanMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryChallanResponseDto getDeliveryChallanById(Long id) {
        DeliveryChallan deliveryChallan = deliveryChallanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found with id: " + id));

        return deliveryChallanMapper.toResponseDto(deliveryChallan);
    }

    @Override
    public void deleteDeliveryChallan(Long id) {
        DeliveryChallan deliveryChallan = deliveryChallanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found with id: " + id));

        deliveryChallanRepository.delete(deliveryChallan);
    }
}
