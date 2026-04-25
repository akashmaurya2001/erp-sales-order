package com.precisioncast.erp.dispatchnote.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "dispatch_note")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispatchNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dispatch_id")
    private Long dispatchId;

    @Column(name = "sales_order_id")
    private Long salesOrderId;

    @Column(name = "dispatch_date", nullable = false)
    private LocalDate dispatchDate;

    @Column(name = "vehicle_no", length = 100)
    private String vehicleNo;

    @Column(name = "driver_name", length = 150)
    private String driverName;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "status")
    private String status;
}
