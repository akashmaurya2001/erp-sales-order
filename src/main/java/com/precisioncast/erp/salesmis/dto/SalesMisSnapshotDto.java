package com.precisioncast.erp.salesmis.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesMisSnapshotDto {

    private Long salesCount;
    private Long invoiceCount;
    private Long dispatchCount;
    private Long salesReturnCount;
    private BigDecimal revenueToday;
}
