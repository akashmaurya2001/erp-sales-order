package com.precisioncast.erp.transportchallan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transport_challan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportChallan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challanId")
    private Long challanId;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "challanNumber", nullable = false)
    private String challanNumber;

    @Column(name = "vehicleId")
    private Long vehicleId;

    @Column(name = "driverId")
    private Long driverId;

    @Column(name = "sourceLocation")
    private String sourceLocation;

    @Column(name = "destinationLocation")
    private String destinationLocation;

    @Column(name = "challanDate")
    private LocalDate challanDate;

    @Column(name = "totalWeight", precision = 18, scale = 2)
    private BigDecimal totalWeight;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "createdAt", insertable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}
