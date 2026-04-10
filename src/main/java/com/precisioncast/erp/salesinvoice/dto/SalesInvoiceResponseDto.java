package com.precisioncast.erp.salesinvoice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesInvoiceResponseDto {

    private Long invoiceId;
    private Long salesOrderId;
    private LocalDate invoiceDate;
    private BigDecimal totalAmount;
    private String remarks;
}
