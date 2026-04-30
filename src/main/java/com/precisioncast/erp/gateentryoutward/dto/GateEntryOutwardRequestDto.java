package com.precisioncast.erp.gateentryoutward.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GateEntryOutwardRequestDto {

    private LocalDateTime entryDateTime;

    private String gateEntryNumber;

    private String dcNumber;

    private String transportName;

    private String remarks;

}
