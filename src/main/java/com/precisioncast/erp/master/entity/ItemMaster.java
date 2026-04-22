package com.precisioncast.erp.master.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "item_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemId")
    private Long itemId;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "itemCode", nullable = false, unique = true, length = 100)
    private String itemCode;

    @Column(name = "itemName", nullable = false, length = 200)
    private String itemName;

    @Column(name = "categoryId", nullable = false)
    private Long categoryId;

    @Column(name = "subcategoryId")
    private Long subcategoryId;

    @Column(name = "uomId", nullable = false)
    private Long uomId;

    @Enumerated(EnumType.STRING)
    @Column(name = "itemType", nullable = false)
    private ItemType itemType;

    @Column(name = "description")
    private String description;

    @Column(name = "isActive")
    private Boolean isActive;

    @Column(name = "createdAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}