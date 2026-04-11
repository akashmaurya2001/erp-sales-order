package com.precisioncast.erp.salesinvoice.service.impl;

import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceRequestDto;
import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceResponseDto;
import com.precisioncast.erp.salesinvoice.entity.SalesInvoice;
import com.precisioncast.erp.salesinvoice.repository.SalesInvoiceRepository;
import com.precisioncast.erp.salesinvoice.service.SalesInvoiceService;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.repository.SalesOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesInvoiceServiceImpl implements SalesInvoiceService {

    private final SalesInvoiceRepository salesInvoiceRepository;
    private final SalesOrderRepository salesOrderRepository;

    @Override
    public SalesInvoiceResponseDto createSalesInvoice(SalesInvoiceRequestDto requestDto) {

        SalesOrder salesOrder = salesOrderRepository.findById(requestDto.getSalesOrderId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order not found with id: " + requestDto.getSalesOrderId()
                ));

        if (!"CONFIRMED".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Invoice can only be created for CONFIRMED sales order");
        }

        SalesInvoice salesInvoice = new SalesInvoice();
        salesInvoice.setSalesOrderId(requestDto.getSalesOrderId());
        salesInvoice.setInvoiceDate(requestDto.getInvoiceDate());
        salesInvoice.setDueDate(requestDto.getDueDate());
        salesInvoice.setTotalAmount(requestDto.getTotalAmount());
        salesInvoice.setStatus("OPEN");

        SalesInvoice savedInvoice = salesInvoiceRepository.save(salesInvoice);

        return mapToResponseDto(savedInvoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesInvoiceResponseDto> getAllSalesInvoices() {
        List<SalesInvoice> invoices = salesInvoiceRepository.findAll();
        List<SalesInvoiceResponseDto> responseDtos = new ArrayList<>();

        for (SalesInvoice invoice : invoices) {
            responseDtos.add(mapToResponseDto(invoice));
        }

        return responseDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public SalesInvoiceResponseDto getSalesInvoiceById(Long invoiceId) {
        SalesInvoice invoice = salesInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales invoice not found with id: " + invoiceId
                ));

        return mapToResponseDto(invoice);
    }

    @Override
    public void deleteSalesInvoice(Long invoiceId) {
        SalesInvoice invoice = salesInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales invoice not found with id: " + invoiceId
                ));

        salesInvoiceRepository.delete(invoice);
    }

    private SalesInvoiceResponseDto mapToResponseDto(SalesInvoice invoice) {
        return SalesInvoiceResponseDto.builder()
                .invoiceId(invoice.getInvoiceId())
                .salesOrderId(invoice.getSalesOrderId())
                .invoiceDate(invoice.getInvoiceDate())
                .dueDate(invoice.getDueDate())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build();
    }
}