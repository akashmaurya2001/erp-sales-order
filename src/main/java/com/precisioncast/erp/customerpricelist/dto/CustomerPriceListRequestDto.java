package com.precisioncast.erp.customerpricelist.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPriceListRequestDto {

    @NotNull(message = "Item id is required")
    private Long itemId;

    @NotNull(message = "Special rate is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Price cannot be negative")
    private BigDecimal specialRate;

    private LocalDate validFrom;

    private LocalDate validTo;
}