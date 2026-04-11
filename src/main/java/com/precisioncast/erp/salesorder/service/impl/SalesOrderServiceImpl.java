package com.precisioncast.erp.salesorder.service.impl;

import com.precisioncast.erp.master.entity.CustomerMaster;
import com.precisioncast.erp.master.entity.ItemMaster;
import com.precisioncast.erp.master.Repository.CustomerMasterRepository;
import com.precisioncast.erp.master.Repository.ItemMasterRepository;
import com.precisioncast.erp.salesorder.dto.SalesOrderItemRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderItemResponseDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderResponseDto;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.entity.SalesOrderItem;
import com.precisioncast.erp.salesorder.repository.SalesOrderRepository;
import com.precisioncast.erp.salesorder.service.SalesOrderService;
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
        List<SalesOrder> salesOrders = salesOrderRepository.findAll();
        return salesOrders.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SalesOrderResponseDto getSalesOrderById(Long salesOrderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order not found with id: " + salesOrderId
                ));

        return mapToResponseDto(salesOrder);
    }

    @Override
    public SalesOrderResponseDto confirmSalesOrder(Long salesOrderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order not found with id: " + salesOrderId
                ));

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

        SalesOrder updatedSalesOrder = salesOrderRepository.save(salesOrder);
        return mapToResponseDto(updatedSalesOrder);
    }

    @Override
    public SalesOrderResponseDto cancelSalesOrder(Long salesOrderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order not found with id: " + salesOrderId
                ));

        if ("CANCELLED".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Sales order is already cancelled");
        }

        if ("CONFIRMED".equalsIgnoreCase(salesOrder.getOrderStatus())) {

            throw new IllegalStateException("Confirmed sales order cannot be cancelled directly");
        }

        salesOrder.setOrderStatus("CANCELLED");

        SalesOrder updatedSalesOrder = salesOrderRepository.save(salesOrder);
        return mapToResponseDto(updatedSalesOrder);
    }

    @Override
    public void deleteSalesOrder(Long salesOrderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales order not found with id: " + salesOrderId
                ));

        if (!"PENDING".equalsIgnoreCase(salesOrder.getOrderStatus())) {
            throw new IllegalStateException("Only PENDING sales order can be deleted");
        }

        salesOrderRepository.delete(salesOrder);
    }

    private SalesOrderResponseDto mapToResponseDto(SalesOrder salesOrder) {
        List<SalesOrderItemResponseDto> itemResponseDtos = new ArrayList<>();

        if (salesOrder.getItems() != null) {
            for (SalesOrderItem item : salesOrder.getItems()) {
                SalesOrderItemResponseDto itemResponseDto = SalesOrderItemResponseDto.builder()
                        .itemId(item.getItemId())
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .rate(item.getRate())
                        .amount(item.getAmount())
                        .build();

                itemResponseDtos.add(itemResponseDto);
            }
        }

        return SalesOrderResponseDto.builder()
                .salesOrderId(salesOrder.getSalesOrderId())
                .customerId(salesOrder.getCustomerId())
                .orderDate(salesOrder.getOrderDate())
                .orderStatus(salesOrder.getOrderStatus())
                .totalAmount(salesOrder.getTotalAmount())
                .remarks(salesOrder.getRemarks())
                .createdAt(salesOrder.getCreatedAt())
                .updatedAt(salesOrder.getUpdatedAt())
                .items(itemResponseDtos)
                .build();
    }
}