package com.precisioncast.erp.receipt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ar_receipt_allocation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArReceiptAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receiptAllocationId")
    private Long receiptAllocationId;

    @Column(name = "arReceiptId", nullable = false)
    private Long arReceiptId;

    @Column(name = "arInvoiceId", nullable = false)
    private Long arInvoiceId;

    @Column(name = "allocatedAmount", nullable = false, precision = 18, scale = 2)
    private BigDecimal allocatedAmount;

    @Column(name = "allocatedAt", insertable = false, updatable = false)
    private LocalDateTime allocatedAt;
}
