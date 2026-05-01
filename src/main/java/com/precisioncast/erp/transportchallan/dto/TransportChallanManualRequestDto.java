package com.precisioncast.erp.transportchallan.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportChallanManualRequestDto {

    private Long vehicleId;
    private Long driverId;
    private Long destinationWarehouseId;
    private Long createdBy;

    private String challanNumber;
    private String vendor;
    private String sourceLocation;
    private String destinationLocation;
    private LocalDate challanDate;
    private BigDecimal totalWeight;
    private String remarks;
}
