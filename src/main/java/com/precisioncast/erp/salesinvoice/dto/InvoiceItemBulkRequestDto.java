package com.precisioncast.erp.salesinvoice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItemBulkRequestDto {

    private List<InvoiceItemRequestDto> items;
}
