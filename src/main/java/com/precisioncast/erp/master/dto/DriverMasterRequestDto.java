package com.precisioncast.erp.master.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverMasterRequestDto {

    private String uuid;

    @NotBlank(message = "Driver name is required")
    private String driverName;

    private String licenseNumber;
    private String phoneNumber;
    private String address;
    private Boolean isActive;
}
