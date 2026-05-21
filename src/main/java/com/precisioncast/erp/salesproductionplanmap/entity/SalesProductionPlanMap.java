package com.precisioncast.erp.salesproductionplanmap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sales_production_plan_map")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesProductionPlanMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid", columnDefinition = "char(36)")
    private String uuid;

    @Column(name = "salesOrderId", nullable = false)
    private Long salesOrderId;

    @Column(name = "planId", nullable = false)
    private Long planId;

    @Column(name = "createdAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.uuid == null || this.uuid.isBlank())

            this.uuid = UUID.randomUUID().toString();
    }
 
}
