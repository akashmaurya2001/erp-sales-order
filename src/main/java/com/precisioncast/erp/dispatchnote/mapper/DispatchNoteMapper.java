package com.precisioncast.erp.dispatchnote.mapper;

import com.precisioncast.erp.dispatchnote.dto.DispatchNoteRequestDto;
import com.precisioncast.erp.dispatchnote.dto.DispatchNoteResponseDto;
import com.precisioncast.erp.dispatchnote.entity.DispatchNote;
import org.springframework.stereotype.Component;

@Component
public class DispatchNoteMapper {

    public DispatchNote toEntity(DispatchNoteRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        return DispatchNote.builder()
                .salesOrderId(requestDto.getSalesOrderId())
                .dispatchDate(requestDto.getDispatchDate())
                .vehicleNo(requestDto.getVehicleNo())
                .driverName(requestDto.getDriverName())
                .remarks(requestDto.getRemarks())
                .build();
    }

    public DispatchNoteResponseDto toResponseDto(DispatchNote dispatchNote) {
        if (dispatchNote == null) {
            return null;
        }

        return DispatchNoteResponseDto.builder()
                .dispatchId(dispatchNote.getDispatchId())
                .salesOrderId(dispatchNote.getSalesOrderId())
                .dispatchDate(dispatchNote.getDispatchDate())
                .vehicleNo(dispatchNote.getVehicleNo())
                .driverName(dispatchNote.getDriverName())
                .remarks(dispatchNote.getRemarks())
                .build();
    }
}
