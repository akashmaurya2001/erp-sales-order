package com.precisioncast.erp.customerpricelist.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPriceListResponseDto {

    private Long priceListId;
    private Long customerId;
    private Long itemId;
    private BigDecimal specialRate;
    private LocalDate validFrom;
    private LocalDate validTo;
}