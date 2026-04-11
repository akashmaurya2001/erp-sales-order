package com.precisioncast.erp.dispatchnote.service.impl;

import com.precisioncast.erp.dispatchnote.dto.DispatchNoteRequestDto;
import com.precisioncast.erp.dispatchnote.dto.DispatchNoteResponseDto;
import com.precisioncast.erp.dispatchnote.entity.DispatchNote;
import com.precisioncast.erp.dispatchnote.repository.DispatchNoteRepository;
import com.precisioncast.erp.dispatchnote.service.DispatchNoteService;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.repository.SalesOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DispatchNoteServiceImpl implements DispatchNoteService {

    private final DispatchNoteRepository dispatchNoteRepository;
    private final SalesOrderRepository salesOrderRepository;

    @Override
    public DispatchNoteResponseDto createDispatchNote(DispatchNoteRequestDto requestDto) {

        SalesOrder salesOrder = salesOrderRepository.findById(requestDto.getSalesOrderId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order not found with id: " + requestDto.getSalesOrderId()
                ));

        if (!"CONFIRMED".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Dispatch note can only be created for CONFIRMED sales order");
        }

        DispatchNote dispatchNote = new DispatchNote();
        dispatchNote.setSalesOrderId(requestDto.getSalesOrderId());
        dispatchNote.setDispatchDate(requestDto.getDispatchDate());
        dispatchNote.setVehicleNo(requestDto.getVehicleNo());
        dispatchNote.setDriverName(requestDto.getDriverName());
        dispatchNote.setRemarks(requestDto.getRemarks());

        DispatchNote savedDispatchNote = dispatchNoteRepository.save(dispatchNote);

        return mapToResponseDto(savedDispatchNote);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DispatchNoteResponseDto> getAllDispatchNotes() {
        List<DispatchNote> dispatchNotes = dispatchNoteRepository.findAll();
        List<DispatchNoteResponseDto> responseDto = new ArrayList<>();

        for (DispatchNote dispatchNote : dispatchNotes) {
            responseDto.add(mapToResponseDto(dispatchNote));
        }

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public DispatchNoteResponseDto getDispatchNoteById(Long dispatchId) {
        DispatchNote dispatchNote = dispatchNoteRepository.findById(dispatchId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Dispatch note not found with id: " + dispatchId
                ));

        return mapToResponseDto(dispatchNote);
    }

    @Override
    public void deleteDispatchNote(Long dispatchId) {
        DispatchNote dispatchNote = dispatchNoteRepository.findById(dispatchId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Dispatch note not found with id: " + dispatchId
                ));

        dispatchNoteRepository.delete(dispatchNote);
    }

    private DispatchNoteResponseDto mapToResponseDto(DispatchNote dispatchNote) {
        return DispatchNoteResponseDto.builder()
                .dispatchId(dispatchNote.getDispatchId())
                .salesOrderId(dispatchNote.getSalesOrderId())
                .dispatchDate(dispatchNote.getDispatchDate())
                .vehicleNo(dispatchNote.getVehicleNo())
                .driverName(dispatchNote.getDriverName())
                .remarks(dispatchNote.getRemarks())
                .build();
    }
}