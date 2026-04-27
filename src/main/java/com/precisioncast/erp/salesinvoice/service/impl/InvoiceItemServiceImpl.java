package com.precisioncast.erp.salesinvoice.service.impl;

import com.precisioncast.erp.master.repository.ItemMasterRepository;
import com.precisioncast.erp.master.entity.ItemMaster;
import com.precisioncast.erp.salesinvoice.dto.InvoiceItemBulkRequestDto;
import com.precisioncast.erp.salesinvoice.dto.InvoiceItemRequestDto;
import com.precisioncast.erp.salesinvoice.dto.InvoiceItemResponseDto;
import com.precisioncast.erp.salesinvoice.entity.InvoiceItems;
import com.precisioncast.erp.salesinvoice.entity.SalesInvoice;
import com.precisioncast.erp.salesinvoice.repository.InvoiceItemRepository;
import com.precisioncast.erp.salesinvoice.repository.SalesInvoiceRepository;
import com.precisioncast.erp.salesinvoice.service.InvoiceItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceItemServiceImpl implements InvoiceItemService {

    private final InvoiceItemRepository invoiceItemRepository;
    private final SalesInvoiceRepository salesInvoiceRepository;
    private final ItemMasterRepository itemMasterRepository;

    @Override
    public InvoiceItemResponseDto addItem(Long invoiceId, Long itemId, BigDecimal qty, BigDecimal price) {
        SalesInvoice invoice = getInvoice(invoiceId);

        if (!"DRAFT".equalsIgnoreCase(invoice.getStatus())) {
            throw new IllegalStateException("Items can be added only to DRAFT invoice");
        }

        ItemMaster item = itemMasterRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));

        BigDecimal amount = qty.multiply(price);

        InvoiceItems invoiceItem = new InvoiceItems();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setProductId(item.getItemId());
        invoiceItem.setQuantity(qty);
        invoiceItem.setRate(price);
        invoiceItem.setAmount(amount);

        InvoiceItems saved = invoiceItemRepository.save(invoiceItem);
        recalculateInvoiceTotal(invoice.getInvoiceId());

        return map(saved);
    }

    @Override
    public List<InvoiceItemResponseDto> addBulkItems(Long invoiceId, InvoiceItemBulkRequestDto requestDto) {
        List<InvoiceItemResponseDto> responseList = new ArrayList<>();

        for (InvoiceItemRequestDto itemDto : requestDto.getItems()) {
            responseList.add(
                    addItem(invoiceId, itemDto.getItemId(), itemDto.getQuantity(), itemDto.getRate())
            );
        }

        return responseList;
    }

    @Override
    public List<InvoiceItemResponseDto> getItemsByInvoice(Long invoiceId) {
        getInvoice(invoiceId);

        List<InvoiceItemResponseDto> responseList = new ArrayList<>();
        for (InvoiceItems item : invoiceItemRepository.findByInvoiceId(invoiceId)) {
            responseList.add(map(item));
        }
        return responseList;
    }

    @Override
    public InvoiceItemResponseDto updateQty(Long invoiceItemId, BigDecimal qty) {
        InvoiceItems item = getInvoiceItem(invoiceItemId);
        SalesInvoice invoice = getInvoice(item.getInvoiceId());

        if (!"DRAFT".equalsIgnoreCase(invoice.getStatus())) {
            throw new IllegalStateException("Items can be updated only in DRAFT invoice");
        }

        item.setQuantity(qty);
        item.setAmount(qty.multiply(item.getRate()));

        InvoiceItems saved = invoiceItemRepository.save(item);
        recalculateInvoiceTotal(saved.getInvoiceId());

        return map(saved);
    }

    @Override
    public InvoiceItemResponseDto updatePrice(Long invoiceItemId, BigDecimal price) {
        InvoiceItems item = getInvoiceItem(invoiceItemId);
        SalesInvoice invoice = getInvoice(item.getInvoiceId());

        if (!"DRAFT".equalsIgnoreCase(invoice.getStatus())) {
            throw new IllegalStateException("Items can be updated only in DRAFT invoice");
        }

        item.setRate(price);
        item.setAmount(item.getQuantity().multiply(price));

        InvoiceItems saved = invoiceItemRepository.save(item);
        recalculateInvoiceTotal(saved.getInvoiceId());

        return map(saved);
    }

    @Override
    public void deleteItem(Long invoiceItemId) {
        InvoiceItems item = getInvoiceItem(invoiceItemId);
        SalesInvoice invoice = getInvoice(item.getInvoiceId());

        if (!"DRAFT".equalsIgnoreCase(invoice.getStatus())) {
            throw new IllegalStateException("Items can be deleted only from DRAFT invoice");
        }

        Long invoiceId = item.getInvoiceId();
        invoiceItemRepository.delete(item);
        recalculateInvoiceTotal(invoiceId);
    }

    private SalesInvoice getInvoice(Long invoiceId) {
        return salesInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + invoiceId));
    }

    private InvoiceItems getInvoiceItem(Long invoiceItemId) {
        return invoiceItemRepository.findById(invoiceItemId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice item not found with id: " + invoiceItemId));
    }

    private void recalculateInvoiceTotal(Long invoiceId) {
        SalesInvoice invoice = getInvoice(invoiceId);

        BigDecimal total = BigDecimal.ZERO;
        for (InvoiceItems item : invoiceItemRepository.findByInvoiceId(invoiceId)) {
            if (item.getAmount() != null) {
                total = total.add(item.getAmount());
            }
        }

        invoice.setTotalAmount(total);
        salesInvoiceRepository.save(invoice);
    }

    private InvoiceItemResponseDto map(InvoiceItems item) {
        return InvoiceItemResponseDto.builder()
                .invoiceItemId(item.getItemId())
                .invoiceId(item.getInvoiceId())
                .itemId(item.getProductId())
                .quantity(item.getQuantity())
                .rate(item.getRate())
                .amount(item.getAmount())
                .build();
    }
}