package com.precisioncast.erp.transportchallan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportChallanRequestDto {

    private String uuid;

    @NotBlank(message = "Challan number is required")
    private String challanNumber;

    private Long vehicleId;
    private Long driverId;
    private String sourceLocation;
    private String destinationLocation;

    @NotNull(message = "Challan date is required")
    private LocalDate challanDate;

    private BigDecimal totalWeight;
    private String remarks;
}
