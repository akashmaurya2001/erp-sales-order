package com.precisioncast.erp.receipt.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArReceiptRequestDto {

    @NotBlank(message = "Receipt number is required")
    private String receiptNo;

    @NotNull(message = "Receipt date is required")
    private LocalDate receiptDate;

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotNull(message = "Receipt amount is required")
    @DecimalMin(value = "0.01", message = "Receipt amount must be greater then 0")
    private BigDecimal receiptAmount;

    @NotBlank(message = "Payment mode is required")
    private String paymentMode;

    private String referenceNo;

}
