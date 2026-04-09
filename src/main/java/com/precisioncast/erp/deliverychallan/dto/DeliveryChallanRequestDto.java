package com.precisioncast.erp.deliverychallan.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryChallanRequestDto {

    @NotNull(message = "Dispatch id is required")
    private Long dispatchId;

    @NotNull(message = "Challan date is required")
    private LocalDate challanDate;

    @NotNull(message = "Total quantity is required")
    private BigDecimal totalQuantity;

    private String remarks;
}
