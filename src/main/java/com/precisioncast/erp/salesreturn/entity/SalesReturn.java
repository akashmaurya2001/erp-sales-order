package com.precisioncast.erp.salesreturn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sales_return")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_id")
    private Long returnId;

    @Column(name = "invoice_id", nullable = false)
    private Long invoiceId;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @Column(name = "reason")
    private String reason;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;
}
