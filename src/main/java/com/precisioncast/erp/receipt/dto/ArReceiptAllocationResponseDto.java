package com.precisioncast.erp.receipt.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArReceiptAllocationResponseDto {

    private Long receiptAllocationId;
    private Long arReceiptId;
    private Long arInvoiceId;
    private BigDecimal allocatedAmount;
    private LocalDateTime allocatedAt;
}
