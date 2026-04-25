package com.precisioncast.erp.deliverychallan.service;

import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanResponseDto;

import java.util.List;

public interface DeliveryChallanService {

    DeliveryChallanResponseDto createChallan(Long salesOrderId, Long dispatchId);

    DeliveryChallanResponseDto getChallanById(Long challanId);

    List<DeliveryChallanResponseDto> getAllChallans();

    DeliveryChallanResponseDto completeChallan(Long challanId);

    void deleteChallan(Long challanId);
}
