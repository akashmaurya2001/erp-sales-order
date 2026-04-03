package com.precisioncast.erp.salesorder.dto;

import com.precisioncast.erp.salesorder.enums.SalesOrderStatus;
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
    private String orderNumber;
    private LocalDate orderDate;
    private String customerName;
    private String customerCode;
    private String customerEmail;
    private String customerPhone;
    private String billingAddress;
    private String shippingAddress;
    private SalesOrderStatus status;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String remarks;
    private List<SalesOrderItemResponseDto> items;
}