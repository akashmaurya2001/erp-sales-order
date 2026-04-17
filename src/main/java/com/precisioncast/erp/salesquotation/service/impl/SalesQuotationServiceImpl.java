package com.precisioncast.erp.salesquotation.service.impl;

import com.precisioncast.erp.master.Repository.CustomerMasterRepository;
import com.precisioncast.erp.master.Repository.ItemMasterRepository;
import com.precisioncast.erp.master.entity.CustomerMaster;
import com.precisioncast.erp.master.entity.ItemMaster;
import com.precisioncast.erp.salesquotation.dto.SalesQuotationItemRequestDto;
import com.precisioncast.erp.salesquotation.dto.SalesQuotationItemResponseDto;
import com.precisioncast.erp.salesquotation.dto.SalesQuotationRequestDto;
import com.precisioncast.erp.salesquotation.dto.SalesQuotationResponseDto;
import com.precisioncast.erp.salesquotation.entity.SalesQuotation;
import com.precisioncast.erp.salesquotation.entity.SalesQuotationItem;
import com.precisioncast.erp.salesquotation.repository.SalesQuotationRepository;
import com.precisioncast.erp.salesquotation.service.SalesQuotationService;
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
public class SalesQuotationServiceImpl implements SalesQuotationService {

    private final SalesQuotationRepository salesQuotationRepository;
    private final CustomerMasterRepository customerMasterRepository;
    private final ItemMasterRepository itemMasterRepository;

