package com.precisioncast.erp.salesquotation.service.impl;

import com.precisioncast.erp.master.Repository.ItemMasterRepository;
import com.precisioncast.erp.master.entity.ItemMaster;
import com.precisioncast.erp.salesquotation.dto.SalesQuotationItemRequestDto;
import com.precisioncast.erp.salesquotation.dto.SalesQuotationItemResponseDto;
import com.precisioncast.erp.salesquotation.entity.SalesQuotation;
import com.precisioncast.erp.salesquotation.entity.SalesQuotationItem;
import com.precisioncast.erp.salesquotation.repository.SalesQuotationItemRepository;
import com.precisioncast.erp.salesquotation.repository.SalesQuotationRepository;
import com.precisioncast.erp.salesquotation.service.SalesQuotationItemService;
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
public class SalesQuotationItemServiceImpl implements SalesQuotationItemService {

    private final SalesQuotationRepository salesQuotationRepository;
    private final SalesQuotationItemRepository salesQuotationItemRepository;
    private final ItemMasterRepository itemMasterRepository;

    @Override
    public SalesQuotationItemResponseDto addQuotationItem(Long quotationId, Long productId, BigDecimal qty) {
        SalesQuotation quotation = salesQuotationRepository.findById(quotationId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Quotation not found with id: " + quotationId
                ));

        ItemMaster itemMaster = itemMasterRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item not found with id: " + productId
                ));

        SalesQuotationItem quotationItem = new SalesQuotationItem();
        quotationItem.setSalesQuotation(quotation);
        quotationItem.setProductId(itemMaster.getItemId());
        quotationItem.setQuantity(qty);

        BigDecimal rate = BigDecimal.ZERO;
        quotationItem.setRate(rate);
        quotationItem.setAmount(qty.multiply(rate));

        SalesQuotationItem saved = salesQuotationItemRepository.save(quotationItem);

        recalculateQuotationTotal(quotation);

        return mapToResponseDto(saved);
    }

    @Override
    public List<SalesQuotationItemResponseDto> addBulkQuotationItems(Long quotationId, List<SalesQuotationItemRequestDto> items) {
        SalesQuotation quotation = salesQuotationRepository.findById(quotationId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Quotation not found with id: " + quotationId
                ));

        List<SalesQuotationItemResponseDto> responseDto = new ArrayList<>();

        for (SalesQuotationItemRequestDto itemDto : items) {
            ItemMaster itemMaster = itemMasterRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Item not found with id: " + itemDto.getProductId()
                    ));

            BigDecimal amount = itemDto.getQuantity().multiply(itemDto.getRate());

            SalesQuotationItem quotationItem = new SalesQuotationItem();
            quotationItem.setSalesQuotation(quotation);
            quotationItem.setProductId(itemMaster.getItemId());
            quotationItem.setQuantity(itemDto.getQuantity());
            quotationItem.setRate(itemDto.getRate());
            quotationItem.setAmount(amount);

            SalesQuotationItem saved = salesQuotationItemRepository.save(quotationItem);
            responseDto.add(mapToResponseDto(saved));
        }

        recalculateQuotationTotal(quotation);

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesQuotationItemResponseDto> getQuotationItems(Long quotationId) {
        List<SalesQuotationItem> items = salesQuotationItemRepository.findBySalesQuotation_QuotationId(quotationId);
        List<SalesQuotationItemResponseDto> responseDto = new ArrayList<>();

        for (SalesQuotationItem item : items) {
            responseDto.add(mapToResponseDto(item));
        }

        return responseDto;
    }

    @Override
    public SalesQuotationItemResponseDto updateQuotationItemQty(Long quotationItemId, BigDecimal qty) {
        SalesQuotationItem item = salesQuotationItemRepository.findById(quotationItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Quotation item not found with id: " + quotationItemId
                ));

        item.setQuantity(qty);

        BigDecimal rate = item.getRate() != null ? item.getRate() : BigDecimal.ZERO;
        item.setAmount(qty.multiply(rate));

        SalesQuotationItem updated = salesQuotationItemRepository.save(item);

        recalculateQuotationTotal(item.getSalesQuotation());

        return mapToResponseDto(updated);
    }

    @Override
    public void deleteQuotationItem(Long quotationItemId) {
        SalesQuotationItem item = salesQuotationItemRepository.findById(quotationItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Quotation item not found with id: " + quotationItemId
                ));

        SalesQuotation quotation = item.getSalesQuotation();
        salesQuotationItemRepository.delete(item);

        recalculateQuotationTotal(quotation);
    }

    private void recalculateQuotationTotal(SalesQuotation quotation) {
        List<SalesQuotationItem> items =
                salesQuotationItemRepository.findBySalesQuotation_QuotationId(quotation.getQuotationId());

        BigDecimal total = BigDecimal.ZERO;

        for (SalesQuotationItem item : items) {
            if (item.getAmount() != null) {
                total = total.add(item.getAmount());
            }
        }

        quotation.setTotalAmount(total);
        salesQuotationRepository.save(quotation);
    }

    private SalesQuotationItemResponseDto mapToResponseDto(SalesQuotationItem item) {
        return SalesQuotationItemResponseDto.builder()
                .itemId(item.getItemId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .rate(item.getRate())
                .amount(item.getAmount())
                .build();
    }
}