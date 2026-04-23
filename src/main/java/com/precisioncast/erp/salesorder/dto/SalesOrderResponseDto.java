package com.precisioncast.erp.salesorder.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderResponseDto {

    private Long salesOrderId;
    private Long customerId;
    private LocalDate orderDate;
    private String orderStatus;
    private BigDecimal totalAmount;
    private String remarks;
    private Boolean isUrgent;
    private Boolean isOnHold;
    private Boolean customerVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SalesOrderItemResponseDto> items;

    private String message;
}