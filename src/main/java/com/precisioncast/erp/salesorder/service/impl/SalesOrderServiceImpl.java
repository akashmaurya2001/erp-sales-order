package com.precisioncast.erp.salesorder.service.impl;

import com.precisioncast.erp.salesorder.dto.SalesOrderRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderResponseDto;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.entity.SalesOrderItem;
import com.precisioncast.erp.salesorder.enums.SalesOrderStatus;
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

    @Override
    public SalesOrderResponseDto createSalesOrder(SalesOrderRequestDto requestDto) {
        if (salesOrderRepository.existsByOrderNumber(requestDto.getOrderNumber())) {
            throw new RuntimeException("SalesOrder already exists with order number: " + requestDto.getOrderNumber());
        }

        SalesOrder salesOrder = salesOrderMapper.toEntity(requestDto);

        salesOrder.setStatus(SalesOrderStatus.CREATED);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (SalesOrderItem item : salesOrder.getItems()) {
            BigDecimal lineTotal = item.getQuantity().multiply(item.getUnitPrice());
            item.setLineTotal(lineTotal);
            subtotal = subtotal.add(lineTotal);
        }

        BigDecimal taxAmount = requestDto.getTaxAmount() != null ? requestDto.getTaxAmount() : BigDecimal.ZERO;
        BigDecimal discountAmount = requestDto.getDiscountAmount() != null ? requestDto.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal totalAmount = subtotal.add(taxAmount).subtract(discountAmount);

        salesOrder.setSubtotal(subtotal);
        salesOrder.setTaxAmount(taxAmount);
        salesOrder.setDiscountAmount(discountAmount);
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
                .orElseThrow(() -> new RuntimeException("SalesOrder not found with id: " + id));

        return salesOrderMapper.toResponseDto(salesOrder);
    }

    @Override
    public void deleteSalesOrder(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SalesOrder not found with id: " + id));

        salesOrderRepository.delete(salesOrder);
    }
}