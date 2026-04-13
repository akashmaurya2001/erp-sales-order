package com.precisioncast.erp.gateentryoutward.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gate_entry_outward_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GateEntryOutwardItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outwardItemId")
    private Long outwardItemId;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "outwardId", nullable = false)
    private Long outwardId;

    @Column(name = "itemCode")
    private String itemCode;

    @Column(name = "itemName")
    private String itemName;

    @Column(name = "uom")
    private String uom;

    @Column(name = "qty", precision = 18, scale = 3)
    private BigDecimal qty;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "createdAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
