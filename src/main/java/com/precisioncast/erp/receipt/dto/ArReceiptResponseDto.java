package com.precisioncast.erp.receipt.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArReceiptResponseDto {

    private Long arReceiptId;
    private String receiptNo;
    private LocalDate receiptDate;
    private Long customerId;
    private BigDecimal receiptAmount;
    private String paymentMode;
    private String referenceNo;
    private LocalDateTime createdAt;
}
