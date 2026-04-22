package com.precisioncast.erp.customerpricelist.controller;

import com.precisioncast.erp.customerpricelist.dto.CustomerPriceListBulkRequestDto;
import com.precisioncast.erp.customerpricelist.dto.CustomerPriceListResponseDto;
import com.precisioncast.erp.customerpricelist.service.CustomerPriceListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/customerPriceList")
@RequiredArgsConstructor
public class CustomerPriceListController {

    private final CustomerPriceListService customerPriceListService;

    @PostMapping("/create/{customerId}/{itemId}/{price}")
    public ResponseEntity<CustomerPriceListResponseDto> createPrice(
            @PathVariable Long customerId,
            @PathVariable Long itemId,
            @PathVariable BigDecimal price
    ) {
        return ResponseEntity.ok(
                customerPriceListService.createPrice(customerId, itemId, price)
        );
    }

    @PostMapping("/createBulk/{customerId}")
    public ResponseEntity<List<CustomerPriceListResponseDto>> createBulkPrice(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerPriceListBulkRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                customerPriceListService.createBulkPrice(customerId, requestDto)
        );
    }

    @GetMapping("/list/{customerId}")
    public ResponseEntity<List<CustomerPriceListResponseDto>> getCustomerPriceList(
            @PathVariable Long customerId
    ) {
        return ResponseEntity.ok(
                customerPriceListService.getCustomerPriceList(customerId)
        );
    }

    @GetMapping("/itemPrice/{customerId}/{itemId}")
    public ResponseEntity<CustomerPriceListResponseDto> getItemPrice(
            @PathVariable Long customerId,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(
                customerPriceListService.getItemPrice(customerId, itemId)
        );
    }

    @PatchMapping("/updatePrice/{priceListId}/{price}")
    public ResponseEntity<CustomerPriceListResponseDto> updatePrice(
            @PathVariable Long priceListId,
            @PathVariable BigDecimal price
    ) {
        return ResponseEntity.ok(
                customerPriceListService.updatePrice(priceListId, price)
        );
    }

    @DeleteMapping("/delete/{priceListId}")
    public ResponseEntity<String> deletePrice(
            @PathVariable Long priceListId
    ) {
        customerPriceListService.deletePrice(priceListId);
        return ResponseEntity.ok("Customer price list deleted successfully");
    }
}