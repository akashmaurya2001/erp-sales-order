package com.precisioncast.erp.master.dto;

import com.precisioncast.erp.master.entity.ItemType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemMasterResponseDto {

    private Long itemId;
    private String uuid;
    private String itemCode;
    private String itemName;
    private Long categoryId;
    private Long subcategoryId;
    private Long uomId;
    private ItemType itemType;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
