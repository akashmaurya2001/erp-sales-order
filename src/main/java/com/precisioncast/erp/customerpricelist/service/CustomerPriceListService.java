package com.precisioncast.erp.customerpricelist.service;

import com.precisioncast.erp.customerpricelist.dto.CustomerPriceListBulkRequestDto;
import com.precisioncast.erp.customerpricelist.dto.CustomerPriceListResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerPriceListService {

    CustomerPriceListResponseDto createPrice(Long customerId, Long itemId, BigDecimal price);

    List<CustomerPriceListResponseDto> createBulkPrice(Long customerId, CustomerPriceListBulkRequestDto requestDto);

    List<CustomerPriceListResponseDto> getCustomerPriceList(Long customerId);

    CustomerPriceListResponseDto getItemPrice(Long customerId, Long itemId);

    CustomerPriceListResponseDto updatePrice(Long priceListId, BigDecimal price);

    void deletePrice(Long priceListId);
}