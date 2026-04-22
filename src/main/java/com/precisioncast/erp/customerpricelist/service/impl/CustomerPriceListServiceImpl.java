package com.precisioncast.erp.customerpricelist.service.impl;

import com.precisioncast.erp.customerpricelist.dto.CustomerPriceListBulkRequestDto;
import com.precisioncast.erp.customerpricelist.dto.CustomerPriceListRequestDto;
import com.precisioncast.erp.customerpricelist.dto.CustomerPriceListResponseDto;
import com.precisioncast.erp.customerpricelist.entity.CustomerPriceList;
import com.precisioncast.erp.customerpricelist.repository.CustomerPriceListRepository;
import com.precisioncast.erp.customerpricelist.service.CustomerPriceListService;
import com.precisioncast.erp.master.Repository.CustomerMasterRepository;
import com.precisioncast.erp.master.Repository.ItemMasterRepository;
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
public class CustomerPriceListServiceImpl implements CustomerPriceListService {

    private final CustomerPriceListRepository customerPriceListRepository;
    private final CustomerMasterRepository customerMasterRepository;
    private final ItemMasterRepository itemMasterRepository;

    @Override
    public CustomerPriceListResponseDto createPrice(Long customerId, Long itemId, BigDecimal price) {
        customerMasterRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with id: " + customerId
                ));

        itemMasterRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item not found with id: " + itemId
                ));

        CustomerPriceList existing = customerPriceListRepository
                .findByCustomerIdAndItemId(customerId, itemId)
                .orElse(null);

        if (existing != null) {
            throw new IllegalStateException(
                    "Price already exists for customerId " + customerId + " and itemId " + itemId
            );
        }

        CustomerPriceList priceList = new CustomerPriceList();
        priceList.setCustomerId(customerId);
        priceList.setItemId(itemId);
        priceList.setSpecialRate(price);

        CustomerPriceList saved = customerPriceListRepository.save(priceList);
        return mapToResponseDto(saved);
    }

    @Override
    public List<CustomerPriceListResponseDto> createBulkPrice(Long customerId, CustomerPriceListBulkRequestDto requestDto) {
        customerMasterRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with id: " + customerId
                ));

        List<CustomerPriceListResponseDto> responseDtos = new ArrayList<>();

        for (CustomerPriceListRequestDto dto : requestDto.getItems()) {
            itemMasterRepository.findById(dto.getItemId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Item not found with id: " + dto.getItemId()
                    ));

            CustomerPriceList existing = customerPriceListRepository
                    .findByCustomerIdAndItemId(customerId, dto.getItemId())
                    .orElse(null);

            if (existing != null) {
                throw new IllegalStateException(
                        "Price already exists for customerId " + customerId + " and itemId " + dto.getItemId()
                );
            }

            CustomerPriceList priceList = new CustomerPriceList();
            priceList.setCustomerId(customerId);
            priceList.setItemId(dto.getItemId());
            priceList.setSpecialRate(dto.getSpecialRate());
            priceList.setValidFrom(dto.getValidFrom());
            priceList.setValidTo(dto.getValidTo());

            CustomerPriceList saved = customerPriceListRepository.save(priceList);
            responseDtos.add(mapToResponseDto(saved));
        }

        return responseDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerPriceListResponseDto> getCustomerPriceList(Long customerId) {
        List<CustomerPriceList> list = customerPriceListRepository.findByCustomerId(customerId);
        List<CustomerPriceListResponseDto> responseDtos = new ArrayList<>();

        for (CustomerPriceList priceList : list) {
            responseDtos.add(mapToResponseDto(priceList));
        }

        return responseDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerPriceListResponseDto getItemPrice(Long customerId, Long itemId) {
        CustomerPriceList priceList = customerPriceListRepository
                .findByCustomerIdAndItemId(customerId, itemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer price not found for customerId " + customerId + " and itemId " + itemId
                ));

        return mapToResponseDto(priceList);
    }

    @Override
    public CustomerPriceListResponseDto updatePrice(Long priceListId, BigDecimal price) {
        CustomerPriceList priceList = customerPriceListRepository.findById(priceListId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer price list not found with id: " + priceListId
                ));

        priceList.setSpecialRate(price);

        CustomerPriceList updated = customerPriceListRepository.save(priceList);
        return mapToResponseDto(updated);
    }

    @Override
    public void deletePrice(Long priceListId) {
        CustomerPriceList priceList = customerPriceListRepository.findById(priceListId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer price list not found with id: " + priceListId
                ));

        customerPriceListRepository.delete(priceList);
    }

    private CustomerPriceListResponseDto mapToResponseDto(CustomerPriceList priceList) {
        return CustomerPriceListResponseDto.builder()
                .priceListId(priceList.getPriceListId())
                .customerId(priceList.getCustomerId())
                .itemId(priceList.getItemId())
                .specialRate(priceList.getSpecialRate())
                .validFrom(priceList.getValidFrom())
                .validTo(priceList.getValidTo())
                .build();
    }
}