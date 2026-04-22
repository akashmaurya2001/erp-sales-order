package com.precisioncast.erp.master.dto;

import com.precisioncast.erp.master.entity.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemMasterRequestDto {

    @NotBlank(message = "Item code is required")
    private String itemCode;

    @NotBlank(message = "item name is required")
    private String itemName;

    @NotNull(message = "Category id is required")
    private Long categoryId;

    private Long subcategoryId;

    @NotNull(message = "UOM id is required")
    private Long uomId;

    @NotNull(message = "Item type is required")
    private ItemType itemType;

    private String description;
}
