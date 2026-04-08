package com.precisioncast.erp.salesorder.service;

import com.precisioncast.erp.salesorder.dto.SalesOrderRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderResponseDto;

import java.util.List;

public interface SalesOrderService {

    SalesOrderResponseDto createSalesOrder(SalesOrderRequestDto requestDto);

    List<SalesOrderResponseDto> getAllSalesOrders();

    SalesOrderResponseDto getSalesOrderById(Long id);

    SalesOrderResponseDto confirmSalesOrder(Long id);

    SalesOrderResponseDto cancelSalesOrder(Long id);

    void deleteSalesOrder(Long id);
}