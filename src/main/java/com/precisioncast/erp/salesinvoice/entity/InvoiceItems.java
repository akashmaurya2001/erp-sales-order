package com.precisioncast.erp.salesinvoice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "invoice_id", nullable = false)
    private Long invoiceId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "amount")
    private BigDecimal amount;
}
