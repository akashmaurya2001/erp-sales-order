package com.precisioncast.erp.salesorder.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderUpdateRequestDto {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Order date is required")
    private LocalDate orderDate;

    private String remarks;

    @Valid
    @Size(min = 1, message = "At least one item is required")
    private List<SalesOrderItemRequestDto> items;
}
