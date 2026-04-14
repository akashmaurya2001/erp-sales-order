package com.precisioncast.erp.master.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverMasterResponseDto {

    private Long driverId;
    private String uuid;
    private String driverName;
    private String licenseNumber;
    private String phoneNumber;
    private String address;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
