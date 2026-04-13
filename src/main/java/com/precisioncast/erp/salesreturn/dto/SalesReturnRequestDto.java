package com.precisioncast.erp.salesreturn.dto;

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
public class SalesReturnRequestDto {

    @NotNull(message = "Invoice id is required")
    private Long invoiceId;

    @NotNull(message = "Return date is required")
    private LocalDate returnDate;

    private String reason;

    @Valid
    @NotEmpty(message = "At least one sales return item is required")
    private List<SalesReturnItemRequestDto> items;
}
