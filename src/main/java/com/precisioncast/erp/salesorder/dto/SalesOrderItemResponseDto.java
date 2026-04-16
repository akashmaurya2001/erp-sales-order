package com.precisioncast.erp.salesorder.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderItemResponseDto {

    private Long itemId;
    private Long productId;
    private BigDecimal quantity;
    private BigDecimal rate;
    private BigDecimal amount;
    private Boolean cancelled;


}
