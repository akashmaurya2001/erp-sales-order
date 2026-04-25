package com.precisioncast.erp.dispatchnote.repository;

import com.precisioncast.erp.dispatchnote.entity.DispatchNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DispatchNoteRepository extends JpaRepository<DispatchNote, Long> {

    List<DispatchNote> findBySalesOrderId(Long salesOrderId);

    List<DispatchNote> findByVehicleNo(String vehicleNo);

    List<DispatchNote> findByDispatchDateBetween(LocalDate from, LocalDate to);
}
