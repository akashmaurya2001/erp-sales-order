package com.precisioncast.erp.master.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemMasterBulkRequestDto {

    @Valid
    @NotEmpty(message = "At least one item is required")
    private List<ItemMasterRequestDto> items;
}
