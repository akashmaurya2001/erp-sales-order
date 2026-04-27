package com.precisioncast.erp.salesinvoice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "sales_order_id", nullable = false)
    private Long salesOrderId;

    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "total_amount", precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at",updatable = false,insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",updatable = false,insertable = false)
    private LocalDateTime updatedAt;


}
