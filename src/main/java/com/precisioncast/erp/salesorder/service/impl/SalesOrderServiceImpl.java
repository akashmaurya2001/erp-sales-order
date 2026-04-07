package com.precisioncast.erp.salesorder.service.impl;

import com.precisioncast.erp.common.exception.ResourceNotFoundException;
import com.precisioncast.erp.master.Repository.CustomerMasterRepository;
import com.precisioncast.erp.master.Repository.ItemMasterRepository;
import com.precisioncast.erp.salesorder.dto.SalesOrderRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderResponseDto;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.entity.SalesOrderItem;
import com.precisioncast.erp.salesorder.mapper.SalesOrderMapper;
import com.precisioncast.erp.salesorder.repository.SalesOrderRepository;
import com.precisioncast.erp.salesorder.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderMapper salesOrderMapper;
    private final CustomerMasterRepository customerMasterRepository;
    private final ItemMasterRepository itemMasterRepository;

    @Override
    public SalesOrderResponseDto createSalesOrder(SalesOrderRequestDto requestDto) {

        if (!customerMasterRepository.existsById(requestDto.getCustomerId())) {
            throw new ResourceNotFoundException("Customer not found with id " + requestDto.getCustomerId());
        }

        requestDto.getItems().forEach(item -> {
            if (!itemMasterRepository.existsById(item.getProductId())) {
                throw new ResourceNotFoundException("Product not found with id " + item.getProductId());
            }
        });

        SalesOrder salesOrder = salesOrderMapper.toEntity(requestDto);

        salesOrder.setOrderStatus("DRAFT");

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SalesOrderItem item : salesOrder.getItems()) {
            BigDecimal amount = item.getQuantity().multiply(item.getRate());
            item.setAmount(amount);
            totalAmount = totalAmount.add(amount);
        }

        salesOrder.setTotalAmount(totalAmount);

        SalesOrder savedSalesOrder = salesOrderRepository.save(salesOrder);

        return salesOrderMapper.toResponseDto(savedSalesOrder);
    }

    @Override
    public List<SalesOrderResponseDto> getAllSalesOrders() {
        return salesOrderRepository.findAll()
                .stream()
                .map(salesOrderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public SalesOrderResponseDto getSalesOrderById(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales order not found with id: " + id));

        return salesOrderMapper.toResponseDto(salesOrder);
    }

    @Override
    public void deleteSalesOrder(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales order not found with id: " + id));

        salesOrderRepository.delete(salesOrder);
    }
}