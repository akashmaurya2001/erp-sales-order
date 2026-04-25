package com.precisioncast.erp.deliverychallan.service.impl;

import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanResponseDto;
import com.precisioncast.erp.deliverychallan.entity.DeliveryChallan;
import com.precisioncast.erp.deliverychallan.repository.DeliveryChallanRepository;
import com.precisioncast.erp.deliverychallan.service.DeliveryChallanService;
import com.precisioncast.erp.dispatchnote.entity.DispatchNote;
import com.precisioncast.erp.dispatchnote.repository.DispatchNoteRepository;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.repository.SalesOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryChallanServiceImpl implements DeliveryChallanService {

    private final DeliveryChallanRepository repository;
    private final DispatchNoteRepository dispatchRepository;
    private final SalesOrderRepository salesOrderRepository;

    @Override
    public DeliveryChallanResponseDto createChallan(Long salesOrderId, Long dispatchId) {

        SalesOrder order = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Sales order not found with id: " + salesOrderId));

        DispatchNote dispatch = dispatchRepository.findById(dispatchId)
                .orElseThrow(() -> new EntityNotFoundException("Dispatch not found with id: " + dispatchId));

        if (!dispatch.getSalesOrderId().equals(order.getSalesOrderId())) {
            throw new IllegalStateException("Dispatch does not belong to given sales order");
        }

        if (!"OUT_FOR_DELIVERY".equalsIgnoreCase(dispatch.getStatus())) {
            throw new IllegalStateException("Challan allowed only when dispatch is OUT_FOR_DELIVERY");
        }

        repository.findByDispatchId(dispatchId).ifPresent(c -> {
            throw new IllegalStateException("Challan already exists for this dispatch");
        });

        DeliveryChallan challan = new DeliveryChallan();
        challan.setDispatchId(dispatchId);
        challan.setChallanDate(LocalDate.now());
        challan.setTotalQuantity(BigDecimal.ZERO);
        challan.setRemarks("Delivery challan created");

        DeliveryChallan saved = repository.save(challan);
        return map(saved);
    }

    @Override
    public DeliveryChallanResponseDto getChallanById(Long challanId) {
        return map(get(challanId));
    }

    @Override
    public List<DeliveryChallanResponseDto> getAllChallans() {
        List<DeliveryChallanResponseDto> responseList = new ArrayList<>();

        for (DeliveryChallan challan : repository.findAll()) {
            responseList.add(map(challan));
        }

        return responseList;
    }

    @Override
    public DeliveryChallanResponseDto completeChallan(Long challanId) {
        DeliveryChallan challan = get(challanId);
        return map(challan);
    }

    @Override
    public void deleteChallan(Long challanId) {
        DeliveryChallan challan = get(challanId);
        repository.delete(challan);
    }

    private DeliveryChallan get(Long challanId) {
        return repository.findById(challanId)
                .orElseThrow(() -> new EntityNotFoundException("Challan not found with id: " + challanId));
    }

    private DeliveryChallanResponseDto map(DeliveryChallan challan) {
        return DeliveryChallanResponseDto.builder()
                .challanId(challan.getChallanId())
                .dispatchId(challan.getDispatchId())
                .challanDate(challan.getChallanDate())
                .totalQuantity(challan.getTotalQuantity())
                .remarks(challan.getRemarks())
                .build();
    }
}