package com.precisioncast.erp.salesquotation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales_quotation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesQuotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quotation_id")
    private Long quotationId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "quotation_date", nullable = false)
    private LocalDate quotationDate;

    @Column(name = "validity_date")
    private LocalDate validityDate;

    @Column(name = "total_amount", precision = 15,scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "salesQuotation", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SalesQuotationItem> items = new ArrayList<>();
}
