package com.precisioncast.erp.salesinvoice.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItemResponseDto {

    private Long invoiceItemId;
    private Long invoiceId;
    private Long itemId;
    private BigDecimal quantity;
    private BigDecimal rate;
    private BigDecimal amount;
}
