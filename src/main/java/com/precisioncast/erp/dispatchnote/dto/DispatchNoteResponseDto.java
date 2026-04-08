package com.precisioncast.erp.dispatchnote.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispatchNoteResponseDto {

    private Long dispatchId;
    private Long salesOrderId;
    private LocalDate dispatchDate;
    private String vehicleNo;
    private String driverName;
    private String remarks;
}
