package com.precisioncast.erp.gateentryoutward.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GateEntryOutwardItemBulkRequestDto {

    private List<GateEntryOutwardItemRequestDto> items;
}
