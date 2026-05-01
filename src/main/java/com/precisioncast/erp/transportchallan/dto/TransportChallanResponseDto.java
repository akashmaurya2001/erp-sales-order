package com.precisioncast.erp.transportchallan.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportChallanResponseDto {

    private Long challanId;
    private String uuid;
    private String challanNumber;

    private Long vehicleId;
    private Long driverId;
    private Long destinationWarehouseId;

    private String vendor;
    private String sourceLocation;
    private String destinationLocation;
    private LocalDate challanDate;
    private BigDecimal totalWeight;
    private String status;
    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
}
