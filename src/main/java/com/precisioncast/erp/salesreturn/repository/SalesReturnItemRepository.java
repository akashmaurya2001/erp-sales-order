package com.precisioncast.erp.salesreturn.repository;

import com.precisioncast.erp.salesreturn.entity.SalesReturnItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesReturnItemRepository extends JpaRepository<SalesReturnItem,Long> {

    List<SalesReturnItem> findByReturnId(Long returnId);
}
