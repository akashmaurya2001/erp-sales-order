package com.precisioncast.erp.salesquotation.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesQuotationItemResponseDto {

    private Long itemId;
    private Long productId;
    private BigDecimal quantity;
    private BigDecimal rate;
    private BigDecimal amount;

}
