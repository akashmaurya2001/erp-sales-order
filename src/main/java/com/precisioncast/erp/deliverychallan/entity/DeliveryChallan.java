package com.precisioncast.erp.deliverychallan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "delivery_challan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryChallan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challan_id")
    private Long challanId;

    @Column(name = "dispatch_id")
    private Long dispatchId;

    @Column(name = "challan_date")
    private LocalDate challanDate;

    @Column(name = "total_quantity", precision = 12, scale = 2)
    private BigDecimal totalQuantity;

    @Column(name = "remarks")
    private String remarks;
}
