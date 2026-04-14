package com.precisioncast.erp.master.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicleId")
    private Long vehicleId;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "vehicleNumber", nullable = false)
    private String vehicleNumber;

    @Column(name = "vehicleType")
    private String vehicleType;

    @Column(name = "capacity")
    private String capacity;

    @Column(name = "ownerName")
    private String ownerName;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "isActive")
    private Boolean isActive;

    @Column(name = "createdAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
