package com.precisioncast.erp.master.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleMasterBulkRequestDto {

    @Valid
    @NotEmpty(message = "At least one vehicle is required")
    private List<VehicleMasterRequestDto>  vehicles;
}
