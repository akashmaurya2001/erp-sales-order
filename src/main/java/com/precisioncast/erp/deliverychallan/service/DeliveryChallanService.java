package com.precisioncast.erp.deliverychallan.service;

import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanRequestDto;
import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanResponseDto;

import java.util.List;

public interface DeliveryChallanService {

    DeliveryChallanResponseDto createDeliveryChallan(DeliveryChallanRequestDto requestDto);

    List<DeliveryChallanResponseDto> getAllDeliveryChallans();

    DeliveryChallanResponseDto getDeliveryChallanById(Long challanId);

    void deleteDeliveryChallan(Long challanId);
}
