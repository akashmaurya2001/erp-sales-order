package com.precisioncast.erp.salesinvoice.service.impl;

import com.precisioncast.erp.common.exception.InvalidOperationException;
import com.precisioncast.erp.common.exception.ResourceNotFoundException;
import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceRequestDto;
import com.precisioncast.erp.salesinvoice.dto.SalesInvoiceResponseDto;
import com.precisioncast.erp.salesinvoice.entity.SalesInvoice;
import com.precisioncast.erp.salesinvoice.mapper.SalesInvoiceMapper;
import com.precisioncast.erp.salesinvoice.repository.SalesInvoiceRepository;
import com.precisioncast.erp.salesinvoice.service.SalesInvoiceService;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesInvoiceServiceImpl implements SalesInvoiceService {

    private final SalesInvoiceRepository salesInvoiceRepository;
    private final SalesInvoiceMapper salesInvoiceMapper;
    private final SalesOrderRepository salesOrderRepository;

    @Override
    public SalesInvoiceResponseDto createSalesInvoice(SalesInvoiceRequestDto requestDto) {

        SalesOrder salesOrder = salesOrderRepository.findById(requestDto.getSalesOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sales order not found with id " + requestDto.getSalesOrderId()));

        if (!"CONFIRMED".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new InvalidOperationException(
                    "sales invoice can be created only for CONFIRMED sales orders. Current status is "
                            + salesOrder.getOrderStatus()
            );
        }

        SalesInvoice salesInvoice = salesInvoiceMapper.toEntity(requestDto);

        SalesInvoice savedSalesInvoice = salesInvoiceRepository.save(salesInvoice);

        return  salesInvoiceMapper.toResponseDto(savedSalesInvoice);
    }

    @Override
    public List<SalesInvoiceResponseDto> getAllSalesInvoices() {
        return salesInvoiceRepository.findAll()
                .stream()
                .map(salesInvoiceMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public SalesInvoiceResponseDto getSalesInvoiceById(Long id) {
        SalesInvoice salesInvoice = salesInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Invoice not found with id " + id));

        return salesInvoiceMapper.toResponseDto(salesInvoice);
    }

    @Override
    public void deleteSalesInvoice(Long id) {
        SalesInvoice salesInvoice = salesInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Invoice not found with id " + id));

        salesInvoiceRepository.delete(salesInvoice);
    }
}
