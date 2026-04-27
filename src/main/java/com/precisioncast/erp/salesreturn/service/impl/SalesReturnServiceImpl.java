package com.precisioncast.erp.salesreturn.service.impl;

import com.precisioncast.erp.salesinvoice.entity.SalesInvoice;
import com.precisioncast.erp.salesinvoice.repository.SalesInvoiceRepository;
import com.precisioncast.erp.salesreturn.dto.SalesReturnResponseDto;
import com.precisioncast.erp.salesreturn.entity.SalesReturn;
import com.precisioncast.erp.salesreturn.repository.SalesReturnRepository;
import com.precisioncast.erp.salesreturn.service.SalesReturnService;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.repository.SalesOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesReturnServiceImpl implements SalesReturnService {

    private final SalesReturnRepository salesReturnRepository;
    private final SalesInvoiceRepository salesInvoiceRepository;
    private final SalesOrderRepository salesOrderRepository;

    @Override
    public SalesReturnResponseDto createSalesReturn(Long invoiceId, Long customerId) {

        SalesInvoice invoice = salesInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + invoiceId));

        if (!"PAID".equalsIgnoreCase(invoice.getStatus()) && !"POSTED".equalsIgnoreCase(invoice.getStatus())) {
            throw new IllegalStateException("Sales return can be created only for POSTED or PAID invoice");
        }

        SalesOrder salesOrder = salesOrderRepository.findById(invoice.getSalesOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Sales order not found with id: " + invoice.getSalesOrderId()));

        if (!customerId.equals(salesOrder.getCustomerId())) {
            throw new IllegalStateException("Customer does not match with invoice sales order customer");
        }

        SalesReturn salesReturn = new SalesReturn();
        salesReturn.setInvoiceId(invoice.getInvoiceId());
        salesReturn.setReturnDate(LocalDate.now());
        salesReturn.setReason("Sales return created");
        salesReturn.setTotalAmount(BigDecimal.ZERO);

        SalesReturn saved = salesReturnRepository.save(salesReturn);
        return map(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturnResponseDto> getAllSalesReturns() {
        List<SalesReturnResponseDto> responseList = new ArrayList<>();

        for (SalesReturn salesReturn : salesReturnRepository.findAll()) {
            responseList.add(map(salesReturn));
        }

        return responseList;
    }

    @Override
    @Transactional(readOnly = true)
    public SalesReturnResponseDto getSalesReturnById(Long salesReturnId) {
        return map(get(salesReturnId));
    }

    @Override
    public SalesReturnResponseDto approveSalesReturn(Long salesReturnId) {
        SalesReturn salesReturn = get(salesReturnId);

        salesReturn.setReason("APPROVED");

        return map(salesReturnRepository.save(salesReturn));
    }

    @Override
    public SalesReturnResponseDto rejectSalesReturn(Long salesReturnId) {
        SalesReturn salesReturn = get(salesReturnId);

        salesReturn.setReason("REJECTED");

        return map(salesReturnRepository.save(salesReturn));
    }

    @Override
    public void deleteSalesReturn(Long salesReturnId) {
        SalesReturn salesReturn = get(salesReturnId);

        if ("APPROVED".equalsIgnoreCase(salesReturn.getReason())) {
            throw new IllegalStateException("Approved sales return cannot be deleted");
        }

        salesReturnRepository.delete(salesReturn);
    }

    private SalesReturn get(Long salesReturnId) {
        return salesReturnRepository.findById(salesReturnId)
                .orElseThrow(() -> new EntityNotFoundException("Sales return not found with id: " + salesReturnId));
    }

    private SalesReturnResponseDto map(SalesReturn salesReturn) {
        return SalesReturnResponseDto.builder()
                .returnId(salesReturn.getReturnId())
                .invoiceId(salesReturn.getInvoiceId())
                .returnDate(salesReturn.getReturnDate())
                .reason(salesReturn.getReason())
                .totalAmount(salesReturn.getTotalAmount())
                .build();
    }
}