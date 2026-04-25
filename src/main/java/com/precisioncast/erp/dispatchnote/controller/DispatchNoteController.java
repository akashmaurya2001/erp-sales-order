package com.precisioncast.erp.dispatchnote.controller;

import com.precisioncast.erp.dispatchnote.dto.DispatchNoteRequestDto;
import com.precisioncast.erp.dispatchnote.dto.DispatchNoteResponseDto;
import com.precisioncast.erp.dispatchnote.service.DispatchNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dispatchNote")
@RequiredArgsConstructor
public class DispatchNoteController {

    private final DispatchNoteService service;

    @PostMapping("/create/{salesOrderId}/{vehicleId}/{driverId}")
    public ResponseEntity<DispatchNoteResponseDto> create(
            @PathVariable Long salesOrderId,
            @PathVariable Long vehicleId,
            @PathVariable Long driverId,
            @RequestBody DispatchNoteRequestDto requestDto) {

        return ResponseEntity.ok(
                service.createDispatchNote(salesOrderId, vehicleId, driverId, requestDto)
        );
    }

    @GetMapping("/list")
    public ResponseEntity<List<DispatchNoteResponseDto>> list() {
        return ResponseEntity.ok(service.getAllDispatchNotes());
    }

    @GetMapping("/{dispatchId}")
    public ResponseEntity<DispatchNoteResponseDto> getById(@PathVariable Long dispatchId) {
        return ResponseEntity.ok(service.getDispatchNoteById(dispatchId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DispatchNoteResponseDto>> search(
            @RequestParam(required = false) Long customer,
            @RequestParam(required = false) Long vehicle,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {

        return ResponseEntity.ok(
                service.searchDispatchNotes(customer, vehicle, from, to)
        );
    }

    @PatchMapping("/markLoaded/{dispatchId}")
    public ResponseEntity<DispatchNoteResponseDto> markLoaded(@PathVariable Long dispatchId) {
        return ResponseEntity.ok(service.markLoaded(dispatchId));
    }

    @PatchMapping("/markOutForDelivery/{dispatchId}")
    public ResponseEntity<DispatchNoteResponseDto> markOutForDelivery(@PathVariable Long dispatchId) {
        return ResponseEntity.ok(service.markOutForDelivery(dispatchId));
    }

    @PatchMapping("/complete/{dispatchId}")
    public ResponseEntity<DispatchNoteResponseDto> complete(@PathVariable Long dispatchId) {
        return ResponseEntity.ok(service.completeDispatch(dispatchId));
    }

    @DeleteMapping("/delete/{dispatchId}")
    public ResponseEntity<String> delete(@PathVariable Long dispatchId) {
        service.deleteDispatchNote(dispatchId);
        return ResponseEntity.ok("Dispatch note deleted successfully");
    }
}