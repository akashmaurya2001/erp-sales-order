package com.precisioncast.erp.salesorder.mapper;

import com.precisioncast.erp.salesorder.dto.SalesOrderItemResponseDto;
import com.precisioncast.erp.salesorder.dto.SalesOrderResponseDto;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.entity.SalesOrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SalesOrderMapper {

    public SalesOrderResponseDto toResponseDto(SalesOrder salesOrder) {
        if (salesOrder == null) {
            return null;
        }

        List<SalesOrderItemResponseDto> itemResponseDtos = new ArrayList<>();

        if (salesOrder.getItems() != null) {
            for (SalesOrderItem item : salesOrder.getItems()) {
                SalesOrderItemResponseDto itemDto = new SalesOrderItemResponseDto();
                itemDto.setItemId(item.getItemId());
                itemDto.setProductId(item.getProductId());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setRate(item.getRate());
                itemDto.setAmount(item.getAmount());

                itemResponseDtos.add(itemDto);
            }
        }

        SalesOrderResponseDto responseDto = new SalesOrderResponseDto();
        responseDto.setSalesOrderId(salesOrder.getSalesOrderId());
        responseDto.setCustomerId(salesOrder.getCustomerId());
        responseDto.setOrderDate(salesOrder.getOrderDate());
        responseDto.setOrderStatus(salesOrder.getOrderStatus());
        responseDto.setTotalAmount(salesOrder.getTotalAmount());
        responseDto.setRemarks(salesOrder.getRemarks());
        responseDto.setCreatedAt(salesOrder.getCreatedAt());
        responseDto.setUpdatedAt(salesOrder.getUpdatedAt());
        responseDto.setItems(itemResponseDtos);

        return responseDto;
    }

    public List<SalesOrderResponseDto> toResponseDtoList(List<SalesOrder> salesOrders) {
        List<SalesOrderResponseDto> responseDtos = new ArrayList<>();

        if (salesOrders == null) {
            return responseDtos;
        }

        for (SalesOrder salesOrder : salesOrders) {
            responseDtos.add(toResponseDto(salesOrder));
        }

        return responseDtos;
    }
}