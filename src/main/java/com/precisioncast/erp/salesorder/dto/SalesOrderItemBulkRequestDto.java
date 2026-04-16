package com.precisioncast.erp.salesorder.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderItemBulkRequestDto {

    @Valid
    @NotEmpty(message = "At least one sales order item is required")
    private List<SalesOrderItemRequestDto> items;
}
