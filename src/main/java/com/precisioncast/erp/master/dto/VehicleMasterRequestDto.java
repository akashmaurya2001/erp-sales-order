package com.precisioncast.erp.master.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleMasterRequestDto {

    private String uuid;

    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;

    private String vehicleType;
    private String capacity;
    private String ownerName;
    private String contactNumber;
    private Boolean isActive;
}
