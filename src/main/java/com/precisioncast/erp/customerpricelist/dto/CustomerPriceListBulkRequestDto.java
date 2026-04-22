package com.precisioncast.erp.customerpricelist.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPriceListBulkRequestDto {

    @Valid
    @NotEmpty(message = "At least one price list item is required")
    private List<CustomerPriceListRequestDto> items;
}
