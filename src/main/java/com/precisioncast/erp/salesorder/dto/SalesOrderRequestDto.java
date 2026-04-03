package com.precisioncast.erp.salesorder.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderRequestDto {

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @NotNull(message = "Order date is required")
    private LocalDate orderDate;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    private String customerCode;
    private String customerEmail;
    private String customerPhone;
    private String billingAddress;
    private String shippingAddress;

    @DecimalMin(value = "0.00", message = "Tax amount cannot be negative")
    private BigDecimal taxAmount;

    @DecimalMin(value = "0.00", message = "Discount amount cannot be negative")
    private BigDecimal discountAmount;

    private String remarks;

    @Valid
    @NotEmpty(message = "At least one sales order item is required")
    private List<SalesOrderItemRequestDto> items;
}