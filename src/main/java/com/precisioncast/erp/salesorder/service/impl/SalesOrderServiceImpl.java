package com.precisioncast.erp.salesorder.service.impl;

import com.precisioncast.erp.common.exception.InvalidOperationException;
import com.precisioncast.erp.master.repository.CustomerMasterRepository;
import com.precisioncast.erp.master.repository.ItemMasterRepository;
import com.precisioncast.erp.master.entity.CustomerMaster;
import com.precisioncast.erp.master.entity.ItemMaster;
import com.precisioncast.erp.salesorder.dto.*;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.entity.SalesOrderItem;
import com.precisioncast.erp.salesorder.repository.SalesOrderRepository;
import com.precisioncast.erp.salesorder.service.SalesOrderService;
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
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerMasterRepository customerMasterRepository;
    private final ItemMasterRepository itemMasterRepository;

    @Override
    public SalesOrderResponseDto createSalesOrder(SalesOrderRequestDto requestDto) {
        CustomerMaster customer = customerMasterRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with id: " + requestDto.getCustomerId()
                ));

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCustomerId(customer.getCustomerId());
        salesOrder.setOrderDate(requestDto.getOrderDate());
        salesOrder.setOrderStatus("PENDING");
        salesOrder.setRemarks(requestDto.getRemarks());
        salesOrder.setIsUrgent(false);
        salesOrder.setIsOnHold(false);
        salesOrder.setCustomerVerified(false);

        List<SalesOrderItem> itemEntities = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SalesOrderItemRequestDto itemDto : requestDto.getItems()) {
            ItemMaster itemMaster = itemMasterRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Product not found with id: " + itemDto.getProductId()
                    ));

            BigDecimal quantity = itemDto.getQuantity();
            BigDecimal rate = itemDto.getRate();
            BigDecimal amount = quantity.multiply(rate);

            SalesOrderItem salesOrderItem = new SalesOrderItem();
            salesOrderItem.setSalesOrder(salesOrder);
            salesOrderItem.setProductId(itemMaster.getItemId());
            salesOrderItem.setQuantity(quantity);
            salesOrderItem.setRate(rate);
            salesOrderItem.setAmount(amount);

            itemEntities.add(salesOrderItem);
            totalAmount = totalAmount.add(amount);
        }

        salesOrder.setTotalAmount(totalAmount);
        salesOrder.setItems(itemEntities);

        SalesOrder savedSalesOrder = salesOrderRepository.save(salesOrder);
        return mapToResponseDto(savedSalesOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderResponseDto> getAllSalesOrders() {
        return salesOrderRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override

    public SalesOrderResponseDto getSalesOrderById(Long salesOrderId) {
        SalesOrder salesOrder = getSalesOrderEntity(salesOrderId);
        return mapToResponseDto(salesOrder);
    }

    @Override
    public SalesOrderResponseDto updateSalesOrder(Long salesOrderId, SalesOrderUpdateRequestDto requestDto) {
        SalesOrder salesOrder = getSalesOrderEntity(salesOrderId);

        if ("CONFIRMED".equalsIgnoreCase(salesOrder.getOrderStatus()) ||
                "CANCELLED".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Only PENDING sales order can be updated");
        }

        customerMasterRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with id: " + requestDto.getCustomerId()
                ));

        salesOrder.setCustomerId(requestDto.getCustomerId());
        salesOrder.setOrderDate(requestDto.getOrderDate());
        salesOrder.setRemarks(requestDto.getRemarks());

        salesOrder.getItems().clear();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SalesOrderItemRequestDto itemDto : requestDto.getItems()) {
            ItemMaster itemMaster = itemMasterRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Product not found with id: " + itemDto.getProductId()
                    ));

            BigDecimal quantity = itemDto.getQuantity();
            BigDecimal rate = itemDto.getRate();
            BigDecimal amount = quantity.multiply(rate);

            SalesOrderItem item = new SalesOrderItem();
            item.setSalesOrder(salesOrder);
            item.setProductId(itemMaster.getItemId());
            item.setQuantity(quantity);
            item.setRate(rate);
            item.setAmount(amount);
            item.setCancelled(false);

            salesOrder.getItems().add(item);
            totalAmount = totalAmount.add(amount);
        }

        salesOrder.setTotalAmount(totalAmount);

        SalesOrder updated = salesOrderRepository.save(salesOrder);
        return mapToResponseDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderResponseDto> searchSalesOrders(Long customerId, Long itemId, String status, LocalDate from, LocalDate to) {
        List<SalesOrder> allOrders = salesOrderRepository.findAll();
        List<SalesOrderResponseDto> result = new ArrayList<>();

        for (SalesOrder order : allOrders) {
            boolean match = true;

            if (customerId != null && !customerId.equals(order.getCustomerId())) {
                match = false;
            }

            if (status != null && !status.isBlank() &&
                    (order.getOrderStatus() == null || !order.getOrderStatus().equalsIgnoreCase(status))) {
                match = false;
            }

            if (from != null && (order.getOrderDate() == null || order.getOrderDate().isBefore(from))) {
                match = false;
            }

            if (to != null && (order.getOrderDate() == null || order.getOrderDate().isAfter(to))) {
                match = false;
            }

            if (itemId != null) {
                boolean itemMatch = false;
                if (order.getItems() != null) {
                    for (SalesOrderItem item : order.getItems()) {
                        if (itemId.equals(item.getProductId())) {
                            itemMatch = true;
                            break;
                        }
                    }
                }
                if (!itemMatch) {
                    match = false;
                }
            }

            if (match) {
                result.add(mapToResponseDto(order));
            }
        }

        return result;
    }

    @Override
    public SalesOrderResponseDto updateSalesOrderStatus(Long salesOrderId, String status) {
        SalesOrder salesOrder = getSalesOrderEntity(salesOrderId);

        if (status == null || status.isBlank()) {
            throw new IllegalStateException("Order status is required");
        }

        String normalizedStatus = status.trim().toUpperCase();

        List<String> allowedStatuses = List.of("PENDING", "CONFIRMED", "DELIVERED", "CANCELLED");

        if (!allowedStatuses.contains(normalizedStatus)) {
            throw new IllegalStateException(
                    "Invalid order status. Allowed values are: PENDING, CONFIRMED, DELIVERED, CANCELLED"
            );
        }

        if (normalizedStatus.equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Sales order is already in " + normalizedStatus + " status");
        }

        salesOrder.setOrderStatus(normalizedStatus);

        SalesOrderResponseDto responseDto = mapToResponseDto(salesOrderRepository.save(salesOrder));
        responseDto.setMessage("Sales order status updated successfully");
        return responseDto;
    }
    @Override
    public SalesOrderResponseDto verifyCustomer(Long salesOrderId) {
        SalesOrder salesOrder = getSalesOrderEntity(salesOrderId);

        if (Boolean.TRUE.equals(salesOrder.getCustomerVerified())) {
            throw new InvalidOperationException("Customer is already verified for this sales order");
        }

        salesOrder.setCustomerVerified(true);

        SalesOrderResponseDto responseDto = mapToResponseDto(salesOrderRepository.save(salesOrder));
        responseDto.setMessage("Customer verified successfully");
        return responseDto;
    }

    @Override
    public SalesOrderResponseDto markUrgent(Long salesOrderId) {
        SalesOrder salesOrder = getSalesOrderEntity(salesOrderId);

        if (Boolean.TRUE.equals(salesOrder.getIsUrgent())) {
            throw new InvalidOperationException("Sales order is already marked as urgent");
        }

        if (!"PENDING".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new InvalidOperationException("Only PENDING sales order can be marked as urgent");
        }

        salesOrder.setIsUrgent(true);

        SalesOrderResponseDto responseDto = mapToResponseDto(salesOrderRepository.save(salesOrder));
        responseDto.setMessage("Sales order marked as urgent successfully");
        return responseDto;
    }

    @Override
    public SalesOrderResponseDto markHold(Long salesOrderId) {
        SalesOrder salesOrder = getSalesOrderEntity(salesOrderId);


        if (Boolean.TRUE.equals(salesOrder.getIsOnHold())) {
            throw new InvalidOperationException("Sales order is already on hold");
        }


        if (!"PENDING".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new InvalidOperationException("Only PENDING sales order can be marked as on hold");
        }

        salesOrder.setIsOnHold(true);

        SalesOrderResponseDto responseDto = mapToResponseDto(salesOrderRepository.save(salesOrder));
        responseDto.setMessage("Sales order marked as on hold successfully");
        return responseDto;
    }
    @Override
    public SalesOrderResponseDto confirmSalesOrder(Long salesOrderId) {
        SalesOrder salesOrder = getSalesOrderEntity(salesOrderId);

        if ("CONFIRMED".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Sales order is already confirmed");
        }

        if ("CANCELLED".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Cancelled sales order cannot be confirmed");
        }

        if (salesOrder.getItems() == null || salesOrder.getItems().isEmpty()) {
            throw new IllegalStateException("Sales order must have at least one item before confirmation");
        }

        salesOrder.setOrderStatus("CONFIRMED");
        return mapToResponseDto(salesOrderRepository.save(salesOrder));
    }

    @Override
    public SalesOrderResponseDto cancelSalesOrder(Long salesOrderId) {
        SalesOrder salesOrder = getSalesOrderEntity(salesOrderId);

        if ("CANCELLED".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Sales order is already cancelled");
        }

        if ("CONFIRMED".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Confirmed sales order cannot be cancelled directly");
        }

        salesOrder.setOrderStatus("CANCELLED");
        return mapToResponseDto(salesOrderRepository.save(salesOrder));
    }

    @Override
    public void deleteSalesOrder(Long salesOrderId) {
        SalesOrder salesOrder = getSalesOrderEntity(salesOrderId);

        if (!"PENDING".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Only PENDING sales order can be deleted");
        }

        salesOrderRepository.delete(salesOrder);
    }

    private SalesOrder getSalesOrderEntity(Long salesOrderId) {
        return salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order not found with id: " + salesOrderId
                ));
    }

    private SalesOrderResponseDto mapToResponseDto(SalesOrder salesOrder) {
        List<SalesOrderItemResponseDto> itemResponseDtos = new ArrayList<>();

        if (salesOrder.getItems() != null) {
            for (SalesOrderItem item : salesOrder.getItems()) {
                itemResponseDtos.add(
                        SalesOrderItemResponseDto.builder()
                                .itemId(item.getItemId())
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .rate(item.getRate())
                                .amount(item.getAmount())
                                .build()
                );
            }
        }

        return SalesOrderResponseDto.builder()
                .salesOrderId(salesOrder.getSalesOrderId())
                .customerId(salesOrder.getCustomerId())
                .orderDate(salesOrder.getOrderDate())
                .orderStatus(salesOrder.getOrderStatus())
                .totalAmount(salesOrder.getTotalAmount())
                .remarks(salesOrder.getRemarks())
                .isUrgent(salesOrder.getIsUrgent())
                .isOnHold(salesOrder.getIsOnHold())
                .customerVerified(salesOrder.getCustomerVerified())
                .createdAt(salesOrder.getCreatedAt())
                .updatedAt(salesOrder.getUpdatedAt())
                .items(itemResponseDtos)
                .message(null)
                .build();
    }
}