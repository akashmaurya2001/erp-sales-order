package com.precisioncast.erp.salesreturn.service;

import com.precisioncast.erp.salesreturn.dto.SalesReturnResponseDto;

import java.util.List;

public interface SalesReturnService {

    SalesReturnResponseDto createSalesReturn(Long invoiceId, Long customerId);

    List<SalesReturnResponseDto> getAllSalesReturns();

    SalesReturnResponseDto getSalesReturnById(Long salesReturnId);

    SalesReturnResponseDto approveSalesReturn(Long salesReturnId);

    SalesReturnResponseDto rejectSalesReturn(Long salesReturnId);

    void deleteSalesReturn(Long salesReturnId);

}
