package com.precisioncast.erp.salesquotation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesQuotationRequestDto {

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotNull(message = "Quotation date is required")
    private LocalDate quotationDate;

    private LocalDate validityDate;

    @Valid
    @NotEmpty(message = "At least one quotation item is required")
    private List<SalesQuotationItemRequestDto> items;
}
