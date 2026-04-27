package com.precisioncast.erp.salesreturn.service;

import com.precisioncast.erp.salesreturn.dto.SalesReturnItemBulkRequestDto;
import com.precisioncast.erp.salesreturn.dto.SalesReturnItemResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface SalesReturnItemService {

    SalesReturnItemResponseDto addItem(Long salesReturnId, Long itemId, BigDecimal qty, Long reasonId);

    List<SalesReturnItemResponseDto> addBulkItems(Long salesReturnId, SalesReturnItemBulkRequestDto requestDto);

    List<SalesReturnItemResponseDto> getItemsByReturnId(Long salesReturnId);

    SalesReturnItemResponseDto updateQty(Long salesReturnItemId, BigDecimal qty);

    void deleteItem(Long salesReturnItemId);
}
