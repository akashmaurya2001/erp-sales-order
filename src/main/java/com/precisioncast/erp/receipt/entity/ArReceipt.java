package com.precisioncast.erp.receipt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "ar_receipt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "arReceiptId")
    private Long arReceiptId;

    @Column(name = "receiptNo", nullable = false, unique = true)
    private String receiptNo;

    @Column(name = "receiptDate", nullable = false)
    private LocalDate receiptDate;

    @Column(name = "customerId", nullable = false)
    private Long customerId;

    @Column(name = "receiptAmount", nullable = false, precision = 18, scale = 2)
    private BigDecimal receiptAmount;

    @Column(name = "paymentMode", nullable = false)
    private String paymentMode;

    @Column(name = "referenceNo")
    private String referenceNo;

    @Column(name = "createdAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
