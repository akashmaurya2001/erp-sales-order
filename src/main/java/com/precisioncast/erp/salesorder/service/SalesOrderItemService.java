package com.precisioncast.erp.salesorder.service;

import com.precisioncast.erp.salesorder.dto.SalesOrderItemBulkRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderItemResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface SalesOrderItemService {

    SalesOrderItemResponseDto addSalesOrderItem(Long salesOrderId, Long itemId, BigDecimal qty);

    List<SalesOrderItemResponseDto> addBulkSalesOrderItem(Long salesOrderId, SalesOrderItemBulkRequestDto requestDto);

    List<SalesOrderItemResponseDto> getSalesOrderItems(Long salesOrderId);

    SalesOrderItemResponseDto updateSalesOrderItemQty(Long salesOrderItemId, BigDecimal qty);

    SalesOrderItemResponseDto cancelSalesOrderItem(Long salesOrderItemId);

    void deleteSalesOrderItem(Long salesOrderItemId);
}
