package com.precisioncast.erp.salesorder.service;

import com.precisioncast.erp.salesorder.dto.SalesOrderRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderResponseDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderUpdateRequestDto;

import java.time.LocalDate;
import java.util.List;

public interface SalesOrderService {

    SalesOrderResponseDto createSalesOrder(SalesOrderRequestDto requestDto);

    List<SalesOrderResponseDto> getAllSalesOrders();

    SalesOrderResponseDto getSalesOrderById(Long salesOrderId);

    SalesOrderResponseDto updateSalesOrder(Long salesOrderId, SalesOrderUpdateRequestDto requestDto);

    List<SalesOrderResponseDto> searchSalesOrders(
            Long customerId,
            Long itemId,
            String status,
            LocalDate from,
            LocalDate to
    );

    SalesOrderResponseDto updateSalesOrderStatus(Long salesOrderId, String status);

    SalesOrderResponseDto verifyCustomer(Long salesOrderId);

    SalesOrderResponseDto markUrgent(Long salesOrderId);

    SalesOrderResponseDto markHold(Long salesOrderId);

    SalesOrderResponseDto confirmSalesOrder(Long salesOrderId);

    SalesOrderResponseDto cancelSalesOrder(Long salesOrderId);

    void deleteSalesOrder(Long salesOrderId);
}