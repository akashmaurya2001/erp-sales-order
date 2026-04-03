package com.precisioncast.erp.salesorder.mapper;

import com.precisioncast.erp.salesorder.dto.SalesOrderItemRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderItemResponseDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderRequestDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderResponseDto;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.entity.SalesOrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SalesOrderMapper {

    public SalesOrder toEntity(SalesOrderRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        SalesOrder salesOrder = SalesOrder.builder()
                .orderNumber(requestDto.getOrderNumber())
                .orderDate(requestDto.getOrderDate())
                .customerName(requestDto.getCustomerName())
                .customerCode(requestDto.getCustomerCode())
                .customerEmail(requestDto.getCustomerEmail())
                .customerPhone(requestDto.getCustomerPhone())
                .billingAddress(requestDto.getBillingAddress())
                .shippingAddress(requestDto.getShippingAddress())
                .taxAmount(requestDto.getTaxAmount())
                .discountAmount(requestDto.getDiscountAmount())
                .remarks(requestDto.getRemarks())
                .build();

        List<SalesOrderItem> items = new ArrayList<>();

        if (requestDto.getItems() != null) {
            for (SalesOrderItemRequestDto itemDto : requestDto.getItems()) {
                SalesOrderItem item = SalesOrderItem.builder()
                        .productCode(itemDto.getProductCode())
                        .productName(itemDto.getProductName())
                        .description(itemDto.getDescription())
                        .quantity(itemDto.getQuantity())
                        .unitPrice(itemDto.getUnitPrice())
                        .unit(itemDto.getUnit())
                        .build();

                item.setSalesOrder(salesOrder);
                items.add(item);
            }
        }

        salesOrder.setItems(items);
        return salesOrder;
    }

    public SalesOrderResponseDto toResponseDto(SalesOrder salesOrder) {
        if (salesOrder == null) {
            return null;
        }

        return SalesOrderResponseDto.builder()
                .id(salesOrder.getId())
                .orderNumber(salesOrder.getOrderNumber())
                .orderDate(salesOrder.getOrderDate())
                .customerName(salesOrder.getCustomerName())
                .customerCode(salesOrder.getCustomerCode())
                .customerEmail(salesOrder.getCustomerEmail())
                .customerPhone(salesOrder.getCustomerPhone())
                .billingAddress(salesOrder.getBillingAddress())
                .shippingAddress(salesOrder.getShippingAddress())
                .status(salesOrder.getStatus())
                .subtotal(salesOrder.getSubtotal())
                .taxAmount(salesOrder.getTaxAmount())
                .discountAmount(salesOrder.getDiscountAmount())
                .totalAmount(salesOrder.getTotalAmount())
                .remarks(salesOrder.getRemarks())
                .items(toItemResponseDtoList(salesOrder.getItems()))
                .build();
    }

    public List<SalesOrderItemResponseDto> toItemResponseDtoList(List<SalesOrderItem> items) {
        if (items == null) {
            return new ArrayList<>();
        }

        return items.stream()
                .map(this::toItemResponseDto)
                .collect(Collectors.toList());
    }

    public SalesOrderItemResponseDto toItemResponseDto(SalesOrderItem item) {
        if (item == null) {
            return null;
        }

        return SalesOrderItemResponseDto.builder()
                .id(item.getId())
                .productCode(item.getProductCode())
                .productName(item.getProductName())
                .description(item.getDescription())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .lineTotal(item.getLineTotal())
                .unit(item.getUnit())
                .build();
    }
}