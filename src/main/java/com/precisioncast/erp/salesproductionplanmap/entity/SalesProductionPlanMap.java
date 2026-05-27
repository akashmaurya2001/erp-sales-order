package com.precisioncast.erp.salesproductionplanmap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "sales_production_plan_map",
        indexes = {
                @Index(name = "idx_sales_order_id", columnList = "salesOrderId")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesProductionPlanMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", columnDefinition = "char(36)")
    private String uuid;

    @Column(name = "salesOrderId", nullable = false)
    private Long salesOrderId;

    @Column(name = "planId", nullable = false)
    private Long planId;

    @Column(name = "createdAt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Timestamp createdAt;

    @PrePersist
    public void prePersist() {

        if (uuid == null || uuid.isBlank()) {
            uuid = UUID.randomUUID().toString();
        }
    }
}