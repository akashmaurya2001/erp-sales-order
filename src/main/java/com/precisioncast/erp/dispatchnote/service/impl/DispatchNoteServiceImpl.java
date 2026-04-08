package com.precisioncast.erp.dispatchnote.service.impl;

import com.precisioncast.erp.common.exception.InvalidOperationException;
import com.precisioncast.erp.common.exception.ResourceNotFoundException;
import com.precisioncast.erp.dispatchnote.dto.DispatchNoteRequestDto;
import com.precisioncast.erp.dispatchnote.dto.DispatchNoteResponseDto;
import com.precisioncast.erp.dispatchnote.entity.DispatchNote;
import com.precisioncast.erp.dispatchnote.mapper.DispatchNoteMapper;
import com.precisioncast.erp.dispatchnote.repository.DispatchNoteRepository;
import com.precisioncast.erp.dispatchnote.service.DispatchNoteService;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DispatchNoteServiceImpl implements DispatchNoteService {

    private final DispatchNoteRepository dispatchNoteRepository;
    private final DispatchNoteMapper dispatchNoteMapper;
    private final SalesOrderRepository salesOrderRepository;

    @Override
    public DispatchNoteResponseDto createDispatchNote(DispatchNoteRequestDto requestDto) {

        SalesOrder salesOrder = salesOrderRepository.findById(requestDto.getSalesOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sales order not found with id: "
                        + requestDto.getSalesOrderId()));

        if (!"CONFIRMED".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new InvalidOperationException(
                    "Dispatch note can be created only for CONFIRMED sales order. Current status is: "
                            + salesOrder.getOrderStatus()
            );
        }

        DispatchNote dispatchNote = dispatchNoteMapper.toEntity(requestDto);

        DispatchNote savedDispatchNote = dispatchNoteRepository.save(dispatchNote);

        return dispatchNoteMapper.toResponseDto(savedDispatchNote);
    }

    @Override
    public List<DispatchNoteResponseDto> getAllDispatchNotes() {
        return dispatchNoteRepository.findAll()
                .stream()
                .map(dispatchNoteMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public DispatchNoteResponseDto getDispatchNoteById(Long id) {
        DispatchNote dispatchNote = dispatchNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispatch note not found with id " + id));

        return dispatchNoteMapper.toResponseDto(dispatchNote);
    }

    @Override
    public void deleteDispatchNote(Long id) {
        DispatchNote dispatchNote = dispatchNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispatch note not found with id " + id));

        dispatchNoteRepository.delete(dispatchNote);

    }
}
