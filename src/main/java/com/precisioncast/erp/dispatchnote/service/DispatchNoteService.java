package com.precisioncast.erp.dispatchnote.service;

import com.precisioncast.erp.dispatchnote.dto.DispatchNoteRequestDto;
import com.precisioncast.erp.dispatchnote.dto.DispatchNoteResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface DispatchNoteService {

    DispatchNoteResponseDto createDispatchNote(Long salesOrderId, Long vehicleId, Long driverId, DispatchNoteRequestDto requestDto);

    List<DispatchNoteResponseDto> getAllDispatchNotes();

    DispatchNoteResponseDto getDispatchNoteById(Long dispatchId);

    List<DispatchNoteResponseDto> searchDispatchNotes(Long customerId, Long vehicleId, LocalDate from, LocalDate to);

    DispatchNoteResponseDto markLoaded(Long dispatchId);

    DispatchNoteResponseDto markOutForDelivery(Long dispatchId);

    DispatchNoteResponseDto completeDispatch(Long dispatchId);

    void deleteDispatchNote(Long dispatchId);
}