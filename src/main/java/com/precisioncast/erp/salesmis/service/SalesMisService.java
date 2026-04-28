package com.precisioncast.erp.salesmis.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SalesMisService {

    Long getSalesCount();

    Long getInvoiceCount();

    Long getDispatchCount();

    Long getSalesReturnCount();

    BigDecimal getRevenueToday();

    BigDecimal getRevenueRange(LocalDate from, LocalDate to);

    byte[] exportSalesSnapshot(Integer month, Integer year);
}
