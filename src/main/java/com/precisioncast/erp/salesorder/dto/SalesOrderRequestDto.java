package com.precisioncast.erp.salesorder.dto;

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
public class SalesOrderRequestDto {

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotNull(message = "Order date is required")
    private LocalDate orderDate;

    private String remarks;

    @Valid
    @NotEmpty(message = "At least one sales order item is required")
    private List<SalesOrderItemRequestDto> items;
}