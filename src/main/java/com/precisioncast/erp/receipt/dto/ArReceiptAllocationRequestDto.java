package com.precisioncast.erp.receipt.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArReceiptAllocationRequestDto {

    @NotNull(message = "Receipt id is required")
    private Long arReceiptId;

    @NotNull(message = "Invoice id is required")
    private Long arInvoiceId;

    @NotNull(message = "Allocated amount is required")
    @DecimalMin(value = "0.01", message = "Allocated amount must be greater than 0")
    private BigDecimal allocatedAmount;
}
