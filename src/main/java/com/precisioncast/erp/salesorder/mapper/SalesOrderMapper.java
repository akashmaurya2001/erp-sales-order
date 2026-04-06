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
                .customerId(requestDto.getCustomerId())
                .orderDate(requestDto.getOrderDate())
                .remarks(requestDto.getRemarks())
                .build();

        List<SalesOrderItem> items = new ArrayList<>();

        if (requestDto.getItems() != null) {
            for (SalesOrderItemRequestDto itemDto : requestDto.getItems()) {
                SalesOrderItem item = SalesOrderItem.builder()
                        .productId(itemDto.getProductId())
                        .quantity(itemDto.getQuantity())
                        .rate(itemDto.getRate())
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
                .customerId(salesOrder.getCustomerId())
                .orderDate(salesOrder.getOrderDate())
                .orderStatus(salesOrder.getOrderStatus())
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
                .itemId(item.getItemId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .rate(item.getRate())
                .amount(item.getAmount())
                .build();
    }
}