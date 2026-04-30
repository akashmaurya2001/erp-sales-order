package com.precisioncast.erp.salesorder.service.impl;

import com.precisioncast.erp.common.exception.InvalidOperationException;
import com.precisioncast.erp.customerpricelist.entity.CustomerPriceList;
import com.precisioncast.erp.customerpricelist.repository.CustomerPriceListRepository;
import com.precisioncast.erp.master.repository.ItemMasterRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesOrderItemServiceImpl implements SalesOrderItemService {

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final ItemMasterRepository itemMasterRepository;
    private final CustomerPriceListRepository customerPriceListRepository;

    @Override
    public SalesOrderItemResponseDto addSalesOrderItem(Long salesOrderId, Long itemId, BigDecimal qty) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order not found with id: " + salesOrderId
                ));

        if (!"PENDING".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new InvalidOperationException("Items can only be added to PENDING sales order");
        }

        ItemMaster itemMaster = itemMasterRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item not found with id: " + itemId
                ));

        CustomerPriceList priceList = customerPriceListRepository
                .findByCustomerIdAndItemId(salesOrder.getCustomerId(), itemMaster.getItemId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer price not found for customerId " + salesOrder.getCustomerId()
                                + " and itemId " + itemMaster.getItemId()
                ));

        BigDecimal rate = priceList.getSpecialRate();
        BigDecimal amount = qty.multiply(rate);

        SalesOrderItem salesOrderItem = new SalesOrderItem();
        salesOrderItem.setSalesOrder(salesOrder);
        salesOrderItem.setProductId(itemMaster.getItemId());
        salesOrderItem.setQuantity(qty);
        salesOrderItem.setRate(rate);
        salesOrderItem.setAmount(amount);
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
            throw new InvalidOperationException("Items can only be added to PENDING sales order");
        }

        Set<Long> requestProductIds = new HashSet<>();

        for (SalesOrderItemRequestDto itemDto : requestDto.getItems()) {
            if (!requestProductIds.add(itemDto.getProductId())) {
                throw new InvalidOperationException(
                        "Duplicate productId found in request: " + itemDto.getProductId()
                );
            }
        }

        List<SalesOrderItem> existingItems =
                salesOrderItemRepository.findBySalesOrder_SalesOrderId(salesOrderId);

        for (SalesOrderItem existingItem : existingItems) {
            if (Boolean.TRUE.equals(existingItem.getCancelled())) {
                continue;
            }

            if (requestProductIds.contains(existingItem.getProductId())) {
                throw new InvalidOperationException(
                        "Product already exists in this sales order: " + existingItem.getProductId()
                );
            }
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

        salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order not found with id: " + salesOrderId
                ));

        List<SalesOrderItem> items = salesOrderItemRepository.findBySalesOrder_SalesOrderId(salesOrderId);

        List<SalesOrderItemResponseDto> responseDto = new ArrayList<>();

        for (SalesOrderItem item : items) {
            responseDto.add(mapToResponseDto(item));
        }

        return responseDto;
    }

    @Override
    public SalesOrderItemResponseDto updateSalesOrderItemQty(Long salesOrderItemId, BigDecimal qty) {

        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Qty should be greater than 0");
        }

        if (qty.compareTo(new BigDecimal("999999")) > 0) {
            throw new InvalidOperationException("Qty is too large. Maximum allowed quantity is 999999");
        }

        SalesOrderItem item = salesOrderItemRepository.findById(salesOrderItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order item not found with id: " + salesOrderItemId
                ));

        SalesOrder salesOrder = item.getSalesOrder();

        if (!"PENDING".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new InvalidOperationException("Only PENDING sales order items can be updated");
        }

        BigDecimal rate = item.getRate() != null ? item.getRate() : BigDecimal.ZERO;
        BigDecimal amount = qty.multiply(rate);

        if (amount.compareTo(new BigDecimal("9999999999.99")) > 0) {
            throw new InvalidOperationException("Amount is too large. Please reduce quantity or rate");
        }

        item.setQuantity(qty);
        item.setAmount(amount);

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
            throw new InvalidOperationException("Only PENDING sales order items can be cancelled");
        }

        if (Boolean.TRUE.equals(item.getCancelled())) {
            throw new InvalidOperationException("Sales order item is already cancelled");
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
            throw new InvalidOperationException("Only PENDING sales order items can be deleted");
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