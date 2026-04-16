package com.precisioncast.erp.salesorder.service.impl;

import com.precisioncast.erp.master.Repository.ItemMasterRepository;
import com.precisioncast.erp.master.entity.ItemMaster;
import com.precisioncast.erp.salesorder.dto.SalesOrderItemRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderItemResponseDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderItemBulkRequestDto;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.entity.SalesOrderItem;
import com.precisioncast.erp.salesorder.repository.SalesOrderItemRepository;
import com.precisioncast.erp.salesorder.repository.SalesOrderRepository;
import com.precisioncast.erp.salesorder.service.SalesOrderItemService;
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
public class SalesOrderItemServiceImpl implements SalesOrderItemService {

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final ItemMasterRepository itemMasterRepository;

    @Override
    public SalesOrderItemResponseDto addSalesOrderItem(Long salesOrderId, Long itemId, BigDecimal qty) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order not found with id: " + salesOrderId
                ));

        if (!"PENDING".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Items can only be added to PENDING sales order");
        }

        ItemMaster itemMaster = itemMasterRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item not found with id: " + itemId
                ));

        SalesOrderItem salesOrderItem = new SalesOrderItem();
        salesOrderItem.setSalesOrder(salesOrder);
        salesOrderItem.setProductId(itemMaster.getItemId());
        salesOrderItem.setQuantity(qty);

        BigDecimal rate = BigDecimal.ZERO;
        salesOrderItem.setRate(rate);
        salesOrderItem.setAmount(qty.multiply(rate));
        salesOrderItem.setCancelled(false);

        SalesOrderItem saved = salesOrderItemRepository.save(salesOrderItem);

        recalculateSalesOrderTotal(salesOrder);

        return mapToResponseDto(saved);
    }

    @Override
    public List<SalesOrderItemResponseDto> addBulkSalesOrderItem(Long salesOrderId, SalesOrderItemBulkRequestDto requestDto) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order not found with id: " + salesOrderId
                ));

        if (!"PENDING".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Items can only be added to PENDING sales order");
        }

        List<SalesOrderItemResponseDto> responseDto = new ArrayList<>();

        for (SalesOrderItemRequestDto itemDto : requestDto.getItems()) {
            ItemMaster itemMaster = itemMasterRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Item not found with id: " + itemDto.getProductId()
                    ));

            BigDecimal amount = itemDto.getQuantity().multiply(itemDto.getRate());

            SalesOrderItem item = new SalesOrderItem();
            item.setSalesOrder(salesOrder);
            item.setProductId(itemMaster.getItemId());
            item.setQuantity(itemDto.getQuantity());
            item.setRate(itemDto.getRate());
            item.setAmount(amount);
            item.setCancelled(false);

            SalesOrderItem saved = salesOrderItemRepository.save(item);
            responseDto.add(mapToResponseDto(saved));
        }

        recalculateSalesOrderTotal(salesOrder);

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderItemResponseDto> getSalesOrderItems(Long salesOrderId) {
        List<SalesOrderItem> items = salesOrderItemRepository.findBySalesOrder_SalesOrderId(salesOrderId);
        List<SalesOrderItemResponseDto> responseDto = new ArrayList<>();

        for (SalesOrderItem item : items) {
            responseDto.add(mapToResponseDto(item));
        }

        return responseDto;
    }

    @Override
    public SalesOrderItemResponseDto updateSalesOrderItemQty(Long salesOrderItemId, BigDecimal qty) {
        SalesOrderItem item = salesOrderItemRepository.findById(salesOrderItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order item not found with id: " + salesOrderItemId
                ));

        SalesOrder salesOrder = item.getSalesOrder();

        if (!"PENDING".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Only PENDING sales order items can be updated");
        }

        item.setQuantity(qty);

        BigDecimal rate = item.getRate() != null ? item.getRate() : BigDecimal.ZERO;
        item.setAmount(qty.multiply(rate));

        SalesOrderItem updated = salesOrderItemRepository.save(item);

        recalculateSalesOrderTotal(salesOrder);

        return mapToResponseDto(updated);
    }

    @Override
    public SalesOrderItemResponseDto cancelSalesOrderItem(Long salesOrderItemId) {
        SalesOrderItem item = salesOrderItemRepository.findById(salesOrderItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order item not found with id: " + salesOrderItemId
                ));

        SalesOrder salesOrder = item.getSalesOrder();

        if (!"PENDING".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Only PENDING sales order items can be cancelled");
        }

        item.setCancelled(true);

        SalesOrderItem updated = salesOrderItemRepository.save(item);

        recalculateSalesOrderTotal(salesOrder);

        return mapToResponseDto(updated);
    }

    @Override
    public void deleteSalesOrderItem(Long salesOrderItemId) {
        SalesOrderItem item = salesOrderItemRepository.findById(salesOrderItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order item not found with id: " + salesOrderItemId
                ));

        SalesOrder salesOrder = item.getSalesOrder();

        if (!"PENDING".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Only PENDING sales order items can be deleted");
        }

        salesOrderItemRepository.delete(item);

        recalculateSalesOrderTotal(salesOrder);
    }

    private void recalculateSalesOrderTotal(SalesOrder salesOrder) {
        List<SalesOrderItem> allItems = salesOrderItemRepository.findBySalesOrder_SalesOrderId(salesOrder.getSalesOrderId());

        BigDecimal total = BigDecimal.ZERO;

        for (SalesOrderItem item : allItems) {
            if (Boolean.TRUE.equals(item.getCancelled())) {
                continue;
            }

            if (item.getAmount() != null) {
                total = total.add(item.getAmount());
            }
        }

        salesOrder.setTotalAmount(total);
        salesOrderRepository.save(salesOrder);
    }

    private SalesOrderItemResponseDto mapToResponseDto(SalesOrderItem item) {
        return SalesOrderItemResponseDto.builder()
                .itemId(item.getItemId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .rate(item.getRate())
                .amount(item.getAmount())
                .cancelled(item.getCancelled())
                .build();
    }
}