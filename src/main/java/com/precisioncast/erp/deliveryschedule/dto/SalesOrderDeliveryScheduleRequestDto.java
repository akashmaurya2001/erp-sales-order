package com.precisioncast.erp.deliveryschedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderDeliveryScheduleRequestDto {

    @NotNull(message = "Sales order id is required")
    private Long salesOrderId;

    @NotNull(message = "Delivery date is required")
    private LocalDate deliveryDate;

    private BigDecimal quantity;

    private String status;
}
