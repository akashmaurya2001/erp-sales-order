package com.precisioncast.erp.dispatchnote.controller;

import com.precisioncast.erp.dispatchnote.dto.DispatchNoteRequestDto;
import com.precisioncast.erp.dispatchnote.dto.DispatchNoteResponseDto;
import com.precisioncast.erp.dispatchnote.service.DispatchNoteService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/dispatch-notes")
@RequiredArgsConstructor
public class DispatchNoteController{

    private final DispatchNoteService dispatchNoteService;

    @PostMapping
    public ResponseEntity<DispatchNoteResponseDto> createDispatchNote(
           @Valid @RequestBody DispatchNoteRequestDto requestDto){
        DispatchNoteResponseDto response = dispatchNoteService.createDispatchNote(requestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DispatchNoteResponseDto>> getAllDispatchNotes() {
        List<DispatchNoteResponseDto> response = dispatchNoteService.getAllDispatchNotes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DispatchNoteResponseDto> getDispatchNote(@PathVariable Long id){
        DispatchNoteResponseDto response = dispatchNoteService.getDispatchNoteById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDispatchNote(@PathVariable Long id){
        dispatchNoteService.deleteDispatchNote(id);
        return ResponseEntity.ok("Dispatch note deleted successfully with id " + id);
    }
}
