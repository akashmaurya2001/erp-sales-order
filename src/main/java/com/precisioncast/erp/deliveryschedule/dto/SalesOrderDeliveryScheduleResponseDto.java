package com.precisioncast.erp.deliveryschedule.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderDeliveryScheduleResponseDto {

    private Long scheduleId;
    private Long salesOrderId;
    private LocalDate deliveryDate;
    private BigDecimal quantity;
    private String status;
}
