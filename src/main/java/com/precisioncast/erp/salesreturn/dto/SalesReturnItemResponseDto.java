package com.precisioncast.erp.salesreturn.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReturnItemResponseDto {

    private Long itemId;
    private Long returnId;
    private Long productId;
    private BigDecimal quantity;
    private BigDecimal rate;
    private BigDecimal amount;
}
