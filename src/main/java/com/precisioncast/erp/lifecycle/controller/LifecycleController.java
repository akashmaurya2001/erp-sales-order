package com.precisioncast.erp.lifecycle.controller;

import com.precisioncast.erp.lifecycle.service.LifecycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LifecycleController {

    private final LifecycleService service;

    @GetMapping("/api/customer/list")
    public ResponseEntity<List<Object>> getCustomerList(){
        return ResponseEntity.ok(service.getCustomerList());
    }

    @GetMapping("/api/customer/{customerId}")
    public ResponseEntity<Object> getCustomerById(@PathVariable Long customerId){
        return ResponseEntity.ok(service.getCustomerById(customerId));
    }

    @GetMapping("/api/customerOrders/{customerId}")
    public ResponseEntity<List<Object>> getCustomerOrders(@PathVariable Long customerId){
        return ResponseEntity.ok(service.getCustomerOrders(customerId));
    }

    @GetMapping("/api/customerInvoices/{customerId}")
    public ResponseEntity<List<Object>> getCustomerInvoices(@PathVariable Long customerId){
        return ResponseEntity.ok(service.getCustomerInvoices(customerId));
    }

    @GetMapping("/api/customerDispatches/{customerId}")
    public ResponseEntity<List<Object>> getCustomerDispatches(@PathVariable Long customerId){
        return ResponseEntity.ok(service.getCustomerDispatches(customerId));
    }

    @GetMapping("/api/customerReturns/{customerId}")
    public ResponseEntity<List<Object>> getCustomerReturns(@PathVariable Long customerId){
        return ResponseEntity.ok(service.getCustomerReturns(customerId));
    }

    @GetMapping("/api/item/list")
    public ResponseEntity<List<Object>> getItemList(){
        return ResponseEntity.ok(service.getItemList());
    }

    @GetMapping("/api/item/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId){
        return ResponseEntity.ok(service.getItemById(itemId));
    }

    @GetMapping("/api/itemSalesHistory/{itemId}")
    public ResponseEntity<List<Object>> getItemSalesHistory(@PathVariable Long itemId){
        return ResponseEntity.ok(service.getItemSalesHistory(itemId));
    }

    @GetMapping("/api/itemDispatchHistory/{itemId}")
    public ResponseEntity<List<Object>> getItemDispatchHistory(@PathVariable Long itemId){
        return ResponseEntity.ok(service.getItemDispatchHistory(itemId));
    }

    @GetMapping("/api/itemReturnHistory/{itemId}")
    public ResponseEntity<List<Object>> getItemReturnHistory(@PathVariable Long itemId){
        return ResponseEntity.ok(service.getItemReturnHistory(itemId));
    }
}
