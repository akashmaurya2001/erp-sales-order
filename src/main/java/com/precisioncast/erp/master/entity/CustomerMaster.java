package com.precisioncast.erp.master.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customerId")
    private Long customerId;

    @Column(name = "customerCode", unique = true, length = 50)
    private String customerCode;

    @Column(name = "customerName", length = 150)
    private String customerName;

    @Column(name = "createdAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
