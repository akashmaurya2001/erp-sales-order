package com.precisioncast.erp.salesreturn.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReturnResponseDto {

    private Long returnId;
    private Long invoiceId;
    private LocalDate returnDate;
    private String reason;
    private BigDecimal totalAmount;
    private List<SalesReturnItemResponseDto> items;
}
