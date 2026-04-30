package com.precisioncast.erp.gateentryoutward.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GateEntryOutwardItemResponseDto {

    private Long outwardItemId;
    private Long gateEntryOutwardId;

    private Long itemId;
    private BigDecimal quantity;

    private String batchUuid;
    private String status;
    private Long reasonId;
}
