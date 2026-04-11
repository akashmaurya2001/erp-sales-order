package com.precisioncast.erp.salesorder.service;

import com.precisioncast.erp.salesorder.dto.SalesOrderRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderResponseDto;

import java.util.List;

public interface SalesOrderService {

    SalesOrderResponseDto createSalesOrder(SalesOrderRequestDto requestDto);

    List<SalesOrderResponseDto> getAllSalesOrders();

    SalesOrderResponseDto getSalesOrderById(Long salesOrderId);

    SalesOrderResponseDto confirmSalesOrder(Long salesOrderId);

    SalesOrderResponseDto cancelSalesOrder(Long salesOrderId);

    void deleteSalesOrder(Long salesOrderId);
}