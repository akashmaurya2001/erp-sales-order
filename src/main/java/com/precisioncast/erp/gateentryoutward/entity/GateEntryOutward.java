package com.precisioncast.erp.gateentryoutward.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "gate_entry_outward")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GateEntryOutward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outwardId")
    private Long outwardId;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "gateEntryNumber", nullable = false)
    private String gateEntryNumber;

    @Column(name = "entryDateTime", nullable = false)
    private LocalDateTime entryDateTime;

    @Column(name = "vehicleId")
    private Long vehicleId;

    @Column(name = "driverId")
    private Long driverId;

    @Column(name = "customerId")
    private Long customerId;

    @Column(name = "soNumber")
    private String soNumber;

    @Column(name = "dcNumber")
    private String dcNumber;

    @Column(name = "transportName")
    private String transportName;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "status")
    private String status;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
