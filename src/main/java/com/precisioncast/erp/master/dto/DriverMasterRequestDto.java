package com.precisioncast.erp.master.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverMasterRequestDto {

    @NotBlank(message = "Driver name is required")
    private String driverName;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String address;
}