    @Override
    public SalesQuotationResponseDto createQuotation(SalesQuotationRequestDto requestDto) {

        CustomerMaster customer = customerMasterRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with id: " + requestDto.getCustomerId()
                ));

        SalesQuotation quotation = new SalesQuotation();
        quotation.setCustomerId(customer.getCustomerId());
        quotation.setQuotationDate(requestDto.getQuotationDate());
        quotation.setValidityDate(requestDto.getValidityDate());
        quotation.setStatus("OPEN");

        List<SalesQuotationItem> itemEntities = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SalesQuotationItemRequestDto itemDto : requestDto.getItems()) {
            ItemMaster itemMaster = itemMasterRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Item not found with id: " + itemDto.getProductId()
                    ));

            BigDecimal amount = itemDto.getQuantity().multiply(itemDto.getRate());

            SalesQuotationItem quotationItem = new SalesQuotationItem();
            quotationItem.setSalesQuotation(quotation);
            quotationItem.setProductId(itemMaster.getItemId());
            quotationItem.setQuantity(itemDto.getQuantity());
            quotationItem.setRate(itemDto.getRate());
            quotationItem.setAmount(amount);

            itemEntities.add(quotationItem);
            totalAmount = totalAmount.add(amount);
        }

        quotation.setItems(itemEntities);
        quotation.setTotalAmount(totalAmount);

        SalesQuotation savedQuotation = salesQuotationRepository.save(quotation);
        return mapToResponseDto(savedQuotation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesQuotationResponseDto> getAllQuotations() {
        List<SalesQuotationResponseDto> responseDto = new ArrayList<>();

        for (SalesQuotation quotation : salesQuotationRepository.findAll()) {
            responseDto.add(mapToResponseDto(quotation));
        }

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public SalesQuotationResponseDto getQuotationById(Long quotationId) {
        SalesQuotation quotation = getQuotationEntity(quotationId);
        return mapToResponseDto(quotation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesQuotationResponseDto> searchQuotations(Long customerId, Long itemId, String status) {
        List<SalesQuotation> allQuotations = salesQuotationRepository.findAll();
        List<SalesQuotationResponseDto> result = new ArrayList<>();

        for (SalesQuotation quotation : allQuotations) {
            boolean match = true;

            if (customerId != null && !customerId.equals(quotation.getCustomerId())) {
                match = false;
            }

            if (status != null && !status.isBlank() &&
                    (quotation.getStatus() == null || !quotation.getStatus().equalsIgnoreCase(status))) {
                match = false;
            }

            if (itemId != null) {
                boolean itemMatch = false;
                if (quotation.getItems() != null) {
                    for (SalesQuotationItem item : quotation.getItems()) {
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
                result.add(mapToResponseDto(quotation));
            }
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportQuotation(Long quotationId) {
        SalesQuotation quotation = getQuotationEntity(quotationId);

        StringBuilder csv = new StringBuilder();

        csv.append("Quotation ID,Customer ID,Quotation Date,Validity Date,Status,Total Amount\n");
        csv.append(quotation.getQuotationId()).append(",")
                .append(quotation.getCustomerId()).append(",")
                .append(quotation.getQuotationDate()).append(",")
                .append(quotation.getValidityDate()).append(",")
                .append(quotation.getStatus()).append(",")
                .append(quotation.getTotalAmount()).append("\n\n");

        csv.append("Item ID,Product ID,Quantity,Rate,Amount\n");

        if (quotation.getItems() != null) {
            for (SalesQuotationItem item : quotation.getItems()) {
                csv.append(item.getItemId()).append(",")
                        .append(item.getProductId()).append(",")
                        .append(item.getQuantity()).append(",")
                        .append(item.getRate()).append(",")
                        .append(item.getAmount()).append("\n");
            }
        }

        return csv.toString().getBytes();
    }

    @Override
    public SalesQuotationResponseDto updateQuotation(Long quotationId, SalesQuotationRequestDto requestDto) {
        SalesQuotation quotation = getQuotationEntity(quotationId);

        customerMasterRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with id: " + requestDto.getCustomerId()
                ));

        quotation.setCustomerId(requestDto.getCustomerId());
        quotation.setQuotationDate(requestDto.getQuotationDate());
        quotation.setValidityDate(requestDto.getValidityDate());

        quotation.getItems().clear();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SalesQuotationItemRequestDto itemDto : requestDto.getItems()) {
            ItemMaster itemMaster = itemMasterRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Item not found with id: " + itemDto.getProductId()
                    ));

            BigDecimal amount = itemDto.getQuantity().multiply(itemDto.getRate());

            SalesQuotationItem item = new SalesQuotationItem();
            item.setSalesQuotation(quotation);
            item.setProductId(itemMaster.getItemId());
            item.setQuantity(itemDto.getQuantity());
            item.setRate(itemDto.getRate());
            item.setAmount(amount);

            quotation.getItems().add(item);
            totalAmount = totalAmount.add(amount);
        }

        quotation.setTotalAmount(totalAmount);

        SalesQuotation updatedQuotation = salesQuotationRepository.save(quotation);
        return mapToResponseDto(updatedQuotation);
    }

    @Override
    public SalesQuotationResponseDto approveQuotation(Long quotationId) {
        SalesQuotation quotation = getQuotationEntity(quotationId);
        quotation.setStatus("APPROVED");
        return mapToResponseDto(salesQuotationRepository.save(quotation));
    }

    @Override
    public SalesQuotationResponseDto rejectQuotation(Long quotationId) {
        SalesQuotation quotation = getQuotationEntity(quotationId);
        quotation.setStatus("REJECTED");
        return mapToResponseDto(salesQuotationRepository.save(quotation));
    }

    @Override
    public SalesQuotationResponseDto activateQuotation(Long quotationId) {
        SalesQuotation quotation = getQuotationEntity(quotationId);
        quotation.setStatus("ACTIVE");
        return mapToResponseDto(salesQuotationRepository.save(quotation));
    }

    @Override
    public SalesQuotationResponseDto deactivateQuotation(Long quotationId) {
        SalesQuotation quotation = getQuotationEntity(quotationId);
        quotation.setStatus("INACTIVE");
        return mapToResponseDto(salesQuotationRepository.save(quotation));
    }

    @Override
    public void deleteQuotation(Long quotationId) {
        SalesQuotation quotation = getQuotationEntity(quotationId);
        salesQuotationRepository.delete(quotation);
    }

    private SalesQuotation getQuotationEntity(Long quotationId) {
        return salesQuotationRepository.findById(quotationId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sales quotation not found with id: " + quotationId
                ));
    }

    private SalesQuotationResponseDto mapToResponseDto(SalesQuotation quotation) {
        List<SalesQuotationItemResponseDto> itemResponseDto = new ArrayList<>();

        if (quotation.getItems() != null) {
            for (SalesQuotationItem item : quotation.getItems()) {
                itemResponseDto.add(
                        SalesQuotationItemResponseDto.builder()
                                .itemId(item.getItemId())
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .rate(item.getRate())
                                .amount(item.getAmount())
                                .build()
                );
            }
        }

        return SalesQuotationResponseDto.builder()
                .quotationId(quotation.getQuotationId())
                .customerId(quotation.getCustomerId())
                .quotationDate(quotation.getQuotationDate())
                .validityDate(quotation.getValidityDate())
                .totalAmount(quotation.getTotalAmount())
                .status(quotation.getStatus())
                .createdAt(quotation.getCreatedAt())
                .updatedAt(quotation.getUpdatedAt())
                .items(itemResponseDto)
                .build();
    }
}