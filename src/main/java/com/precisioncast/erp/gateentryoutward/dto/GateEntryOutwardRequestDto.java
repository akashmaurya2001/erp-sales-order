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

    private Long customerId;

    private String gateEntryNumber;

    private String soNumber;

    private String dcNumber;

    private String transportName;

    private String remarks;

}
