package com.precisioncast.erp.deliverychallan.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryChallanResponseDto {

    private Long challanId;
    private Long dispatchId;
    private LocalDate challanDate;
    private BigDecimal totalQuantity;
    private String remarks;
}
