package com.precisioncast.erp.master.service;

import com.precisioncast.erp.master.dto.ItemMasterBulkRequestDto;
import com.precisioncast.erp.master.dto.ItemMasterRequestDto;
import com.precisioncast.erp.master.dto.ItemMasterResponseDto;

import java.util.List;

public interface ItemMasterService {

    ItemMasterResponseDto createItem(ItemMasterRequestDto requestDto);

    List<ItemMasterResponseDto> createBulkItems(ItemMasterBulkRequestDto requestDto);

    List<ItemMasterResponseDto> getAllItems();

    ItemMasterResponseDto getItemById(Long itemId);

    List<ItemMasterResponseDto> searchItems(String itemCode, String itemName, Long categoryId, Boolean active);

    ItemMasterResponseDto updateItem(Long itemId, ItemMasterRequestDto requestDto);

    ItemMasterResponseDto activateItem(Long itemId);

    ItemMasterResponseDto deactivateItem(Long itemId);

    void deleteItem(Long itemId);

    byte[] exportItem(Long itemId);
}