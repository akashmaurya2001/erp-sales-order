package com.precisioncast.erp.deliverychallan.mapper;

import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanRequestDto;
import com.precisioncast.erp.deliverychallan.dto.DeliveryChallanResponseDto;
import com.precisioncast.erp.deliverychallan.entity.DeliveryChallan;
import org.springframework.stereotype.Component;

@Component
public class DeliveryChallanMapper {

    public DeliveryChallan toEntity(DeliveryChallanRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        return DeliveryChallan.builder()
                .dispatchId(requestDto.getDispatchId())
                .challanDate(requestDto.getChallanDate())
                .totalQuantity((requestDto.getTotalQuantity()))
                .remarks(requestDto.getRemarks())
                .build();
    }

    public DeliveryChallanResponseDto toResponseDto(DeliveryChallan deliveryChallan) {
        if (deliveryChallan == null) {
            return null;
        }

        return DeliveryChallanResponseDto.builder()
                .challanId(deliveryChallan.getChallanId())
                .dispatchId(deliveryChallan.getDispatchId())
                .challanDate(deliveryChallan.getChallanDate())
                .totalQuantity(deliveryChallan.getTotalQuantity())
                .build();
    }
}
