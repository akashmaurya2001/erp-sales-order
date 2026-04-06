package com.precisioncast.erp.salesorder.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderResponseDto {

    private Long id;
    private Long customerId;
    private LocalDate orderDate;
    private String orderStatus;
    private BigDecimal totalAmount;
    private String remarks;
    private List<SalesOrderItemResponseDto> items;
}