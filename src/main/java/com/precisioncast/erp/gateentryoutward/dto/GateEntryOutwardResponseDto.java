package com.precisioncast.erp.gateentryoutward.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GateEntryOutwardResponseDto {

    private Long outwardId;
    private String uuid;
    private String gateEntryNumber;
    private LocalDateTime entryDateTime;
    private Long vehicleId;
    private Long driverId;
    private Long customerId;
    private String soNumber;
    private String dcNumber;
    private String transportName;
    private String remarks;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
