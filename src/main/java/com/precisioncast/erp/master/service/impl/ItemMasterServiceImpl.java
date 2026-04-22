package com.precisioncast.erp.master.service.impl;

import com.precisioncast.erp.master.Repository.ItemMasterRepository;
import com.precisioncast.erp.master.dto.ItemMasterBulkRequestDto;
import com.precisioncast.erp.master.dto.ItemMasterRequestDto;
import com.precisioncast.erp.master.dto.ItemMasterResponseDto;
import com.precisioncast.erp.master.entity.ItemMaster;
import com.precisioncast.erp.master.service.ItemMasterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemMasterServiceImpl implements ItemMasterService {

    private final ItemMasterRepository itemMasterRepository;

    @Override
    public ItemMasterResponseDto createItem(ItemMasterRequestDto requestDto) {
        itemMasterRepository.findByItemCode(requestDto.getItemCode())
                .ifPresent(item -> {
                    throw new IllegalStateException("Item code already exists: " + requestDto.getItemCode());
                });

        ItemMaster item = new ItemMaster();
        item.setUuid(UUID.randomUUID().toString());
        item.setItemCode(requestDto.getItemCode());
        item.setItemName(requestDto.getItemName());
        item.setCategoryId(requestDto.getCategoryId());
        item.setSubcategoryId(requestDto.getSubcategoryId());
        item.setUomId(requestDto.getUomId());
        item.setItemType(requestDto.getItemType());
        item.setDescription(requestDto.getDescription());
        item.setIsActive(true);

        ItemMaster saved = itemMasterRepository.save(item);
        return mapToResponseDto(saved);
    }

    @Override
    public List<ItemMasterResponseDto> createBulkItems(ItemMasterBulkRequestDto requestDto) {
        List<ItemMasterResponseDto> responseDto = new ArrayList<>();

        for (ItemMasterRequestDto dto : requestDto.getItems()) {
            itemMasterRepository.findByItemCode(dto.getItemCode())
                    .ifPresent(item -> {
                        throw new IllegalStateException("Item code already exists: " + dto.getItemCode());
                    });

            ItemMaster item = new ItemMaster();
            item.setUuid(UUID.randomUUID().toString());
            item.setItemCode(dto.getItemCode());
            item.setItemName(dto.getItemName());
            item.setCategoryId(dto.getCategoryId());
            item.setSubcategoryId(dto.getSubcategoryId());
            item.setUomId(dto.getUomId());
            item.setItemType(dto.getItemType());
            item.setDescription(dto.getDescription());
            item.setIsActive(true);

            ItemMaster saved = itemMasterRepository.save(item);
            responseDto.add(mapToResponseDto(saved));
        }

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemMasterResponseDto> getAllItems() {
        List<ItemMasterResponseDto> responseDto = new ArrayList<>();
        for (ItemMaster item : itemMasterRepository.findAll()) {
            responseDto.add(mapToResponseDto(item));
        }
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemMasterResponseDto getItemById(Long itemId) {
        ItemMaster item = itemMasterRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));
        return mapToResponseDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemMasterResponseDto> searchItems(String itemCode, String itemName, Long categoryId, Boolean active) {
        List<ItemMasterResponseDto> result = new ArrayList<>();

        for (ItemMaster item : itemMasterRepository.findAll()) {
            boolean match = true;

            if (itemCode != null && !itemCode.isBlank()) {
                if (item.getItemCode() == null || !item.getItemCode().toLowerCase().contains(itemCode.toLowerCase())) {
                    match = false;
                }
            }

            if (itemName != null && !itemName.isBlank()) {
                if (item.getItemName() == null || !item.getItemName().toLowerCase().contains(itemName.toLowerCase())) {
                    match = false;
                }
            }

            if (categoryId != null && !categoryId.equals(item.getCategoryId())) {
                match = false;
            }

            if (active != null && !active.equals(item.getIsActive())) {
                match = false;
            }

            if (match) {
                result.add(mapToResponseDto(item));
            }
        }

        return result;
    }

    @Override
    public ItemMasterResponseDto updateItem(Long itemId, ItemMasterRequestDto requestDto) {
        ItemMaster item = itemMasterRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));

        itemMasterRepository.findByItemCode(requestDto.getItemCode())
                .ifPresent(existing -> {
                    if (!existing.getItemId().equals(itemId)) {
                        throw new IllegalStateException("Item code already exists: " + requestDto.getItemCode());
                    }
                });

        item.setItemCode(requestDto.getItemCode());
        item.setItemName(requestDto.getItemName());
        item.setCategoryId(requestDto.getCategoryId());
        item.setSubcategoryId(requestDto.getSubcategoryId());
        item.setUomId(requestDto.getUomId());
        item.setItemType(requestDto.getItemType());
        item.setDescription(requestDto.getDescription());

        ItemMaster updated = itemMasterRepository.save(item);
        return mapToResponseDto(updated);
    }

    @Override
    public ItemMasterResponseDto activateItem(Long itemId) {
        ItemMaster item = itemMasterRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));

        item.setIsActive(true);
        return mapToResponseDto(itemMasterRepository.save(item));
    }

    @Override
    public ItemMasterResponseDto deactivateItem(Long itemId) {
        ItemMaster item = itemMasterRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));

        item.setIsActive(false);
        return mapToResponseDto(itemMasterRepository.save(item));
    }

    @Override
    public void deleteItem(Long itemId) {
        ItemMaster item = itemMasterRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));

        itemMasterRepository.delete(item);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportItem(Long itemId) {
        ItemMaster item = itemMasterRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));

        StringBuilder csv = new StringBuilder();
        csv.append("Item ID,UUID,Item Code,Item Name,Category ID,Subcategory ID,UOM ID,Item Type,Description,Active\n");
        csv.append(item.getItemId()).append(",")
                .append(item.getUuid()).append(",")
                .append(item.getItemCode()).append(",")
                .append(item.getItemName()).append(",")
                .append(item.getCategoryId()).append(",")
                .append(item.getSubcategoryId()).append(",")
                .append(item.getUomId()).append(",")
                .append(item.getItemType()).append(",")
                .append(item.getDescription()).append(",")
                .append(item.getIsActive());

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private ItemMasterResponseDto mapToResponseDto(ItemMaster item) {
        return ItemMasterResponseDto.builder()
                .itemId(item.getItemId())
                .uuid(item.getUuid())
                .itemCode(item.getItemCode())
                .itemName(item.getItemName())
                .categoryId(item.getCategoryId())
                .subcategoryId(item.getSubcategoryId())
                .uomId(item.getUomId())
                .itemType(item.getItemType())
                .description(item.getDescription())
                .isActive(item.getIsActive())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}