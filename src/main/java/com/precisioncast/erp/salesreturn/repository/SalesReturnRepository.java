package com.precisioncast.erp.salesreturn.repository;

import com.precisioncast.erp.salesreturn.entity.SalesReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesReturnRepository extends JpaRepository<SalesReturn,Long> {
}
