package com.precisioncast.erp.gateentryoutward.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GateEntryOutwardItemRequestDto {

    private Long itemId;
    private BigDecimal quantity;
    private String batchUuid;
}
