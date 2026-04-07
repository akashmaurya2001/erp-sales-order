package com.precisioncast.erp.master.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
