package com.precisioncast.erp.deliverychallan.controller;

import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanRequestDto;
import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanResponseDto;
import com.precisioncast.erp.deliverychallan.service.DeliveryChallanService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/delivery-challans")
@RequiredArgsConstructor
public class DeliveryChallanController {

    private final DeliveryChallanService deliveryChallanService;

    @PostMapping
    public ResponseEntity<DeliveryChallanResponseDto> createDeliveryChallan(
            @Valid @RequestBody DeliveryChallanRequestDto requestDto){
        DeliveryChallanResponseDto response =  deliveryChallanService.createDeliveryChallan(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryChallanResponseDto>> getAllDeliveryChallans(){
        List<DeliveryChallanResponseDto> response =  deliveryChallanService.getAllDeliveryChallans();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryChallanResponseDto> getDeliveryChallanById(@PathVariable Long id){
        DeliveryChallanResponseDto response =  deliveryChallanService.getDeliveryChallanById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDeliveryChallanById(@PathVariable Long id){
        deliveryChallanService.deleteDeliveryChallan(id);
        return ResponseEntity.ok("Delivery challan deleted successfully with id: " + id);
    }

}
