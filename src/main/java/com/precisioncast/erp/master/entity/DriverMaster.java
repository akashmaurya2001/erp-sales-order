package com.precisioncast.erp.master.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "driver_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driverId")
    private Long driverId;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "driverName", nullable = false)
    private String driverName;

    @Column(name = "licenseNumber")
    private String licenseNumber;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "isActive")
    private Boolean isActive;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
}
