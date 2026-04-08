package com.precisioncast.erp.dispatchnote.service;

import com.precisioncast.erp.dispatchnote.dto.DispatchNoteRequestDto;
import com.precisioncast.erp.dispatchnote.dto.DispatchNoteResponseDto;

import java.util.List;

public interface DispatchNoteService {

    DispatchNoteResponseDto createDispatchNote(DispatchNoteRequestDto requestDto);

    List<DispatchNoteResponseDto> getAllDispatchNotes();

    DispatchNoteResponseDto getDispatchNoteById(Long id);

    void deleteDispatchNote(Long id);
}
