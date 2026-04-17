package com.precisioncast.erp.salesquotation.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesQuotationResponseDto {

    private Long quotationId;
    private Long customerId;
    private LocalDate quotationDate;
    private LocalDate validityDate;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SalesQuotationItemResponseDto> items;
}
