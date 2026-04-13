package com.precisioncast.erp.gateentryoutward.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GateEntryOutwardItemResponseDto {

    private Long outwardItemId;
    private String uuid;
    private Long outwardId;
    private String itemCode;
    private String itemName;
    private String uom;
    private BigDecimal qty;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
