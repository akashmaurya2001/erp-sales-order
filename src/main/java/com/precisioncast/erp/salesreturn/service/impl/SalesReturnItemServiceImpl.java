package com.precisioncast.erp.salesreturn.service.impl;

import com.precisioncast.erp.master.entity.ItemMaster;
import com.precisioncast.erp.master.repository.ItemMasterRepository;
import com.precisioncast.erp.salesinvoice.entity.InvoiceItems;
import com.precisioncast.erp.salesinvoice.repository.InvoiceItemRepository;
import com.precisioncast.erp.salesreturn.dto.SalesReturnItemBulkRequestDto;
import com.precisioncast.erp.salesreturn.dto.SalesReturnItemRequestDto;
import com.precisioncast.erp.salesreturn.dto.SalesReturnItemResponseDto;
import com.precisioncast.erp.salesreturn.entity.SalesReturn;
import com.precisioncast.erp.salesreturn.entity.SalesReturnItem;
import com.precisioncast.erp.salesreturn.repository.SalesReturnItemRepository;
import com.precisioncast.erp.salesreturn.repository.SalesReturnRepository;
import com.precisioncast.erp.salesreturn.service.SalesReturnItemService;
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
public class SalesReturnItemServiceImpl implements SalesReturnItemService {

    private final SalesReturnRepository salesReturnRepository;
    private final SalesReturnItemRepository salesReturnItemRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final ItemMasterRepository itemMasterRepository;

    @Override
    public SalesReturnItemResponseDto addItem(Long salesReturnId, Long itemId, BigDecimal qty, Long reasonId) {

        SalesReturn salesReturn = getReturn(salesReturnId);

        ItemMaster item = itemMasterRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));

        InvoiceItems invoiceItem = invoiceItemRepository.findByProductId(itemId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Item not found in invoice"));

        if (qty.compareTo(invoiceItem.getQuantity()) > 0) {
            throw new IllegalStateException("Return quantity cannot exceed invoice quantity");
        }

        BigDecimal amount = qty.multiply(invoiceItem.getRate());

        SalesReturnItem returnItem = new SalesReturnItem();
        returnItem.setReturnId(salesReturnId);
        returnItem.setProductId(item.getItemId());
        returnItem.setQuantity(qty);
        returnItem.setRate(invoiceItem.getRate());
        returnItem.setAmount(amount);

        SalesReturnItem saved = salesReturnItemRepository.save(returnItem);

        recalculateTotal(salesReturnId);

        return map(saved);
    }

    @Override
    public List<SalesReturnItemResponseDto> addBulkItems(Long salesReturnId, SalesReturnItemBulkRequestDto requestDto) {

        List<SalesReturnItemResponseDto> responseList = new ArrayList<>();

        for (SalesReturnItemRequestDto dto : requestDto.getItems()) {
            responseList.add(
                    addItem(salesReturnId, dto.getItemId(), dto.getQuantity(), dto.getReasonId())
            );
        }

        return responseList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturnItemResponseDto> getItemsByReturnId(Long salesReturnId) {

        getReturn(salesReturnId);

        List<SalesReturnItemResponseDto> responseList = new ArrayList<>();

        for (SalesReturnItem item : salesReturnItemRepository.findByReturnId(salesReturnId)) {
            responseList.add(map(item));
        }

        return responseList;
    }

    @Override
    public SalesReturnItemResponseDto updateQty(Long salesReturnItemId, BigDecimal qty) {

        SalesReturnItem item = getItem(salesReturnItemId);

        item.setQuantity(qty);
        item.setAmount(qty.multiply(item.getRate()));

        SalesReturnItem saved = salesReturnItemRepository.save(item);

        recalculateTotal(saved.getReturnId());

        return map(saved);
    }

    @Override
    public void deleteItem(Long salesReturnItemId) {

        SalesReturnItem item = getItem(salesReturnItemId);

        Long returnId = item.getReturnId();

        salesReturnItemRepository.delete(item);

        recalculateTotal(returnId);
    }

    private SalesReturn getReturn(Long id) {
        return salesReturnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sales return not found with id: " + id));
    }

    private SalesReturnItem getItem(Long id) {
        return salesReturnItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sales return item not found with id: " + id));
    }

    private void recalculateTotal(Long returnId) {

        SalesReturn salesReturn = getReturn(returnId);

        BigDecimal total = BigDecimal.ZERO;

        for (SalesReturnItem item : salesReturnItemRepository.findByReturnId(returnId)) {
            if (item.getAmount() != null) {
                total = total.add(item.getAmount());
            }
        }

        salesReturn.setTotalAmount(total);

        salesReturnRepository.save(salesReturn);
    }

    private SalesReturnItemResponseDto map(SalesReturnItem item) {
        return SalesReturnItemResponseDto.builder()
                .returnItemId(item.getItemId())
                .returnId(item.getReturnId())
                .itemId(item.getProductId())
                .quantity(item.getQuantity())
                .rate(item.getRate())
                .amount(item.getAmount())
                .build();
    }
}