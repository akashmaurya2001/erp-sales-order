package com.precisioncast.erp.master.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleMasterResponseDto {

    private Long vehicleId;
    private String uuid;
    private String vehicleNumber;
    private String vehicleType;
    private String capacity;
    private String ownerName;
    private String contactNumber;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
