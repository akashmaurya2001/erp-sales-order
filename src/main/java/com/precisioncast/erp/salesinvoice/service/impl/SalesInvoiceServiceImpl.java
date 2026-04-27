package com.precisioncast.erp.salesinvoice.service.impl;

import com.precisioncast.erp.deliverychallan.entity.DeliveryChallan;
import com.precisioncast.erp.deliverychallan.repository.DeliveryChallanRepository;
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

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesInvoiceServiceImpl implements SalesInvoiceService {

    private final SalesInvoiceRepository salesInvoiceRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final DeliveryChallanRepository deliveryChallanRepository;

    @Override
    public SalesInvoiceResponseDto createInvoice(Long salesOrderId, Long deliveryId, SalesInvoiceRequestDto requestDto) {

        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Sales order not found with id: " + salesOrderId));

        DeliveryChallan challan = deliveryChallanRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery challan not found with id: " + deliveryId));

        if (!challan.getDispatchId().equals(deliveryId) && challan.getChallanId() == null) {
            throw new IllegalStateException("Invalid delivery challan");
        }

        SalesInvoice invoice = new SalesInvoice();
        invoice.setSalesOrderId(salesOrder.getSalesOrderId());
        invoice.setInvoiceDate(requestDto.getInvoiceDate() != null ? requestDto.getInvoiceDate() : LocalDate.now());
        invoice.setDueDate(requestDto.getDueDate());
        invoice.setTotalAmount(BigDecimal.ZERO);
        invoice.setStatus("DRAFT");

        SalesInvoice saved = salesInvoiceRepository.save(invoice);
        SalesInvoice refreshed = salesInvoiceRepository.findById(saved.getInvoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + saved.getInvoiceId()));

        return mapToResponse(refreshed);
    }

    @Override
    public SalesInvoiceResponseDto createManualInvoice(SalesInvoiceRequestDto requestDto) {
        SalesInvoice invoice = new SalesInvoice();
        invoice.setSalesOrderId(null);
        invoice.setInvoiceDate(requestDto.getInvoiceDate() != null ? requestDto.getInvoiceDate() : LocalDate.now());
        invoice.setDueDate(requestDto.getDueDate());
        invoice.setTotalAmount(BigDecimal.ZERO);
        invoice.setStatus("DRAFT");

        return mapToResponse(salesInvoiceRepository.save(invoice));
    }

    @Override
    public List<SalesInvoiceResponseDto> getAllInvoices() {
        List<SalesInvoiceResponseDto> list = new ArrayList<>();
        for (SalesInvoice invoice : salesInvoiceRepository.findAll()) {
            list.add(mapToResponse(invoice));
        }
        return list;
    }

    @Override
    public SalesInvoiceResponseDto getInvoiceById(Long invoiceId) {
        return mapToResponse(get(invoiceId));
    }

    @Override
    public List<SalesInvoiceResponseDto> searchInvoices(Long customerId, LocalDate from, LocalDate to, String status) {
        List<SalesInvoiceResponseDto> list = new ArrayList<>();

        for (SalesInvoice invoice : salesInvoiceRepository.findAll()) {
            boolean match = true;

            if (status != null && !status.isBlank()) {
                match = invoice.getStatus() != null && invoice.getStatus().equalsIgnoreCase(status);
            }

            if (from != null && invoice.getInvoiceDate().isBefore(from)) {
                match = false;
            }

            if (to != null && invoice.getInvoiceDate().isAfter(to)) {
                match = false;
            }

            if (customerId != null) {
                SalesOrder order = salesOrderRepository.findById(invoice.getSalesOrderId()).orElse(null);
                if (order == null || !customerId.equals(order.getCustomerId())) {
                    match = false;
                }
            }

            if (match) {
                list.add(mapToResponse(invoice));
            }
        }

        return list;
    }

    @Override
    public SalesInvoiceResponseDto updateInvoice(Long invoiceId, SalesInvoiceRequestDto requestDto) {
        SalesInvoice invoice = get(invoiceId);

        if (!"DRAFT".equalsIgnoreCase(invoice.getStatus())) {
            throw new IllegalStateException("Only DRAFT invoice can be updated");
        }

        invoice.setInvoiceDate(requestDto.getInvoiceDate() != null ? requestDto.getInvoiceDate() : invoice.getInvoiceDate());
        invoice.setDueDate(requestDto.getDueDate());

        return mapToResponse(salesInvoiceRepository.save(invoice));
    }

    @Override
    public SalesInvoiceResponseDto postInvoice(Long invoiceId) {
        SalesInvoice invoice = get(invoiceId);

        if (!"DRAFT".equalsIgnoreCase(invoice.getStatus())) {
            throw new IllegalStateException("Only DRAFT invoice can be posted");
        }

        invoice.setStatus("POSTED");
        return mapToResponse(salesInvoiceRepository.save(invoice));
    }

    @Override
    public SalesInvoiceResponseDto cancelInvoice(Long invoiceId) {
        SalesInvoice invoice = get(invoiceId);

        if ("PAID".equalsIgnoreCase(invoice.getStatus())) {
            throw new IllegalStateException("Paid invoice cannot be cancelled");
        }

        invoice.setStatus("CANCELLED");
        return mapToResponse(salesInvoiceRepository.save(invoice));
    }

    @Override
    public SalesInvoiceResponseDto markPaid(Long invoiceId) {
        SalesInvoice invoice = get(invoiceId);

        if (!"POSTED".equalsIgnoreCase(invoice.getStatus())) {
            throw new IllegalStateException("Only POSTED invoice can be marked as PAID");
        }

        invoice.setStatus("PAID");
        return mapToResponse(salesInvoiceRepository.save(invoice));
    }

    @Override
    public void deleteInvoice(Long invoiceId) {
        SalesInvoice invoice = get(invoiceId);

        if (!"DRAFT".equalsIgnoreCase(invoice.getStatus())) {
            throw new IllegalStateException("Only DRAFT invoice can be deleted");
        }

        salesInvoiceRepository.delete(invoice);
    }

    @Override
    public byte[] exportInvoice(Long invoiceId) {
        SalesInvoice invoice = get(invoiceId);

        String csv = "Invoice ID,Sales Order ID,Invoice Date,Due Date,Total Amount,Status\n"
                + invoice.getInvoiceId() + ","
                + invoice.getSalesOrderId() + ","
                + invoice.getInvoiceDate() + ","
                + invoice.getDueDate() + ","
                + invoice.getTotalAmount() + ","
                + invoice.getStatus();

        return csv.getBytes(StandardCharsets.UTF_8);
    }

    private SalesInvoice get(Long invoiceId) {
        return salesInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + invoiceId));
    }

    private SalesInvoiceResponseDto mapToResponse(SalesInvoice invoice) {
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