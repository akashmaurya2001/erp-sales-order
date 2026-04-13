package com.precisioncast.erp.salesreturn.service;

import com.precisioncast.erp.salesreturn.dto.SalesReturnRequestDto;
import com.precisioncast.erp.salesreturn.dto.SalesReturnResponseDto;

import java.util.List;

public interface SalesReturnService {

    SalesReturnResponseDto createSalesReturn(SalesReturnRequestDto requestDto);

    List<SalesReturnResponseDto> getAllSalesReturns();

    SalesReturnResponseDto getSalesReturnById(Long returnId);

    void deleteSalesReturn(Long returnId);
}
