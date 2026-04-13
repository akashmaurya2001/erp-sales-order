package com.precisioncast.erp.salesreturn.service.impl;

import com.precisioncast.erp.salesinvoice.entity.SalesInvoice;
import com.precisioncast.erp.salesinvoice.repository.SalesInvoiceRepository;
import com.precisioncast.erp.salesreturn.dto.SalesReturnItemRequestDto;
import com.precisioncast.erp.salesreturn.dto.SalesReturnItemResponseDto;
import com.precisioncast.erp.salesreturn.dto.SalesReturnRequestDto;
import com.precisioncast.erp.salesreturn.dto.SalesReturnResponseDto;
import com.precisioncast.erp.salesreturn.entity.SalesReturn;
import com.precisioncast.erp.salesreturn.entity.SalesReturnItem;
import com.precisioncast.erp.salesreturn.repository.SalesReturnItemRepository;
import com.precisioncast.erp.salesreturn.repository.SalesReturnRepository;
import com.precisioncast.erp.salesreturn.service.SalesReturnService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesReturnServiceImpl implements SalesReturnService {

    private final SalesReturnRepository salesReturnRepository;
    private final SalesReturnItemRepository salesReturnItemRepository;
    private final SalesInvoiceRepository salesInvoiceRepository;

    @Override
    public SalesReturnResponseDto createSalesReturn(SalesReturnRequestDto requestDto) {

        SalesInvoice salesInvoice = salesInvoiceRepository.findById(requestDto.getInvoiceId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales invoice not found with id: " + requestDto.getInvoiceId()
                ));

        SalesReturn salesReturn = new SalesReturn();
        salesReturn.setInvoiceId(salesInvoice.getInvoiceId());
        salesReturn.setReturnDate(requestDto.getReturnDate());
        salesReturn.setReason(requestDto.getReason());

        BigDecimal totalAmount = BigDecimal.ZERO;
        SalesReturn savedReturn = salesReturnRepository.save(salesReturn);

        List<SalesReturnItem> savedItems = new ArrayList<>();

        for (SalesReturnItemRequestDto itemDto : requestDto.getItems()) {
            BigDecimal amount = itemDto.getQuantity().multiply(itemDto.getRate());

            SalesReturnItem item = new SalesReturnItem();
            item.setReturnId(savedReturn.getReturnId());
            item.setProductId(itemDto.getProductId());
            item.setQuantity(itemDto.getQuantity());
            item.setRate(itemDto.getRate());
            item.setAmount(amount);

            savedItems.add(salesReturnItemRepository.save(item));
            totalAmount = totalAmount.add(amount);
        }

        savedReturn.setTotalAmount(totalAmount);
        SalesReturn updatedReturn = salesReturnRepository.save(savedReturn);

        return mapToResponseDto(updatedReturn, savedItems);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturnResponseDto> getAllSalesReturns() {
        List<SalesReturn> salesReturns = salesReturnRepository.findAll();
        List<SalesReturnResponseDto> responseDto = new ArrayList<>();

        for (SalesReturn salesReturn : salesReturns) {
            List<SalesReturnItem> items = salesReturnItemRepository.findByReturnId(salesReturn.getReturnId());
            responseDto.add(mapToResponseDto(salesReturn, items));
        }

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public SalesReturnResponseDto getSalesReturnById(Long returnId) {
        SalesReturn salesReturn = salesReturnRepository.findById(returnId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales return not found with id: " + returnId
                ));

        List<SalesReturnItem> items = salesReturnItemRepository.findByReturnId(returnId);

        return mapToResponseDto(salesReturn, items);
    }

    @Override
    public void deleteSalesReturn(Long returnId) {
        SalesReturn salesReturn = salesReturnRepository.findById(returnId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales return not found with id: " + returnId
                ));

        List<SalesReturnItem> items = salesReturnItemRepository.findByReturnId(returnId);
        salesReturnItemRepository.deleteAll(items);
        salesReturnRepository.delete(salesReturn);
    }

    private SalesReturnResponseDto mapToResponseDto(SalesReturn salesReturn, List<SalesReturnItem> items) {
        List<SalesReturnItemResponseDto> itemResponseDto = new ArrayList<>();

        for (SalesReturnItem item : items) {
            SalesReturnItemResponseDto itemDto = SalesReturnItemResponseDto.builder()
                    .itemId(item.getItemId())
                    .returnId(item.getReturnId())
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .rate(item.getRate())
                    .amount(item.getAmount())
                    .build();

            itemResponseDto.add(itemDto);
        }

        return SalesReturnResponseDto.builder()
                .returnId(salesReturn.getReturnId())
                .invoiceId(salesReturn.getInvoiceId())
                .returnDate(salesReturn.getReturnDate())
                .reason(salesReturn.getReason())
                .totalAmount(salesReturn.getTotalAmount())
                .items(itemResponseDto)
                .build();
    }
}