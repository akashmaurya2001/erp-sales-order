package com.precisioncast.erp.salesquotation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sales_quotation_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesQuotationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id", nullable = false)
    private SalesQuotation salesQuotation;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "rate", precision = 12, scale = 2)
    private BigDecimal rate;

    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;
}
