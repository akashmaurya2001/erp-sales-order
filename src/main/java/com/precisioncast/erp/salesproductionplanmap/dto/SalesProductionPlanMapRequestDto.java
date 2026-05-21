package com.precisioncast.erp.salesproductionplanmap.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesProductionPlanMapRequestDto {

    private Long salesOrderId;

    private Long planId;
}
