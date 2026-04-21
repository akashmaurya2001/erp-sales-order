package com.precisioncast.erp.customerpricelist.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPriceListRequestDto {

    @NotNull(message = "Product id is required")
    private Long productId;

}
