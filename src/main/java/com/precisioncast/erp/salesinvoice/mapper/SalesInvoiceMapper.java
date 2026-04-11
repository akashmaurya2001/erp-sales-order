package com.precisioncast.erp.salesinvoice.mapper;

import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceRequestDto;
import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceResponseDto;
import com.precisioncast.erp.salesinvoice.entity.SalesInvoice;
import org.springframework.stereotype.Component;

@Component
public class SalesInvoiceMapper {

    public SalesInvoice toEntity(SalesInvoiceRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        return SalesInvoice.builder()
                .salesOrderId(requestDto.getSalesOrderId())
                .invoiceDate(requestDto.getInvoiceDate())
                .totalAmount(requestDto.getTotalAmount())
                .build();
    }

    public SalesInvoiceResponseDto toResponseDto(SalesInvoice salesInvoice) {
        if (salesInvoice == null) {
            return null;
        }

        return SalesInvoiceResponseDto.builder()
                .invoiceId(salesInvoice.getInvoiceId())
                .salesOrderId(salesInvoice.getSalesOrderId())
                .invoiceDate(salesInvoice.getInvoiceDate())
                .totalAmount(salesInvoice.getTotalAmount())
                .build();
    }
}
