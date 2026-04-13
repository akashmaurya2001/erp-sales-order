package com.precisioncast.erp.gateentryoutward.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GateEntryOutwardRequestDto {

    private String uuid;

    @NotBlank(message = "Gate entry number is required")
    private String gateEntryNumber;

    @NotNull(message = "Entity date time is required")
    private LocalDateTime entryDateTime;

    private Long vehicleId;
    private Long driverId;
    private Long customerId;
    private String soNumber;
    private String dcNumber;
    private String transportName;
    private String remarks;
    private String status;

    @Valid
    @NotEmpty(message = "At least one outward item is required")
    private List<GateEntryOutwardItemRequestDto> items;
}
