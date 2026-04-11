package com.precisioncast.erp.salesinvoice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesInvoiceRequestDto {

    @NotNull(message = "Sales order id is required")
    private Long salesOrderId;

    @NotNull(message = "Invoice date is required")
    private LocalDate invoiceDate;

    private LocalDate dueDate;

    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;

}
