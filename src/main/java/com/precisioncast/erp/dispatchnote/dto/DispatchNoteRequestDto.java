package com.precisioncast.erp.dispatchnote.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispatchNoteRequestDto {

    @NotNull(message = "Sales order id is required")
    private Long salesOrderId;

    @NotNull(message = "Dispatch date is required")
    private LocalDate dispatchDate;

    private String vehicleNo;

    private String driverName;

    private String remarks;
}
