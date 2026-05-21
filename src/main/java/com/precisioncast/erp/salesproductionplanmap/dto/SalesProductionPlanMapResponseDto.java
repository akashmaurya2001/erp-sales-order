package com.precisioncast.erp.salesproductionplanmap.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesProductionPlanMapResponseDto {

    private Long id;

    private String uuid;

    private Long salesOrderId;

    private Long planId;

    private LocalDateTime createdAt;
}
