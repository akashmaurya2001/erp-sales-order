package com.precisioncast.erp.dispatchnote.controller;

import com.precisioncast.erp.dispatchnote.dto.DispatchNoteRequestDto;
import com.precisioncast.erp.dispatchnote.dto.DispatchNoteResponseDto;
import com.precisioncast.erp.dispatchnote.service.DispatchNoteService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/dispatch-notes")
@RequiredArgsConstructor
public class DispatchNoteController {

    private final DispatchNoteService dispatchNoteService;

    @PostMapping
    public ResponseEntity<DispatchNoteResponseDto> createDispatchNote(
            @Valid @RequestBody DispatchNoteRequestDto requestDto) {
        return ResponseEntity.ok(dispatchNoteService.createDispatchNote(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<DispatchNoteResponseDto>> getAllDispatchNotes() {
        return ResponseEntity.ok(dispatchNoteService.getAllDispatchNotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DispatchNoteResponseDto> getDispatchNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(dispatchNoteService.getDispatchNoteById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDispatchNote(@PathVariable Long id) {
        dispatchNoteService.deleteDispatchNote(id);
        return ResponseEntity.ok("Dispatch note deleted successfully");
    }
}