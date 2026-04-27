package com.precisioncast.erp.salesreturn.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReturnItemBulkRequestDto {

    private List<SalesReturnItemRequestDto> items;
}
